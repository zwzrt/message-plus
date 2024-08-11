package cn.redcoral.messageplus.utils.cache.impl;

import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.utils.cache.GroupCacheUtil;
import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * @author mo
 **/
@Service
public class GroupCacheUtilImpl implements GroupCacheUtil {

    @Autowired
    private Cache<String, String> stringCache;

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
}
