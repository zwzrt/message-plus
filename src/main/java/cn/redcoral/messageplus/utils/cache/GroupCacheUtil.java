package cn.redcoral.messageplus.utils.cache;

import cn.redcoral.messageplus.data.entity.Group;

import java.util.function.Function;

/**
 * 群组缓存
 * @author mo
 **/
public interface GroupCacheUtil {

    /**
     * 保存群组信息
     */
    void setGroup(Group group);

    /**
     * 通过ID查询群组
     * @param groupId 群组ID
     */
    Group getGroupById(String groupId);

    /**
     * 通过ID查询群组
     * @param groupId 群组ID
     * @param mappingFunction 替补查询方案
     */
    public Group getGroupById(String groupId, Function<? super String, ? extends String> mappingFunction);

}
