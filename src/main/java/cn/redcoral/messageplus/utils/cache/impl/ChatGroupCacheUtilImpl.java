package cn.redcoral.messageplus.utils.cache.impl;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.constant.GroupCacheConstant;
import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.data.entity.po.HistoryMessagePo;
import cn.redcoral.messageplus.utils.cache.ChatGroupCacheUtil;
import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * @author mo
 **/
@Service
@Slf4j
public class ChatGroupCacheUtilImpl implements ChatGroupCacheUtil {

    @Autowired
    private Cache<String, String> stringCache;
    @Autowired
    private Cache<String, Integer> intCache;
    @Autowired
    private Cache<String,BlockingQueue> messageQueueCache;

    @Override
    public void setGroup(Group group) {
        stringCache.put(group.getId(), JSON.toJSONString(group));
        // 存储群组标识符
        stringCache.put(group.getCreateUserId()+":"+group.getName(), group.getId());
    }

    @Override
    public Group getGroupById(String groupId) {
        
        String jsonGroup = stringCache.get(groupId, (k)->null);
        return JSON.parseObject(jsonGroup, Group.class);
    }

    @Override
    public Group getGroupById(String groupId, Function<? super String, ? extends String> mappingFunction) {
        String jsonGroup = stringCache.get(groupId, mappingFunction);
        return JSON.parseObject(jsonGroup, Group.class);
    }

    @Override
    public String getGroupByNameAndCreateId(String name, String createUserId) {
        return stringCache.get(createUserId+":"+name, (k)->null);
    }
    
    @Override
    public void addChatContent(String senderId, String receiverId, HistoryMessagePo message) {
        log.info("准备缓存");
        //key:prefix+receiverId，value:queue<message>
        String key = CachePrefixConstant.CHAT_GROUP_CONTENT+receiverId;
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

    
    
    @Override
    public Integer getUserNumById(String groupId, Callable<Integer> call) {
        return intCache.get(GroupCacheConstant.GROUP_PREFIX+groupId, (k)->{
            try {
                return call.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
