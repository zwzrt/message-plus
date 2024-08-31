package cn.redcoral.messageplus.port;

import cn.redcoral.messageplus.properties.MessagePlusProperties;

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

}
