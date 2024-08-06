package cn.redcoral.messageplus.utils.cache;

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
     * 获取用户ID及所在服务ID
     * @param id 用户ID
     */
    public String getUserService(String id);


}
