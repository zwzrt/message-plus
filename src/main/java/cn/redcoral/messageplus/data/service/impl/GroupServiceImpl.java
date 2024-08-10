package cn.redcoral.messageplus.data.service.impl;

import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.data.entity.po.GroupPo;
import cn.redcoral.messageplus.data.mapper.MessagePlusGroupMapper;
import cn.redcoral.messageplus.data.service.GroupService;
import cn.redcoral.messageplus.utils.cache.GroupCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mo
 **/
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private MessagePlusGroupMapper groupMapper;
    @Autowired
    private GroupCacheUtil groupCacheUtil;


    @Override
    public Group selectGroupById(String groupId) {
        // 从缓存中获取
        Group group = groupCacheUtil.getGroupById(groupId);
        // 若不存在则再数据库查询
        if (group == null) {
            GroupPo groupPo = groupMapper.selectById(groupId);
            if (groupPo != null) {
                group = Group.BuildGroup(groupPo);
                // 存储到缓存中
                groupCacheUtil.setGroup(group);
            }
        }
        return group;
    }

}
