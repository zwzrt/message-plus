package cn.redcoral.messageplus.port;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.data.entity.po.HistoryMessagePo;
import cn.redcoral.messageplus.data.service.HistoryMessageService;
import cn.redcoral.messageplus.manage.UserManage;
import cn.redcoral.messageplus.properties.MessagePlusMessageProperties;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.utils.BeanUtil;
import cn.redcoral.messageplus.utils.RetryUtil;
import cn.redcoral.messageplus.utils.cache.CacheUtil;
import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 *  向开发者提供的工具类
 * @author mo
 **/
public class MessagePlusUtil {

    private static class User {
        public String id;
        public String token;
        public long loginTime;

        public User(String id, String token) {
            this.id = id;
            this.token = token;
            this.loginTime = System.currentTimeMillis();
        }
    }


    /**
     * 存储用户Token及User
     */
    private static final Map<String, User> tokenUserMap = new ConcurrentHashMap<>();
    /**
     * 存储用户Token及User
     */
    private static final Map<String, User> idUserMap = new ConcurrentHashMap<>();
    /**
     * token过期时间
     */
    public static long tokenExpirationTime = TimeUnit.MINUTES.toMillis(MessagePlusProperties.tokenExpirationTime);


    /**
     * 登录用户
     * @param token 用户token
     * @param id 用户ID
     */
    public static void login(String token, String id) {
        User user = new User(id, token);
        tokenUserMap.put(token, user);
        idUserMap.put(id, user);
    }

    /**
     * 登出
     * @param user 用户
     */
    private static void logout(User user) {
        tokenUserMap.remove(user.token);
        idUserMap.remove(user.id);
    }

    /**
     * 登出
     * @param token 用户token
     */
    public static void logoutByToken(String token) {
        User user = tokenUserMap.remove(token);
        if (user!=null) idUserMap.remove(user.id);
    }

    /**
     * 登出
     * @param id 用户id
     */
    public static void logoutById(String id) {
        User user = idUserMap.remove(id);
        if (user!=null) tokenUserMap.remove(user.token);
    }

    /**
     * 是否在线
     * @param token 用户token
     * @return 是否在线
     */
    public static boolean isOnlineByToken(String token) {
        User user = tokenUserMap.get(token);
        if (user==null) return false;
        // 获取当前时间
        long now = System.currentTimeMillis();
        // 在线
        if (user.loginTime + tokenExpirationTime < now) return true;
        // 不在线
        else  {
            // 顺便删除
            logout(user);
            return false;
        }
    }

    /**
     * 是否在线
     * @param id 用户id
     * @return 是否在线
     */
    public static boolean isOnlineById(String id) {
        User user = idUserMap.get(id);
        if (user==null) return false;
        // 获取当前时间
        long now = System.currentTimeMillis();
        // 在线
        if (user.loginTime + tokenExpirationTime < now) return true;
            // 不在线
        else  {
            // 顺便删除
            logout(user);
            return false;
        }
    }

    /**
     * 通过token获取ID
     * @param token 令牌
     * @return ID
     */
    public static String getIdByToken(String token) {
        User user = tokenUserMap.get(token);
        long now = System.currentTimeMillis();

        if(user==null){
            return null;
        }
        // 在线
        if (user.loginTime + tokenExpirationTime < now) {
            return user.id;
        }
        // 不在线（Token已过期）
        else  {
            // 顺便删除
            logout(user);
            return null;
        }
    }
    
    public static boolean checkIdAndToken(String token,String userId){
        if(MessagePlusProperties.tokenExpirationTime==0){
            return true;
        }
        if(userId==null){
            return false;
        }
        String Id = getIdByToken(token);
        return userId.equals(Id);
    }
    
    public static void ResendMessage(String receiverId) {
        // 消息重发，直接调用messagecahche遍历发送，简化代码
        CacheUtil cacheUtil = BeanUtil.chatCache();
        List<HistoryMessagePo> list = cacheUtil.getChatSingleContent(receiverId);
        
        MessagePlusMessageProperties properties = BeanUtil.messagePersistenceProperties();
        HistoryMessageService historyMessageService = BeanUtil.historyService();
        
        synchronized (CachePrefixConstant.FAIL_MSG + receiverId)
        {
            if (list != null)
            {
                //准备重发
                while (true)
                {
                    if (list.isEmpty())
                    {
                        // 队列为空删除缓存
                        //                    chatSingleCacheUtil.removeCache(sid);
                        break;
                    }
                    HistoryMessagePo message = list.remove(0);
                    //取到了消息准备重发
                    RetryUtil.retry(properties.getRetryCount(), properties.getIntervalTime(),
                            () -> {
                                Message msg = cn.hutool.core.bean.BeanUtil.copyProperties(message, Message.class);
                                msg.setData(JSON.parse(message.getData()));
                                boolean flag = UserManage.sendMessage(receiverId, msg);
                                if (!flag)
                                {
                                    throw new RuntimeException();
                                }
                            },
                            (bo) -> {
                                if (bo)
                                {
                                    //成功重发，修改数据库
                                    historyMessageService.updateMessage(message.getId(), false);
                                }
                            }
                    );
                }
                
            }
        }
    }
}
