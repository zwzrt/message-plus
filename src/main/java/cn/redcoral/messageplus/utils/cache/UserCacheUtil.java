package cn.redcoral.messageplus.utils.cache;

import java.util.function.Function;

/**
 * 操作Redis工具类
 * @author mo
 **/
public interface UserCacheUtil {

    /**
     * 存储用户ID及所在服务ID
     * @param id 用户ID
     */
    public void setUserService(String id);

    /**
     * 存储是否被拉黑
     * @param id 拉黑用户
     * @param blackId 被拉黑用户
     * @param isBlack 是否拉黑
     */
    public void setIsBlack(String id, String blackId, boolean isBlack);



    /**
     * 获取用户ID及所在服务ID
     * @param id 用户ID
     */
    public String getUserService(String id);

    /**
     * 查询是否被拉黑
     * @param id 拉黑用户ID
     * @param blackId 被拉黑用户ID
     * @param function 不存在的后续方法
     * @return 是否
     */
    public Boolean getIsBlack(String id, String blackId, Function<? super String, ? extends Boolean> function);


}
