package cn.redcoral.messageplus.port;

import cn.redcoral.messageplus.entity.Group;

/**
 * 查询群组二级接口类
 * @author mo
 * @日期: 2024-06-11 21:06
 **/
public interface GroupInterface {
    /**
     * 开发者自定义的群组查询接口（二级）
     * @param groupId 群组ID
     * @return 群组
     */
    Group getGroupInCustom(String groupId);
}
