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
     * 获取全部群组
     */
    List<Group> selectAllGroup();

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
}
