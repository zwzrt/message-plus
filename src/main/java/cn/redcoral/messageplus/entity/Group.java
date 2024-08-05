package cn.redcoral.messageplus.entity;

import cn.redcoral.messageplus.utils.MessagePlusUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 群组
 * @author mo
 **/
@Data
public class Group {
    /**
     * 群组ID
     */
    private String id;
    /**
     * 创建者ID
     */
    private String createUserId;
    /**
     * 群组名称
     */
    private String name;
    /**
     * 总人数锁
     */
    private static Lock userNumLock = new ReentrantLock();
    /**
     * 用户数量
     */
    private int userNum;
    /**
     * 在线用户数量
     */
    private int onlineUserNum;
    /**
     * 用户ID列表
     */
    private List<String> clientIdList;

    public Group() {
        this.id = UUID.randomUUID().toString();
        this.userNum = 0;
        this.clientIdList = new ArrayList<>();
    }

    /**
     * 创建群组
     * @param id 群组ID
     * @param createUserId 创建者ID
     * @param groupName 群组名称
     * @param clientIdList 成员ID（无需携带创建者ID）
     * @return 群组
     */
    public static Group BuildGroup(String id, String createUserId, String groupName, List<String> clientIdList) {
        Group group = new Group();
        group.id = id;
        group.createUserId = createUserId;
        group.name = groupName;
        // 加入群组
        group.joinGroup(createUserId);
        clientIdList.forEach(group::joinGroup);
        return group;
    }

    /**
     * 加入群组
     * @param userId 用户ID
     * @return 总人数
     */
    public int joinGroup(String userId) {
        if (userId==null) return -1;

        int index = clientIdList.indexOf(userId);
        // 在线且已经加入
        if (MessagePlusUtils.userIdSessionMap.get(userId)!=null&&index!=-1) {
            userNumLock.lock();
            this.onlineUserNum++;
            userNumLock.unlock();
        }
        // 不在线但是没有加入
        else if (MessagePlusUtils.userIdSessionMap.get(userId)==null&&index==-1) {
            userNumLock.lock();
            userNum++;
            this.onlineUserNum++;
            userNumLock.unlock();
        }
        // 不在线
        else {
            userNumLock.lock();
            userNum++;
            userNumLock.unlock();
        }


        // 未加入群组
        if (index==-1) {
            // 加入群组
            clientIdList.add(userId);
        }
//        userIdDialogueMap.put(userId, dialogue);

        return userNum;
    }
    /**
     * 上线
     */
    public void topLine(String userId) {
        userNumLock.lock();
        this.onlineUserNum++;
        userNumLock.unlock();
    }
    /**
     * 下线
     */
    public void downLine(String userId) {
        userNumLock.lock();
        this.onlineUserNum--;
        userNumLock.unlock();
    }

    private String getEasyStr() {
        return this.getCreateUserId()+":"+this.getName();
    }
    private static String getEasyStr(String createUserId, String name) {
        return createUserId+":"+name;
    }

    /**
     * 判断两个群组是否相同
     * @param group1 群组ID
     * @param group2 群组ID
     */
    public static boolean isSame(Group group1, Group group2) {
        return group1.getEasyStr().equals(group2.getEasyStr());
    }
    /**
     * 判断两个群组是否相同
     * @param group2 群组ID
     */
    public static boolean isSame(String createUserId, String name, Group group2) {
        return getEasyStr(createUserId, name).equals(group2.getEasyStr());
    }
}
