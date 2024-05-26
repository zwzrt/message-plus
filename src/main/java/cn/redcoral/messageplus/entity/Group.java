package cn.redcoral.messageplus.entity;

import cn.redcoral.messageplus.utils.ChatUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mo
 * @Description: 群组
 * @日期: 2024-05-14 20:45
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
     * 加入群组
     * @param userId
     * @param dialogue
     * @return 总人数
     */
    public int joinGroup(String userId, Dialogue dialogue) {
        if (userId==null||dialogue==null) return -1;

        // 在线且已经加入
        if (dialogue.isOnLine() && ChatUtils.userIdSessionMap.get(userId)!=null) {
            userNumLock.lock();
            this.onlineUserNum++;
            userNumLock.unlock();
        }
        // 在线但是没有加入
        else if (dialogue.isOnLine() && ChatUtils.userIdSessionMap.get(userId)==null) {
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
            // 加入群组
            clientIdList.add(userId);
        }

//        userIdDialogueMap.put(userId, dialogue);

        return userNum;
    }
}
