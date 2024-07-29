package cn.redcoral.messageplus.utils;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.entity.ChatRoom;
import cn.redcoral.messageplus.entity.Group;
import cn.redcoral.messageplus.entity.Message;
import cn.redcoral.messageplus.entity.MessageType;
import cn.redcoral.messageplus.exteriorUtils.SpringUtils;
import cn.redcoral.messageplus.handler.MessageHandler;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.handler.MessagePlusService;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import com.alibaba.fastjson.JSON;

import javax.websocket.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static cn.redcoral.messageplus.utils.BeanUtil.*;

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
        // 在Redis中存储
        recordConnect(id, base, session);
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

    /**s
     * 记录连接（持久化）
     */
    protected static void recordConnect(String id, MessagePlusService base, Session session) {
        // 持久化标识
        boolean persistence = messagePlusProperties().isPersistence();
        // 消息持久化标识
        boolean messagePersistence = MessagePersistenceProperties.messagePersistence;
        // 服务ID
        String serviceId = messagePlusProperties().getServiceId();
        // 确保开启了持久化
        if (persistence) {
            // 记录该用户所在的服务ID
            stringRedisUtil().setUserService(id);
            // 开启了消息持久化
            if (messagePersistence) {
                // 判断有没有未接收到的消息并获取全部发送失败的消息
                List<String> msgs = new ArrayList<>();
                String msg = stringRedisTemplate().opsForList().rightPop(CachePrefixConstant.USER_MESSAGES_PREFIX+id);
                while (msg!=null) {
                    msgs.add(msg);
                    msg = stringRedisTemplate().opsForList().rightPop(CachePrefixConstant.USER_MESSAGES_PREFIX+id);
                }
                // 发送消息
                msgs.stream().filter(Objects::nonNull).map(s -> {
                    return JSON.parseObject(s, Message.class);
                }).forEach(m -> {
                    // 聊天室消息不发送
                    if (m.getType().equals(MessageType.CHAT_ROOM_SHOT.name())) return;
                    sendMessage(id, m);
//                    messageHandler().handlerMessage(m);
                });
            }
        }
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
        // 开启了持久化
        if (MessagePlusProperties.persistence) {
            // 从本地缓存获取
            Long num = BeanUtil.stringLongCache().get(CachePrefixConstant.ON_LINE_PEOPLE_NUM, k -> 0L);
            // 不存在，从Redis获取
            if (num == 0) {
                Set<String> keys = stringRedisTemplate().keys(CachePrefixConstant.USER_SERVICE_PREFIX.concat("*"));
                BeanUtil.stringLongCache().put(CachePrefixConstant.ON_LINE_PEOPLE_NUM, keys == null ? 0L : keys.size());
                return keys == null ? 0L : keys.size();
            } else {
                return num;
            }
        } else {
            return onLinePeopleNum;
        }
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
        Session session = userIdSessionMap.get(clientId);
        return session;
    }


    /**
     * 用户是否在线
     * @param userId 用户ID
     * @return "0"-本地在线 "-1"-不在线 "服务器ID"-其它服务器在线
     */
    public static String isOnLine(String userId) {
        Session session = userIdSessionMap.get(userId);
        if (session != null) return "0";
        String serviceId = BeanUtil.stringRedisUtil().getUserService(userId);
        return serviceId == null ? "-1" : serviceId;
    }
    /**
     * 查询用户的服务ID
     * @param userId 用户ID
     * @return 服务器ID
     */
    public static String getUserServiceId(String userId) {
        return BeanUtil.stringRedisUtil().getUserService(userId);
    }

    /**
     * 查询指定用户的未接收消息（该方法主要用于集群架构中跨服务使用，框架自己调用，开发者一般不需要）
     * @param userId 用户ID
     */
    public static List<Message> getNewMessage(String userId) {
        List<String> msgs = new ArrayList<>();
        // 查询全部消息
        String msg = stringRedisTemplate().opsForList().rightPop(CachePrefixConstant.USER_MESSAGES_PREFIX+userId);
        while (msg!=null) {
            msgs.add(msg);
            msg = stringRedisTemplate().opsForList().rightPop(CachePrefixConstant.USER_MESSAGES_PREFIX+userId);
        }
        // 转换并返回
        return msgs.stream()
                .filter(Objects::nonNull)
                .map(s -> JSON.parseObject(s, Message.class))
                .collect(Collectors.toList());
    }

    /**
     * 提示指定用户存在新消息（该方法主要用于集群架构中跨服务使用，框架自己调用，开发者一般不需要）
     * @param userId 用户ID
     */
    public static void hasNewMessage(String userId) {
        Session session = userIdSessionMap.get(userId);
        // 不在线，不进行新消息处理
        if (session==null) return;
        // 获取该用户的消息
        List<Message> newMessageList = getNewMessage(userId);
        if (newMessageList.isEmpty()) return;
        // 发送消息
        MessagePlusService base = userIdBaseMap.get(userId);
        for (Message m : newMessageList) {
            messageHandler().handlerMessage(m);
        }
    }
    /**
     * 提示指定聊天室存在新消息（该方法主要用于集群架构中跨服务使用，框架自己调用，开发者一般不需要）
     * @param message 聊天室消息
     */
    public static void hasNewMessageByChatRoom(Message message) {
        if (message == null) return;
        if (!message.getType().equals(MessageType.CHAT_ROOM_SHOT.name())) return;
        // 广播消息
        BeanUtil.simpMessagingTemplate().convertAndSend("/topic/chat/"+message.getChatRoomId(), message.getData());
    }

}
