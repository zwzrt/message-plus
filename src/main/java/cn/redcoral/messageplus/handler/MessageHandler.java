package cn.redcoral.messageplus.handler;

import cn.redcoral.messageplus.data.entity.ChatRoom;
import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.data.entity.message.MessageType;
import cn.redcoral.messageplus.manage.ChatRoomManage;
import cn.redcoral.messageplus.manage.MessagePlusUtils;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.utils.cache.ChatSingleCacheUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  消息处理器
 * @author mo
 **/
@AllArgsConstructor
@Component
@Slf4j
public class MessageHandler {

    private MessagePlusBase messagePlusBase;
    private ChatRoomManage chatRoomManage;
    
    
    @Autowired
    private ChatSingleCacheUtil chatSingleCacheUtil;

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
                // 提示出现失败消息(用户实现)
                log.info("用户不在线");
                messagePlusBase.onFailedMessage(message);
                new Thread(()->{
                    chatSingleCacheUtil.addChatSingleContent(senderId,receiverId,message);
                }).start();
                break;
            }
            // 本地在线
            case "0": {
                // 调用发送方法
                boolean sended = MessagePlusUtils.sendMessage(receiverId, message);
                log.info("用户在线");
                if(!sended){
                    messagePlusBase.onFailedMessage(message);
                    new Thread(()->{
                        chatSingleCacheUtil.addChatSingleContent(senderId,receiverId,message);
                    }).start();
                }
                break;
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
                    // 提示出现失败消息
                    messagePlusBase.onFailedMessage(message);
                    // TODO 缓存
                    break;
                }
                // 本地在线
                case "0": {
                    // 调用接收方法
                    List<String> list = MessagePlusUtils.sendMessageToGroupBarringMe(senderId, groupId, message);
                    // TODO 失败用户存储等待重发
                    break;
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
            // 提示出现失败消息
            messagePlusBase.onFailedMessage(message);
        }
    }

}
