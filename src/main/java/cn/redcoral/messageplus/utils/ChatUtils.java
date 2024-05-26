package cn.redcoral.messageplus.utils;

import cn.redcoral.messageplus.entity.Dialogue;
import cn.redcoral.messageplus.entity.Group;

import javax.websocket.*;
import java.io.IOException;
import java.util.ArrayList;
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
     * 会话Map
     */
    public static ConcurrentHashMap<String, Session> userIdSessionMap;
    /**
     * 总在线人数锁
     */
    private static Lock userNumLock = new ReentrantLock();
    /**
     * 总在线人数
     */
    private static Long userNum = 0L;
    /**
     * 每个用户ID对应的群组ID
     */
    private static ConcurrentHashMap<String, List<String>> userByGroupIdMap;
    /**
     * 群组数组
     */
    private static ConcurrentHashMap<String, Group> idGroupMap;

    static {
        userIdSessionMap = new ConcurrentHashMap<>();
        userByGroupIdMap = new ConcurrentHashMap<>();
        idGroupMap = new ConcurrentHashMap<>();
    }

    /**
     * 加入聊天
     * @param id 用户ID
     * @param session 用户对应session
     * @return 返回全部在线人数
     */
    public static Long joinChat(String id, Session session) {
        userIdSessionMap.put(id, session);
        // 为空则多个在线人数，在线人数加一；若不为空，则为顶替，无需加一
        if (userIdSessionMap.get(id)==null) {
            userNumLock.lock();
            userNum++;
            userNumLock.unlock();
        }
        List<String> groupIdList = ChatUtils.userByGroupIdMap.get(id);
        if (groupIdList!=null) {
            groupIdList.forEach((groupId)->{
                idGroupMap.get(groupId).joinGroup(id, new Dialogue(session));
            });
        }
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
    public static String createGroup(String createUserId, String name, List<String> client_ids) {
        Group group = new Group();

        group.setCreateUserId(createUserId);
        group.setName(name);
        // 加入群组
        group.joinGroup(createUserId, new Dialogue(userIdSessionMap.get(createUserId)));
        client_ids.forEach(client_id->{
            group.joinGroup(client_id, new Dialogue(null));
            // 记录用户的群组ID
            addUserByGroupIdMap(client_id, group.getId());
        });

        idGroupMap.put(group.getId(), group);

        return group.getId();
    }

    /**
     * 加入群组
     * @param userId 用户ID
     * @param session 用户session（为空则代表不在线）
     * @param groupId 群组ID
     */
    public static int joinGroup(String userId, Session session, String groupId) {
        Group group = idGroupMap.get(groupId);
        if (group==null) return -1;
        return group.joinGroup(userId, new Dialogue(session));
    }

    /**
     * 群发（包括自己）
     * @param groupId
     * @param message
     */
    public static void sendMessageToGroup(String groupId, String message) {
        Group group = idGroupMap.get(groupId);
        if (group==null) return;
//        group.getUserIdDialogueMap().forEachEntry(group.getUserIdDialogueMap().size(), action -> {
//            // 在线
//            if (action.getValue().isOnLine()) {
//                sendMessage(action.getValue().getSession(), message);
//            }
//        });
        group.getClientIdList().forEach(clientId->{
            // 在线
            if (userIdSessionMap.get(clientId)!=null) {
                sendMessage(userIdSessionMap.get(clientId), message);
            }
        });
    }

    /**
     * 群发（不包括自己）
     * @param userId
     * @param groupId
     * @param message
     */
    public static void sendMessageToGroup(String userId, String groupId, String message) {
        Group group = idGroupMap.get(groupId);
        if (group==null) return;
//        group.getUserIdDialogueMap().forEachEntry(group.getUserIdDialogueMap().size(), action -> {
//            if (action.getKey().equals(userId)) return;
//            sendMessage(action.getValue().getSession(), message);
//        });
        group.getClientIdList().forEach(clientId->{
            if (clientId.equals(userId)) return;
            // 在线
            if (userIdSessionMap.get(clientId)!=null) {
                sendMessage(userIdSessionMap.get(clientId), message);
            }
        });
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
     * 记录每个用户的群组ID
     * @param client_id
     * @param groupId
     */
    protected static void addUserByGroupIdMap(String client_id, String groupId) {
        List<String> groupIdList = userByGroupIdMap.get(client_id);
        if (groupIdList==null) {
            groupIdList = new ArrayList<>();
            groupIdList.add(groupId);
            userByGroupIdMap.put(client_id, groupIdList);
        } else {
            groupIdList.add(groupId);
        }
    }

    /**
     * 获取总在线人数
     */
    public static Long getUserNum() {
        return userNum;
    }


    /**
     * 获取全部群组
     */
    public static ConcurrentHashMap<String, Group> getGroupList() {
        return idGroupMap;
    }

    /**
     * 获取对应ID群组
     * @param id 群组id
     */
    public static Group getGroup(String id) {
        return idGroupMap.get(id);
    }

}
