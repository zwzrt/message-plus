package cn.redcoral.messageplus.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.utils.CounterIdentifierUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 发送消息
 * @author mo
 **/
@RestController
@RequestMapping("/messageplus/system")
@Slf4j
public class MessagePlusSystemController {

    @Autowired
    private MessagePlusBase messagePlusBase;

    /**
     * 发送系统类消息
     * @param msg 消息体
     */
    @PostMapping("/send")
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

}
