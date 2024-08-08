package cn.redcoral.messageplus.utils.cache.impl;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.utils.cache.ChatSingleCacheUtil;
import com.github.benmanes.caffeine.cache.Cache;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.redcoral.messageplus.utils.cache.MessageData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author mosang
 * time 2024/8/6
 */
@Service
public class ChatSingleCacheUtilImpl implements ChatSingleCacheUtil {
    
    @Autowired
    private Cache<String, BlockingQueue> messageQueueCache;
    
    /**
     * 添加聊天内容
     * @param senderId 发送者
     * @param receiverId 接受者
     * @param message 内容
     */
    @Override
    public void addChatSingleContent(String senderId, String receiverId, Message message) {
        BlockingQueue messagequeue = messageQueueCache.getIfPresent(CachePrefixConstant.USER_MESSAGES_PREFIX
                + receiverId + ":"
                + senderId);
        if(messagequeue!=null){
            //队列存在
            messagequeue.add(message);
        }else {
            //队列不存在
            BlockingQueue queue = new ArrayBlockingQueue(100);
            queue.add(message);
            messageQueueCache.put(CachePrefixConstant.USER_MESSAGES_PREFIX + receiverId + ":" + senderId,queue);
        }
    }
    
    /**
     * 获取聊天内容的队列
     * @param receiverId
     * @return
     */
    @Override
    public CopyOnWriteArrayList<MessageData> removeChatSingleContent(String receiverId) {
        //匹配接受者id
        ConcurrentMap<@NonNull String, @NonNull BlockingQueue> map = messageQueueCache.asMap();
        Set<@NonNull String> keySet = map.keySet();
        CopyOnWriteArrayList<MessageData> messagDataS = null;
        for (String key : keySet) {
            String[] receiverIdS = key.split(":");
            String receiver = receiverIdS[1];
            MessageData messagData = new MessageData();
            if (receiverId.equals(receiver)){
                BlockingQueue queue = map.get(key);
                List<Message> messages = Collections.synchronizedList(new ArrayList<Message>());
                queue.forEach((message)->{
                    messages.add((Message) message);
                });
                messagData.setSendId(receiverIdS[2]);
                messagData.setMessages(messages);
            }
            messagDataS.add(messagData);
        }
        return messagDataS;
    }
}
