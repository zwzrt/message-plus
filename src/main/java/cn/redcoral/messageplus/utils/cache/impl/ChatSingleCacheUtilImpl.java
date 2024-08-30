package cn.redcoral.messageplus.utils.cache.impl;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.data.entity.po.HistoryMessagePo;
import cn.redcoral.messageplus.utils.cache.ChatSingleCacheUtil;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * @author mosang
 * time 2024/8/6
 */
@Service
@Slf4j
public class ChatSingleCacheUtilImpl implements ChatSingleCacheUtil {
    
    @Autowired
    private Cache<String, BlockingQueue> messageQueueCache;
    
    /**
     * 添加聊天内容
     * @param receiverId 接受者
     * @param message 内容
     */
    @Override
    public void addChatContent( String receiverId, HistoryMessagePo message) {
//        log.info("准备缓存");
        //key:prefix+receiverId，value:queue<message>
        String key = CachePrefixConstant.CHAT_SINGLE_CONTENT+receiverId;
        BlockingQueue queue = messageQueueCache.getIfPresent(key);
        //队列不存在
        if(queue==null){
            BlockingQueue blockingQueue = new ArrayBlockingQueue(1000);
            blockingQueue.add(message);
            messageQueueCache.put(key,blockingQueue);
            return;
        }
        //队列存在
        queue.add(message);
    }
    
    /**
     * 获取聊天内容的队列
     * @param receiverId
     * @return
     */
    //改成两个都可以用的
    @Override
    public BlockingQueue getChatSingleContent(String receiverId) {
        //匹配接受者id
        ConcurrentMap<@NonNull String, @NonNull BlockingQueue> map = messageQueueCache.asMap();
        Set<@NonNull String> keySet = map.keySet();
        for (String key : keySet)
        {
            String receiver = key.split(":")[2];
            if(receiverId.equals(receiver)){
                //找到这条记录
                return messageQueueCache.getIfPresent(key);
            }
        }
        //该用户没有消息
        return null;
    }
    
    @Override
    public void removeCache(String receiverId) {
        ConcurrentMap<@NonNull String, @NonNull BlockingQueue> map = messageQueueCache.asMap();
        Set<@NonNull String> keySet = map.keySet();
        for (String key : keySet)
        {
            String[] keyStrs = key.split(":");
            if(receiverId.equals(keyStrs[2])){
                messageQueueCache.invalidate(key);
            }
        }
    }
}
