package cn.redcoral.messageplus.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.handler.MessageHandler;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.utils.CounterIdentifierUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 发送消息
 * @author mo
 **/
@RestController
@RequestMapping("/messageplus")
@Slf4j
public class MessagePlusSendController {

    @Autowired
    private MessagePlusBase messagePlusBase;
    @Autowired
    private MessageHandler messageHandler;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    /**
     * 发送单发类消息
     * @param receiverId 用户ID
     * @param msg 消息体
     */
    @PostMapping("/send/single")
    public void sendSingleMessage(HttpServerRequest request, @RequestParam("id1") String senderId, @RequestParam("id2") String receiverId, @RequestBody String msg) throws Exception {
        log.info("接收到消息"+msg);
        // 1.并发限流
        // 短时间发送消息达到上限，禁止发送消息(默认为1)
        if (CounterIdentifierUtil.isLessThanOrEqual(senderId, MessagePersistenceProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(senderId);

        // TODO 2.查询是否禁言

        // 3.权限校验
        Message message = Message.buildSingle(senderId, receiverId, msg);
        // 进行权限校验(用户自己实现)
        boolean bo = messagePlusBase.onMessageCheck(request, message);
        // 权限校验不通过
        if (!bo) return;

        // 4.发送消息
        messageHandler.handleSingleMessage(senderId, receiverId, message);

        // 计数器减一
        CounterIdentifierUtil.numberOfSendsDecrease(senderId);
    }

    /**
     * 发送群发类消息
     * @param groupId 群组ID
     * @param msg 消息体
     */
    @PostMapping("/send/mass")
    public void sendMassMessage(HttpServerRequest request, @RequestParam("id1") String senderId,
                                @RequestParam("id2") String groupId, @RequestBody String msg) throws Exception {
        // 1.并发限流
        // 短时间发送消息达到上限，禁止发送消息
        if (CounterIdentifierUtil.isLessThanOrEqual(senderId, MessagePersistenceProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(senderId);

        // TODO 2.查询是否禁言

        // 3.权限校验
        Message message = Message.buildMass(senderId, groupId, msg);
        // 进行权限校验
        boolean bo = messagePlusBase.onMessageCheck(request, message);
        // 权限校验不通过
        if (!bo) return;

        // 4.发送群发消息
        messageHandler.handleMassMessage(senderId, groupId, message);

        // 计数器减一
        CounterIdentifierUtil.numberOfSendsDecrease(senderId);
    }

    /**
     * 发送聊天室消息
     * @param senderId 发送者ID
     * @param chatRoomId 聊天室ID
     * @param msg 消息体
     */
    @PostMapping("/send/chatroom")
    public void sendChatRoomMessage(HttpServerRequest request, @RequestParam("id1") String senderId, @RequestParam("id2") String chatRoomId, @RequestBody Object msg) throws Exception {
        // 1.并发限流
        // 短时间发送消息达到上限，禁止发送消息
        if (CounterIdentifierUtil.isLessThanOrEqual(senderId, MessagePersistenceProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(senderId);

        // TODO 2.查询是否禁言

        // 3.权限校验
        Message message = Message.buildChatRoom(senderId, chatRoomId, msg);
        // 进行权限校验
        boolean bo = messagePlusBase.onMessageCheck(request, message);
        // 权限校验不通过
        if (!bo) return;

        // 4.发送消息
        // 广播
        simpMessagingTemplate.convertAndSend("/topic/chat/"+chatRoomId, message);

        // 计数器减一
        CounterIdentifierUtil.numberOfSendsDecrease(senderId);
    }

    /**
     * 发送系统类消息
     * @param msg 消息体
     */
    @PostMapping("/send/system")
    public void sendSystemMessage(HttpServerRequest request, @RequestParam("id1") String senderId, @RequestBody Object msg) throws Exception {
        // 1.并发限流
        // 短时间发送消息达到上限，禁止发送消息
        if (CounterIdentifierUtil.isLessThanOrEqual(senderId, MessagePersistenceProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(senderId);

        // TODO 2.查询是否禁言

        // 3.权限校验
        Message message = Message.buildSystem(senderId, msg);
        // 进行权限校验
        boolean bo = messagePlusBase.onMessageCheck(request, message);
        // 权限校验不通过
        if (!bo) return;

        // 4.发送消息
        messagePlusBase.onMessageBySystem(senderId, message.getData().toString());

        // 计数器减一
        CounterIdentifierUtil.numberOfSendsDecrease(senderId);
    }
    
    /**
     * 当浏览器向服务端发送消息会走这个方法，然后将返回值广播到订阅了/messageplus/getResponse的浏览器实现群聊的功能
     * @param message
     * @return
     */
    @MessageMapping("/hello") // @MessageMapping 和 @RequestMapping 功能类似，浏览器向服务器发起消息，映射到该地址。
    @SendTo("/cn/redcoral/messageplus/getResponse") // 如果服务器接受到了消息，就会对订阅了 @SendTo 括号中的地址的浏览器发送消息。
    public String say(String message) {
        return message;
    }

}
