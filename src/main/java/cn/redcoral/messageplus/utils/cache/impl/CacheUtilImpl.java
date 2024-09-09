package cn.redcoral.messageplus.utils.cache.impl;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.data.entity.po.HistoryMessagePo;
import cn.redcoral.messageplus.port.MessagePlusUtil;
import cn.redcoral.messageplus.utils.cache.CacheUtil;
import cn.redcoral.messageplus.utils.cache.MPCache;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mosang
 * time 2024/9/9
 */
@Service
public class CacheUtilImpl implements CacheUtil {
    
    @Autowired
    private MPCache<String, List<HistoryMessagePo>> messageListCache;
    
    @Override
    public void addChatContent(String receiverId, HistoryMessagePo message) {
        //        log.info("准备缓存");
        //key:prefix+receiverId，value:queue<message>
        String key = CachePrefixConstant.FAIL_MSG +receiverId;
        List<HistoryMessagePo> list = messageListCache.getIfPresent(key);
        //队列不存在
        if(list==null){
            List<HistoryMessagePo> list1 = new ArrayList<>();
            list1.add(message);
            messageListCache.put(key,list1);
            return;
        }
        //队列存在
        list.add(message);
    }
    
    @Override
    public void removeCache(String receiverId) {
        messageListCache.invalidate(CachePrefixConstant.FAIL_MSG + receiverId);
    }
    
    @Override
    public List<HistoryMessagePo> getChatSingleContent(String receiverId) {
        //匹配接受者id
        //        ConcurrentMap<@NonNull String, @NonNull List<HistoryMessagePo>> map = messageListCache.asMap();
        //        List<HistoryMessagePo> list = map.get(CachePrefixConstant.CHAT_SINGLE_CONTENT + receiverId);
        List<HistoryMessagePo> list = messageListCache.getIfPresent(CachePrefixConstant.FAIL_MSG + receiverId);
        //该用户没有消息
        return list;
    }
}
