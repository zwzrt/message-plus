package cn.redcoral.messageplus.utils;

import cn.redcoral.messageplus.entity.Group;

import javax.websocket.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mo
 * @Description:
 * @日期: 2024-05-14 20:44
 **/
public class ChatUtils {

    /**
     * 群组管理
     **/
    private static GroupManage groupManage;

    protected static GroupManage getGroupManage() {
        if (groupManage == null) groupManage = SpringUtils.getBean(GroupManage.class);
        return groupManage;
    }

    /**
     * 会话Map
     */
    public static ConcurrentHashMap<String, Session> userIdSessionMap = new ConcurrentHashMap<>();
    /**
     * 总在线人数锁
     */
    private static Lock userNumLock = new ReentrantLock();
    /**
     * 总在线人数
     */
    private static Long userNum = 0L;

    /**
     * 加入聊天
     * @param id 用户ID
     * @param session 用户对应session
     * @return 返回全部在线人数
     */
    public static Long joinChat(String id, Session session) {
        // 为空则多个在线人数，在线人数加一；若不为空，则为顶替，无需加一
        if (userIdSessionMap.get(id)==null) {
            userNumLock.lock();
            userNum++;
            userNumLock.unlock();
        }
        // 录入session库
        userIdSessionMap.put(id, session);
//        List<String> groupIdList = userByGroupIdMap.get(id);
//        if (groupIdList!=null) {
//            groupIdList.forEach((groupId)->{
//                idGroupMap.get(groupId).joinGroup(id, new Dialogue(session));
//            });
//        }
        return userNum;
    }
    /**
     * 退出聊天
     * @param id 用户ID
     * @return 返回全部在线人数
     */
    public static Long quitChat(String id) {
        // 为空则多个在线人数，在线人数加一；若不为空，则为顶替，无需加一
        if (userIdSessionMap.get(id)!=null) {
            userIdSessionMap.remove(id);
            userNumLock.lock();
            userNum--;
            userNumLock.unlock();
        }
        return userNum;
    }

    /**
     * 给指定用户发送消息
     */
    public static boolean sendMessage(String id, String msg) {
        Session session = userIdSessionMap.get(id);
        if (session==null) {
            return false;
        } else {
            sendMessage(session, msg);
            return true;
        }
    }

    /**
     * 创建群组
     * @param createUserId 创建者ID
     * @param name 群组名称
     * @param client_ids 群成员ID
     * @return 群组ID
     */
    public static Group createGroup(String createUserId, String name, List<String> client_ids) {
        return getGroupManage().createGroup(createUserId, name, client_ids);
    }

    /**
     * 加入群组
     * @param userId 用户ID
     * @param session 用户session（为空则代表不在线）
     * @param groupId 群组ID
     */
    public static int joinGroup(String userId, Session session, String groupId) {
        Group group = getGroupManage().getGroupById(groupId);
        if (group==null) return -1;
        return group.joinGroup(userId);
    }

    /**
     * 群发（包括自己）
     * @param groupId
     * @param message
     * @return 失败用户ID
     */
    public static List<String> sendMessageToGroup(String groupId, String message) {
        Group group = getGroupManage().getGroupById(groupId);
        if (group==null) return Collections.emptyList();
//        group.getUserIdDialogueMap().forEachEntry(group.getUserIdDialogueMap().size(), action -> {
//            // 在线
//            if (action.getValue().isOnLine()) {
//                sendMessage(action.getValue().getSession(), message);
//            }
//        });
        List<String> offLineClientIdList = new ArrayList<>();
        group.getClientIdList().forEach(clientId->{
            // 在线
            if (userIdSessionMap.get(clientId)!=null) {
                sendMessage(userIdSessionMap.get(clientId), message);
            } else {
                offLineClientIdList.add(clientId);
            }
        });
        return offLineClientIdList;
    }
    /**
     * 群发（不包括自己）
     * @param userId
     * @param groupId
     * @param message
     * @return 失败用户ID
     */
    public static List<String> sendMessageToGroupBarringMe(String userId, String groupId, String message) {
        Group group = getGroupManage().getGroupById(groupId);
        if (group==null) return Collections.emptyList();
//        group.getUserIdDialogueMap().forEachEntry(group.getUserIdDialogueMap().size(), action -> {
//            if (action.getKey().equals(userId)) return;
//            sendMessage(action.getValue().getSession(), message);
//        });
        List<String> offLineClientIdList = new ArrayList<>();
        group.getClientIdList().forEach(clientId->{
            if (clientId.equals(userId)) return;
            // 在线
            if (userIdSessionMap.get(clientId)!=null) {
                sendMessage(userIdSessionMap.get(clientId), message);
            } else {
                offLineClientIdList.add(clientId);
            }
        });
        return offLineClientIdList;
    }

    /**
     * 服务端发送消息
     */
    protected static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取总在线人数
     */
    public static Long getUserNum() {
        return userNum;
    }
    /**
     * 获取对应ID群组
     */
    public static Group getGroupById(String groupId) {
        return getGroupManage().getGroupById(groupId);
    }

}
