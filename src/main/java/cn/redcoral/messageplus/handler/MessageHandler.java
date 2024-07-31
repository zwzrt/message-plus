package cn.redcoral.messageplus.handler;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.data.entity.vo.ChatRoom;
import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.data.entity.Message;
import cn.redcoral.messageplus.data.entity.MessageType;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.utils.ChatRoomManage;
import cn.redcoral.messageplus.utils.MessagePlusUtils;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  消息处理器
 * @author mo
 **/
@AllArgsConstructor
@Component
public class MessageHandler {

    private StringRedisTemplate stringRedisTemplate;
    private PublishService publishService;
    private MessagePlusBase messagePlusBase;
    private ChatRoomManage chatRoomManage;

    /**
     * 处理消息（自动判断类型分发）
     * @param message 消息类
     */
    public void handlerMessage(Message message) {
        switch (MessageType.valueOf(message.getType())) {
            case SINGLE_SHOT: {
                this.handleSingleMessage(message.getSenderId(), message.getReceiverId(), message);
                break;
            }
            case MASS_SHOT: {
                this.handleMassMessage(message.getSenderId(), message.getGroupId(), message);
                break;
            }
            case SYSTEM_SHOT: {
                messagePlusBase.onMessageBySystem(message.getSenderId(),message.getData().toString());
                break;
            }
            case CHAT_ROOM_SHOT: {
                this.handleChatRoomMessage(message.getSenderId(), message.getChatRoomId(), message);
            }
        }
    }

    /**
     * 处理单发消息
     * @param message 消息类
     */
    public void handleSingleMessage(String senderId, String receiverId, Message message) {
        // 查看用户是否在线
        String onLineTag = MessagePlusUtils.isOnLine(receiverId);
        switch (onLineTag) {
            // 不在线
            case "-1": {
                // 做持久化存储
                stringRedisTemplate.opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX+ receiverId, JSON.toJSONString(message));
                break;
            }
            // 本地在线
            case "0": {
                // 调用发送方法
                MessagePlusUtils.sendMessage(receiverId, message);
                break;
            }
            // 其它服务器在线
            default: {
                // 确保开启持久化以及消息持久化，并且消息发送失败
                if (MessagePlusProperties.persistence && MessagePersistenceProperties.messagePersistence) {
                    // 存储消息到对方会话的数组中
                    stringRedisTemplate.opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX + receiverId, JSON.toJSONString(message));
                    // 提示指定服务端该用户有新消息
                    publishService.publishByServiceId(onLineTag, receiverId);
                }
            }
        }
    }

    /**
     * 处理群发消息
     * @param message 消息类
     */
    public void handleMassMessage(String senderId, String groupId, Message message) {
        // 调用开发者实现的群发接口
        Group group = MessagePlusUtils.getGroupById(groupId);
        for (String receiverId : group.getClientIdList()) {
            // 查看用户是否在线
            String onLineTag = MessagePlusUtils.isOnLine(receiverId);
            switch (onLineTag) {
                // 不在线
                case "-1": {
                    // 做持久化存储
                    stringRedisTemplate.opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX+receiverId, JSON.toJSONString(message));
                    break;
                }
                // 本地在线
                case "0": {
                    // 调用接收方法
                    MessagePlusUtils.sendMessageToGroupBarringMe(senderId, groupId, message);
                    break;
                }
                // 其它服务器在线
                default: {
                    // 确保开启持久化以及消息持久化，并且消息发送失败
                    if (MessagePlusProperties.persistence && MessagePersistenceProperties.messagePersistence) {
                        // 存储消息到对方会话的数组中
                        stringRedisTemplate.opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX + receiverId, JSON.toJSONString(message));
                        // 提示指定服务端该用户有新消息
                        publishService.publishByServiceId(onLineTag, receiverId);
                    }
                }
            }
        }
    }

    /**
     * 处理聊天室消息
     * @param message 消息类
     */
    public void handleChatRoomMessage(String senderId, String chatRoomId, Message message) {
        // 1.调用开发者实现的群发接口
        ChatRoom chatRoom = chatRoomManage.getChatRoomById(chatRoomId);
        // 2.发送消息
        List<String> offLineClientIdList = MessagePlusUtils.sendMessageToChatRoomBarringMe(senderId, chatRoomId, message);
        // 3.未发送成功，查询对应服务ID，通知对应服务进行再次发送
        for (String receiverId : offLineClientIdList) {
            // 查看用户对应的服务ID
            String serviceId = MessagePlusUtils.getUserServiceId(receiverId);
            // 确保开启持久化以及消息持久化，并且消息发送失败
            if (MessagePlusProperties.persistence && MessagePersistenceProperties.messagePersistence) {
                // 存储消息到对方会话的数组中
                stringRedisTemplate.opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX + receiverId, JSON.toJSONString(message));
                // 提示指定服务端该用户有新消息
                publishService.publishByServiceId(serviceId, receiverId);
            }
        }
    }

}
