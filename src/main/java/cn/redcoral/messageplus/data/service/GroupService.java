package cn.redcoral.messageplus.data.service;

import cn.redcoral.messageplus.data.entity.Group;

import java.util.List;

/**
 * @author mo
 **/
public interface GroupService {

    /**
     * 创建群组
     * @param group 群组
     */
    void createGroup(Group group);




    /**
     * 获取群组总数
     */
    Long selectGroupNum();

    /**
     * 获取全部群组
     */
    List<Group> selectAllGroup();

    /**
     * 模糊查询群组
     * @param name 群组名称
     * @return 群组列表
     */
    List<Group> likeByName(String name);

    /**
     * 查询指定用户的群组
     * @return 群组列表
     */
    List<Group> selectGroupListByCreateId(String createUserId);

    /**
     * 根据群组ID查询群组信息
     */
    Group selectGroupById(String groupId);

    /**
     * 根据群组名称和创建者ID查询群组ID
     * @param name 群组名称
     * @param createId 创建者ID
     * @return 群组ID
     */
    String selectGroupByNameAndCreateId(String name, String createId);

    /**
     * 查询群组的总人数
     * @param groupId 群组ID
     * @return 群组人数
     */
    int selectUserNumById(String groupId);



    /**
     * 修改名称
     * @param groupId 群组ID
     * @param newName 群组新名称
     * @return 是否成功
     */
    boolean updateGroupName(String groupId, String newName);
    
    boolean deleteGroup(String groupId);
    
    boolean joinGroup(String groupId, String userId);
    
    boolean signOutGroup(String groupId, String userId);

}
