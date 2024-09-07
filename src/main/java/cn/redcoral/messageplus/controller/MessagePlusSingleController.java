package cn.redcoral.messageplus.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.handler.MessageHandler;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.utils.CounterIdentifierUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 单发
 * @author mo
 **/
@Slf4j
@RestController
@RequestMapping("/messageplus/single")
public class MessagePlusSingleController {

    @Autowired
    private MessagePlusBase messagePlusBase;
    @Autowired
    private MessageHandler messageHandler;

    /**
     * 发送单发类消息
     * @param receiverId 用户ID
     * @param msg 消息体
     */
    @PostMapping("/send")
    public void sendSingleMessage(HttpServerRequest request, @RequestParam("id1") String senderId, @RequestParam("id2") String receiverId, @RequestBody Object msg) throws Exception {
//        log.info("接收到消息"+msg);
        // 1.并发限流
        // 短时间发送消息达到上限，禁止发送消息(默认为1)
        if (CounterIdentifierUtil.isLessThanOrEqual(senderId, MessagePersistenceProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(senderId);

        // TODO 2.查询是否禁言

        // 3.发送消息
        Message message = Message.buildSingle(senderId, receiverId, msg);
        messageHandler.handleSingleMessage(senderId, receiverId, message);

        // 计数器减一
        CounterIdentifierUtil.numberOfSendsDecrease(senderId);
    }

}
