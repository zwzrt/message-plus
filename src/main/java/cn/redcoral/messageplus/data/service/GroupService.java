package cn.redcoral.messageplus.data.service;

import cn.redcoral.messageplus.data.entity.Group;

/**
 * @author mo
 **/
public interface GroupService {

    /**
     * 根据群组ID查询群组信息
     */
    Group selectGroupById(String groupId);

}
