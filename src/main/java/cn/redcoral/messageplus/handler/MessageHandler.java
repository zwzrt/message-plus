package cn.redcoral.messageplus.handler;

import cn.redcoral.messageplus.data.entity.ChatRoom;
import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.data.entity.message.MessageType;
import cn.redcoral.messageplus.data.entity.po.HistoryMessagePo;
import cn.redcoral.messageplus.data.service.HistoryMessageService;
import cn.redcoral.messageplus.manage.ChatRoomManage;
import cn.redcoral.messageplus.manage.UserManage;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.utils.BeanUtil;
import cn.redcoral.messageplus.utils.RetryUtil;
import cn.redcoral.messageplus.utils.cache.CacheUtil;
import cn.redcoral.messageplus.utils.cache.ChatGroupCacheUtil;
import cn.redcoral.messageplus.utils.cache.ChatSingleCacheUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息处理器
 *
 * @author mo
 **/
@AllArgsConstructor
@Component
@Slf4j
public class MessageHandler {
    
    private MessagePlusBase messagePlusBase;
    
    private ChatRoomManage chatRoomManage;
    
    
    @Autowired
    private CacheUtil cacheUtil;
    
    @Autowired
    private MessagePersistenceProperties properties;
    
    @Autowired
    private HistoryMessageService historyMessageService;
    
    /**
     * 处理消息（自动判断类型分发）
     *
     * @param message 消息类
     */
    public void handlerMessage(Message message) {
        switch (MessageType.valueOf(message.getType()))
        {
            case SINGLE_SHOT:
            {
                this.handleSingleMessage(message.getSenderId(), message.getReceiverId(), message);
                break;
            }
            case MASS_SHOT:
            {
                this.handleMassMessage(message.getSenderId(), message.getGroupId(), message);
                break;
            }
            case SYSTEM_SHOT:
            {
                messagePlusBase.onMessageBySystem(message.getSenderId(), message.getData().toString());
                break;
            }
            case CHAT_ROOM_SHOT:
            {
                this.handleChatRoomMessage(message.getSenderId(), message.getChatRoomId(), message);
            }
        }
    }
    
    /**
     * 处理单发消息
     *
     * @param message 消息类
     */
    public boolean handleSingleMessage(String senderId, String receiverId, Message message) {
        // 查看用户是否在线
        String onLineTag = UserManage.isOnLine(receiverId);
        switch (onLineTag)
        {
            // 不在线
            case "-1":
            {
                // 提示出现失败消息(用户实现)
//                log.info("用户不在线");
                // TODO 提示出现失败消息
                //                messagePlusBase.onFailedMessage(message);
                //TODO 返回值改为布尔类型，提示开发者发送消息失败
                
                historyMessageService.insertMessage(message, true);
                //将消息缓存
                cacheUtil.addChatContent( receiverId,
                        new HistoryMessagePo(message,true));
                
                return false;
            }
            // 本地在线
            case "0":
            {
                // 调用发送方法
                boolean sended = UserManage.sendMessage(receiverId, message);
//                log.info("用户在线");
                if (!sended)
                {
                    // TODO 提示出现失败消息
                    //                    messagePlusBase.onFailedMessage(message);
                    
                    
                    historyMessageService.insertMessage(message, true);
                    //将消息缓存
                    cacheUtil.addChatContent( receiverId,
                            new HistoryMessagePo(message,true));
                    return false;
                }
                //成功发送
//                historyMessageService.insertMessage(message, false);
                return true;
            }
            default:return false;
        }
    }
    
    
    /**
     * 处理群发消息
     *
     * @param message 消息类
     */
    public void handleMassMessage(String senderId, String groupId, Message message) {
        // 调用开发者实现的群发接口
        Group group = UserManage.getGroupById(groupId);

        // 调用接收方法
        List<String> faildList = UserManage.sendMessageToGroupBarringMe(senderId, groupId, message);
        // 提示出现失败消息
        ChatGroupCacheUtil chatGroupCacheUtil = BeanUtil.chatGroupCacheUtil();
        //有元素表名有用户不在线没收到消息
        List<String> allIdList = group.getClientIdList();
        if (!faildList.isEmpty())
        {
            //用户不在线，有需要缓存的数据
            for (String receiverId : faildList)
            {
                historyMessageService.insertMessage(message, true);
                //将消息缓存
                cacheUtil.addChatContent( receiverId,
                        new HistoryMessagePo(message,true));
            }
            allIdList.removeAll(faildList);
            allIdList.remove(senderId);
            for (String receiverId : allIdList)
            {
                Message messageCopy = cn.hutool.core.bean.BeanUtil.copyProperties(message, Message.class);
                messageCopy.setReceiverId(receiverId);
                historyMessageService.insertMessage(messageCopy, false);
            }
        }
        //全部收到了
        else
        {
            for (String receiverId : allIdList)
            {
                message.setReceiverId(receiverId);
                historyMessageService.insertMessage(message, false);
            }
        }
    }
    
    /**
     * 处理聊天室消息
     *
     * @param message 消息类
     */
    public void handleChatRoomMessage(String senderId, String chatRoomId, Message message) {
        // 1.调用开发者实现的群发接口
        ChatRoom chatRoom = chatRoomManage.getChatRoomById(chatRoomId);
        // 2.发送消息
        List<String> offLineClientIdList = UserManage.sendMessageToChatRoomBarringMe(senderId, chatRoomId, message);
        // 3.未发送成功，查询对应服务ID，通知对应服务进行再次发送
        for (String receiverId : offLineClientIdList)
        {
            // TODO 提示出现失败消息
        }
    }
    
}
