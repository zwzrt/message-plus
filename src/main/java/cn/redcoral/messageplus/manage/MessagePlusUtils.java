package cn.redcoral.messageplus.manage;

import cn.redcoral.messageplus.entity.ChatRoom;
import cn.redcoral.messageplus.entity.Group;
import cn.redcoral.messageplus.entity.message.Message;
import cn.redcoral.messageplus.exteriorUtils.SpringUtils;
import cn.redcoral.messageplus.handler.MessagePlusService;
import cn.redcoral.messageplus.utils.BeanUtil;

import javax.websocket.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 消息增强器工具类
 * @author mo
 **/
public class MessagePlusUtils {

    /**
     * 群组管理
     **/
    private static GroupManage groupManage;

    protected static GroupManage getGroupManage() {
        if (groupManage == null) groupManage = SpringUtils.getBean(GroupManage.class);
        return groupManage;
    }

    /**
     * 对应响应类Map
     */
    public static ConcurrentHashMap<String, MessagePlusService> userIdBaseMap = new ConcurrentHashMap<>();
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
    private static Long onLinePeopleNum = 0L;

    /**
     * 加入聊天
     * @param id 用户ID
     * @param session 用户对应session
     * @return 返回全部在线人数
     */
    public static Long joinChat(String id, MessagePlusService base, Session session) {
        // 为空则多个在线人数，在线人数加一；若不为空，则为顶替，无需加一
        if (userIdSessionMap.get(id)==null) {
            userNumLock.lock();
            onLinePeopleNum++;
            userNumLock.unlock();
        }
        userIdBaseMap.put(id, base);
        // 录入session库
        userIdSessionMap.put(id, session);
        return onLinePeopleNum;
    }
    /**
     * 退出聊天
     * @param id 用户ID
     * @return 返回全部在线人数
     */
    public static Long quitChat(String id) {
        // 为空则多个在线人数，在线人数加一；若不为空，则为顶替，无需加一
        if (userIdSessionMap.get(id)!=null) {
            userNumLock.lock();
            onLinePeopleNum--;
            userNumLock.unlock();
        }
        userIdBaseMap.remove(id);
        userIdSessionMap.remove(id);
        return onLinePeopleNum;
    }


    /**
     * 给指定用户发送消息
     */
    public static boolean sendMessage(String id, Message msg) {
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
     * @param groupId 群组ID
     * @param message 消息内容
     * @return 失败用户ID
     */
    public static List<String> sendMessageToGroup(String groupId, Message message) {
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
     * @param userId 用户ID
     * @param groupId 群组ID
     * @param message 消息内容
     * @return 失败用户ID
     */
    public static List<String> sendMessageToGroupBarringMe(String userId, String groupId, Message message) {
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
     * 聊天室群发（不包括自己）
     * @param userId 用户ID
     * @param chatRoomId 聊天室ID
     * @param message 消息内容
     * @return 失败用户ID
     */
    public static List<String> sendMessageToChatRoomBarringMe(String userId, String chatRoomId, Message message) {
        ChatRoom chatRoom = BeanUtil.chatRoomManage().getChatRoomById(chatRoomId);
        if (chatRoom==null) return Collections.emptyList();

        List<String> offLineClientIdList = new ArrayList<>();
        chatRoom.getClientIdList().forEach(clientId->{
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
    public static void sendMessage(Session session, String message) {
        try {
            synchronized (session) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 服务端发送消息
     */
    public static void sendMessage(Session session, Message message) {
        try {
            synchronized (session) {
                session.getBasicRemote().sendObject(message);
            }
        } catch (EncodeException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取总在线人数
     */
    public static Long getOnLinePeopleNum() {
        return onLinePeopleNum;
    }
    /**
     * 获取对应ID群组
     */
    public static Group getGroupById(String groupId) {
        return getGroupManage().getGroupById(groupId);
    }

    /**
     * 获取指定ID的session
     */
    public static Session getSessionByClientId(String clientId) {
        return userIdSessionMap.get(clientId);
    }


    /**
     * 用户是否在线
     * @param userId 用户ID
     * @return "0"-本地在线 "-1"-不在线 "服务器ID"-其它服务器在线
     */
    public static String isOnLine(String userId) {
        //从会话map中获取对应userid的会话
        Session session = userIdSessionMap.get(userId);
        return session != null ? "0" : "-1";
    }

}
