package cn.redcoral.messageplus.data.service;

/**
 * @author mo
 **/
public interface UserBlacklistService {

    /**
     * 拉黑
     * @param id1 用户ID
     * @param id2 拉黑用户ID
     */
    boolean black(String id1, String id2);


    /**
     * 取消拉黑
     * @param id1 用户ID
     * @param id2 拉黑用户ID
     */
    boolean noBlack(String id1, String id2);


    /**
     * 是否被拉黑
     * @param id1 我的ID
     * @param id2 拉黑我的用户ID
     * @return 是否
     */
    boolean whetherPulledBlack(String id1, String id2);
}
