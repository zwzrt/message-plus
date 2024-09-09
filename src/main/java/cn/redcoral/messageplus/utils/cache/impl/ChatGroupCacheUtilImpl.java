package cn.redcoral.messageplus.utils.cache.impl;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.constant.GroupCacheConstant;
import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.data.entity.po.HistoryMessagePo;
import cn.redcoral.messageplus.utils.cache.ChatGroupCacheUtil;
import cn.redcoral.messageplus.utils.cache.MPCache;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * @author mo
 **/
@Service
@Slf4j
public class ChatGroupCacheUtilImpl implements ChatGroupCacheUtil {

    @Autowired
    private MPCache<String, String> stringCache;
    @Autowired
    private MPCache<String, Integer> intCache;
    @Autowired
    private MPCache<String, List<HistoryMessagePo>> messageListCache;
    

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
//        log.info("准备缓存");
        //key:prefix+receiverId，value:queue<message>
        String key = CachePrefixConstant.FAIL_MSG +receiverId;
        List<HistoryMessagePo> list = messageListCache.getIfPresent(key);
        //队列不存在
        if(list==null){
            ArrayList<HistoryMessagePo> list1 = new ArrayList<>();
            list1.add(message);
            messageListCache.put(key,list1);
            return;
        }
        //队列存在
        list.add(message);
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
