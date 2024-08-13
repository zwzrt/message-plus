package cn.redcoral.messageplus.data.service.impl;

import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.data.entity.po.GroupPo;
import cn.redcoral.messageplus.data.mapper.MessagePlusGroupMapper;
import cn.redcoral.messageplus.data.service.GroupService;
import cn.redcoral.messageplus.utils.cache.ChatGroupCacheUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mo
 **/
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private MessagePlusGroupMapper groupMapper;
    @Autowired
    private ChatGroupCacheUtil chatGroupCacheUtil;


    @Override
    public void createGroup(Group group) {
        GroupPo groupPo = GroupPo.BuildGroupPo(group);
        groupMapper.insert(groupPo);
        // 加入缓存
        chatGroupCacheUtil.setGroup(group);
    }

    @Override
    public List<Group> selectAllGroup() {
        return Group.BuildGroupList(groupMapper.selectList(null));
    }

    @Override
    public Group selectGroupById(String groupId) {
        // 从缓存中获取
        Group group = chatGroupCacheUtil.getGroupById(groupId);
        // 若不存在则再数据库查询
        if (group == null) {
            GroupPo groupPo = groupMapper.selectById(groupId);
            if (groupPo != null) {
                group = Group.BuildGroup(groupPo);
                // 存储到缓存中
                chatGroupCacheUtil.setGroup(group);
            }
        }
        return group;
    }

    @Override
    public String selectGroupByNameAndCreateId(String name, String createId) {
        // 1、从缓存中查询群组ID
        String groupId = chatGroupCacheUtil.getGroupByNameAndCreateId(name, createId);
        // 不存在，向数据库查询
        if (groupId == null) {
            LambdaQueryWrapper<GroupPo> lqw = new LambdaQueryWrapper<>();
            lqw.eq(GroupPo::getName, name);
            lqw.eq(GroupPo::getCreateUserId, createId);
            GroupPo groupPo = groupMapper.selectOne(lqw);
            // 若查询到，则返回群组ID，并且重新向缓存添加数据
            if (groupPo != null) {
                groupId = groupPo.getId();
                chatGroupCacheUtil.setGroup(Group.BuildGroup(groupPo));
            }
        }
        return groupId;
    }

}
