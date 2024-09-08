package cn.redcoral.messageplus.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.handler.MessageHandler;
import cn.redcoral.messageplus.manage.UserManage;
import cn.redcoral.messageplus.port.MessagePlusUtil;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
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
    private MessageHandler messageHandler;



    /**
     * 发送单发类消息
     * @param receiverId 用户ID
     * @param msg 消息体
     */
    @PostMapping("/send")
    public int sendSingleMessage(@RequestHeader(value = "token", required = false) String token,
                                  @RequestParam("id1") String senderId, @RequestParam("id2") String receiverId, @RequestBody Object msg) throws Exception {
        // 1.并发限流
        // 短时间发送消息达到上限，禁止发送消息(默认为1)
        if (CounterIdentifierUtil.isLessThanOrEqual(senderId, MessagePersistenceProperties.concurrentNumber)) {
            return 4000;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(senderId);
        // 保证如何情况下都会执行指定代码块
        try {
            // 2.查询是否被拉黑
            boolean whetherPulledBlack = UserManage.whetherPulledBlack(senderId, receiverId);
            // 被拉黑了
            if (whetherPulledBlack) return 4001;

            // 3.发送消息
            Message message = Message.buildSingle(senderId, receiverId, msg);
            messageHandler.handleSingleMessage(senderId, receiverId, message);

            return 2000;
        } finally {
            // 计数器减一
            CounterIdentifierUtil.numberOfSendsDecrease(senderId);
        }
    }

    /**
     * 拉黑
     * @param token 令牌
     * @param id2 拉黑用户ID
     */
    @PostMapping("/black")
    public boolean black(@RequestHeader(value = "token", required = false) String token,
                         @RequestParam String id1, @RequestParam String id2) {
        // 开启了权限校验
        if (MessagePlusProperties.tokenExpirationTime!=0) {
            String idByToken = MessagePlusUtil.getIdByToken(token);
            // 不在线，或服务器存储的ID与请求ID不相同
            if (idByToken == null || idByToken.equals(id1)) return false;
            else return UserManage.black(idByToken, id2);
        }
        // 未开启权限校验
        else {
            return UserManage.black(id1, id2);
        }
    }

    /**
     * 取消拉黑
     * @param token 令牌
     * @param id2 取消拉黑用户ID
     */
    @PostMapping("/noBlack")
    public boolean noBlack(@RequestHeader(value = "token", required = false) String token,
                           @RequestParam String id1, @RequestParam String id2) {
        // 开启了权限校验
        if (MessagePlusProperties.tokenExpirationTime!=0) {
            String idByToken = MessagePlusUtil.getIdByToken(token);
            // 不在线，或服务器存储的ID与请求ID不相同
            if (idByToken == null || idByToken.equals(id1)) return false;
            else return UserManage.noBlack(idByToken, id2);
        }
        // 未开启权限校验
        else {
            return UserManage.noBlack(id1, id2);
        }
    }

}
