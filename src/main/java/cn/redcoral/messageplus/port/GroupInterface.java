package cn.redcoral.messageplus.port;

import cn.redcoral.messageplus.data.entity.Group;

/**
 * 查询群组二级接口类
 * @author mo
 **/
public interface GroupInterface {
    /**
     * 开发者自定义的群组查询接口（二级）
     * @param groupId 群组ID
     * @return 群组
     */
    Group getGroupInCustom(String groupId);
}
