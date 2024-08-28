package cn.redcoral.messageplus.data.service.impl;

import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.data.entity.po.GroupPo;
import cn.redcoral.messageplus.data.mapper.MessagePlusGroupMapper;
import cn.redcoral.messageplus.data.service.GroupService;
import cn.redcoral.messageplus.utils.cache.ChatGroupCacheUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author mo
 **/
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private MessagePlusGroupMapper groupMapper;
    @Autowired
    private ChatGroupCacheUtil groupCacheUtil;


    @Override
    public void createGroup(Group group) {
        GroupPo groupPo = GroupPo.BuildGroupPo(group);
        groupMapper.insert(groupPo);
        // 加入缓存
        groupCacheUtil.setGroup(group);
    }

    @Override
    public List<Group> selectAllGroup() {
        return Group.BuildGroupList(groupMapper.selectList(null));
    }

    @Override
    public List<Group> likeByName(String name) {
        LambdaQueryWrapper<GroupPo> lqw = new LambdaQueryWrapper<>();
        lqw.like(GroupPo::getName, name);
        return Group.BuildGroupList(groupMapper.selectList(lqw));
    }

    @Override
    public List<Group> selectGroupListByCreateId(String createUserId) {
        LambdaQueryWrapper<GroupPo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(GroupPo::getCreateUserId, createUserId);
        return Group.BuildGroupList(groupMapper.selectList(lqw));
    }

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

    @Override
    public String selectGroupByNameAndCreateId(String name, String createId) {
        // 1、从缓存中查询群组ID
        String groupId = groupCacheUtil.getGroupByNameAndCreateId(name, createId);
        // 不存在，向数据库查询
        if (groupId == null) {
            LambdaQueryWrapper<GroupPo> lqw = new LambdaQueryWrapper<>();
            lqw.eq(GroupPo::getName, name);
            lqw.eq(GroupPo::getCreateUserId, createId);
            GroupPo groupPo = groupMapper.selectOne(lqw);
            // 若查询到，则返回群组ID，并且重新向缓存添加数据
            if (groupPo != null) {
                groupId = groupPo.getId();
                groupCacheUtil.setGroup(Group.BuildGroup(groupPo));
            }
        }
        return groupId;
    }

    @Override
    public int selectUserNumById(String groupId) {
        return groupCacheUtil.getUserNumById(groupId, ()->{
            GroupPo groupPo = groupMapper.selectById(groupId);
            if (groupPo != null) return groupPo.getUserNum();
            else return -1;
        });
    }
    
    @Override
    public boolean deleteGroup(String groupId) {
        int flag = groupMapper.deleteById(groupId);
        return flag>0;
    }
    
    @Override
    public boolean joinGroup(String groupId, String userId) {
        GroupPo groupPo = groupMapper.selectById(groupId);
        List<String> ids = JSON.parseArray(groupPo.getClientIds(), String.class);
        ids.add(userId);
        UpdateWrapper<GroupPo> wrp = new UpdateWrapper<>();
        wrp.eq("id",groupId);
        wrp.set("client_ids",JSON.toJSONString(ids));
        int update = groupMapper.update(wrp);
        return update>0;
    }
    

    @Override
    public boolean updateGroupName(String groupId, String newName) {
        LambdaUpdateWrapper<GroupPo> lqw = new LambdaUpdateWrapper<>();
        lqw.eq(GroupPo::getId, groupId);
        lqw.set(GroupPo::getName, newName);
        return groupMapper.update(lqw) > 0;
    }

}
