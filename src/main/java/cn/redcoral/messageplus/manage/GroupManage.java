package cn.redcoral.messageplus.manage;

import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.data.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 群组管理
 * @author mo
 **/
@Configuration
public class GroupManage {

    /**
     * 每个用户ID对应的群组ID（用户ID，群组ID数组），用户加入的群组
     */
    private ConcurrentHashMap<String, List<String>> userByGroupIdMap = new ConcurrentHashMap<>();
    /**
     * 群组数组（群组ID，群组），当前有的群组
     */
    private ConcurrentHashMap<String, Group> idGroupMap = new ConcurrentHashMap<>();

    @Autowired
    private GroupService groupService;

    /**
     * 群组查询接口
     * @param groupId 群组ID
     * @return 群组
     */
    public Group getGroupById(String groupId) {
//        Group group = idGroupMap.get(groupId);
//        // 集合中不存在，向Redis查询
//        if (group == null) {
//            group = getGroupByIdInCache(groupId);
//        }
//        return group;
        return groupService.selectGroupById(groupId);
    }


    /**
     * 创建群组
     * @param createUserId 创建者ID
     * @param name 群组名称
     * @param client_ids 群成员ID
     * @return 群组ID
     */
    public Group createGroup(String createUserId, String name, List<String> client_ids) {
        // 查询该群组是否存在
        String groupId = groupService.selectGroupByNameAndCreateId(name, createUserId);
        // 存在
        if (groupId != null) {
            return groupService.selectGroupById(groupId);
        }
        // 不存在
        else {
            // 1、准备数据
            Group group = new Group();

            group.setCreateUserId(createUserId);
            group.setName(name);

            // 加入群组
            group.joinGroup(createUserId);
            client_ids.forEach(client_id->{
                group.joinGroup(client_id);
                // 记录用户的群组ID
                addUserByGroupIdMap(client_id, group.getId());
            });
            // 2、在数据库创建数据
            groupService.createGroup(group);
            // 3、返回数据
            return group;
        }
    }

    /**
     * 记录每个用户的群组ID
     * @param client_id 用户ID
     * @param groupId 群组ID
     */
    protected void addUserByGroupIdMap(String client_id, String groupId) {
        List<String> groupIdList = userByGroupIdMap.get(client_id);
        if (groupIdList==null) {
            groupIdList = new ArrayList<>();
            groupIdList.add(groupId);
            userByGroupIdMap.put(client_id, groupIdList);
        } else {
            groupIdList.add(groupId);
        }
    }




    /**
     * 查询群组总数
     */
    public Long getGroupNum() {
        return groupService.selectGroupNum();
    }

    /**
     * 模糊查询群组
     * @param name 群组名称
     * @return 群组列表
     */
    public List<Group> likeByName(String name) {
        return groupService.likeByName(name);
    }

    /**
     * 查询指定用户的群组
     * @return 群组列表
     */
    public List<Group> selectGroupListByCreateId(String createUserId) {
        return groupService.selectGroupListByCreateId(createUserId);
    }

    /**
     * 获取全部群组
     */
    public List<Group> getGroupList() {
        return groupService.selectAllGroup();
    }

    /**
     * 获取群组总人数
     */
    public int getUserNum(String groupId) {
        return groupService.selectUserNumById(groupId);
    }
    
    public boolean deleteGroup(String groupId,String userId) {
       return groupService.deleteGroup(groupId,userId);
    }
    
    public boolean joinGroup(String groupId, String userId) {
       return groupService.joinGroup(groupId,userId);
    }


    /**
     * 修改名称
     * @param groupId 群组ID
     * @param newName 群组新名称
     * @return 是否成功
     */
    public boolean updateGroupName(String userId,String groupId, String newName) {
        return groupService.updateGroupName(userId,groupId, newName);
    }
    
    public boolean signOutGroup(String groupId, String userId) {
        return groupService.signOutGroup(groupId,userId);
    }
    
    
    public boolean forbiddenSpeech(String token,String userId, String groupId) {
       return groupService.forbiddenSpeech(token,userId,groupId);
    }
    
    public boolean seerchForbiddenSpeech(String groupId){
        return groupService.seerchForbiddenSpeech(groupId);
    }
    
    public boolean NotForbiddenSpeech(String token, String userId, String groupId) {
        return groupService.notForbiddenSpeech(token, userId, groupId);
    }
}
