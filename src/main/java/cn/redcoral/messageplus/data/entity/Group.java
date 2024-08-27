package cn.redcoral.messageplus.data.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.redcoral.messageplus.data.entity.po.GroupPo;
import cn.redcoral.messageplus.manage.MessageManage;
import cn.redcoral.messageplus.utils.SnowflakeIDUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
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
        this.id = SnowflakeIDUtil.getGroupID();
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
        // 创建的用户加入群组
        group.joinGroup(createUserId);
        //让每个在群组里的用户加入该群组
        clientIdList.forEach(group::joinGroup);
        return group;
    }
    /**
     * 创建群组
     * @return 群组
     */
    public static Group BuildGroup(GroupPo groupPo) {
        if (groupPo == null) return null;
        Group group = new Group();
        BeanUtil.copyProperties(groupPo, group);
        return group;
    }

    public static List<Group> BuildGroupList(List<GroupPo> groupPos) {
        List<Group> groupList = new ArrayList<>();
        for (GroupPo groupPo : groupPos) {
            groupList.add(BuildGroup(groupPo));
        }
        return groupList;
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
        if (MessageManage.userIdSessionMap.get(userId)!=null&&index!=-1) {
            //聊天室在线人数加一
            userNumLock.lock();
            this.onlineUserNum++;
            userNumLock.unlock();
        }
        // 在线但是没有加入
        else if (MessageManage.userIdSessionMap.get(userId)==null&&index==-1) {
            //加入的人数，和聊天室在线人数加一
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
     * @deprecated
     * 上线
     */
    @Deprecated
    public void topLine(String userId) {
        userNumLock.lock();
        this.onlineUserNum++;
        userNumLock.unlock();
    }
    /**
     * @deprecated
     * 下线
     */
    @Deprecated
    public void downLine(String userId) {
        userNumLock.lock();
        this.onlineUserNum--;
        userNumLock.unlock();
    }
    /**
     * 获取群组的简化字符串表示
     * 此方法用于生成一个包含创建者ID和群组名称的字符串，用于快速比较两个群组是否相同
     * @return 群组的简化字符串表示
     */
    private String getEasyStr() {
        return this.getCreateUserId()+":"+this.getName();
    }
    
    /**
     * 获取群组的简化字符串表示
     * 这是一个静态方法，用于外部调用生成群组的简化字符串表示
     * @param createUserId 群组的创建者ID
     * @param name 群组的名称
     * @return 群组的简化字符串表示
     */
    private static String getEasyStr(String createUserId, String name) {
        return createUserId+":"+name;
    }

    /**
     * 判断两个群组是否相同
     * 此方法使用群组的简化字符串表示来比较两个群组是否相同
     * @param group1 第一个群组对象
     * @param group2 第二个群组对象
     * @return 如果两个群组相同返回true，否则返回false
     */
    public static boolean isSame(Group group1, Group group2) {
        return group1.getEasyStr().equals(group2.getEasyStr());
    }
    
    /**
     * 判断给定的群组信息与一个群组对象是否相同
     * 此方法用于在已知群组创建者ID和群组名称的情况下，判断该群组与另一个群组对象是否相同
     * @param createUserId 群组的创建者ID
     * @param name 群组的名称
     * @param group2 待比较的群组对象
     * @return 如果两个群组相同返回true，否则返回false
     */
    public static boolean isSame(String createUserId, String name, Group group2) {
        return getEasyStr(createUserId, name).equals(group2.getEasyStr());
    }

}
