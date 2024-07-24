package cn.redcoral.messageplus.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.entity.Group;
import cn.redcoral.messageplus.entity.Message;
import cn.redcoral.messageplus.entity.MessageType;
import cn.redcoral.messageplus.handler.MessageHandler;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.handler.PublishService;
import cn.redcoral.messageplus.utils.BeanUtil;
import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.utils.CounterIdentifierUtil;
import cn.redcoral.messageplus.utils.MessagePlusUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 发送消息
 * @author mo
 **/
@RestController
@RequestMapping("/messageplus")
public class MessagePlusController {

    @Autowired
    private MessagePlusBase messagePlusBase;
    @Autowired
    private MessageHandler messageHandler;

    /**
     * 发送单发类消息
     * @param receiverId 用户ID
     * @param msg 消息体
     */
    @PostMapping("/send/single")
    public void sendSingleMessage(HttpServerRequest request, @RequestParam("id1") String senderId, @RequestParam("id2") String receiverId, @RequestBody String msg) {
        // 1.并发限流
        // 短时间发送消息达到上限，禁止发送消息
        if (CounterIdentifierUtil.isLessThanOrEqual(senderId, MessagePersistenceProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(senderId);

        // 2.查询是否禁言

        // 3.权限校验
        Message message = Message.buildSingle(senderId, receiverId, msg);
        // 进行权限校验
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
    public void sendMassMessage(HttpServerRequest request, @RequestParam("id1") String senderId, @RequestParam("id2") String groupId, @RequestBody Object msg) {
        // 1.并发限流
        // 短时间发送消息达到上限，禁止发送消息
        if (CounterIdentifierUtil.isLessThanOrEqual(senderId, MessagePersistenceProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(senderId);

        // 2.查询是否禁言

        // 3.权限校验
        Message message = Message.buildMass(senderId, groupId, msg);
        // 进行权限校验
        boolean bo = messagePlusBase.onMessageCheck(request, message);
        // 权限校验不通过
        if (!bo) return;

        // 4.发送消息
        messageHandler.handleMassMessage(senderId, groupId, message);

        // 计数器减一
        CounterIdentifierUtil.numberOfSendsDecrease(senderId);
    }

    /**
     * 发送系统类消息
     * @param msg 消息体
     */
    @PostMapping("/send/system")
    public void sendSystemMessage(HttpServerRequest request, @RequestParam("id1") String senderId, @RequestBody Object msg) {
        // 1.并发限流
        // 短时间发送消息达到上限，禁止发送消息
        if (CounterIdentifierUtil.isLessThanOrEqual(senderId, MessagePersistenceProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(senderId);

        // 2.查询是否禁言

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

}
