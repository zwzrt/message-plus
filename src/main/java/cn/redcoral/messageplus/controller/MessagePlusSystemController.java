package cn.redcoral.messageplus.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.manage.MessageManage;
import cn.redcoral.messageplus.manage.SystemManage;
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
@Slf4j
@RestController
@RequestMapping("/messageplus/system")
public class MessagePlusSystemController {

    @Autowired
    private MessagePlusBase messagePlusBase;
    @Autowired
    private SystemManage systemManage;



    /**
     * 登录接口
     * @param username 用户名
     * @param password 密码
     * @return token（为null或""时登录失败）
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        String token = messagePlusBase.login(username, password);
        if (token == null || token.isEmpty()) return null;
        systemManage.put(token, username);
        return token;
    }

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


        // 3.发送消息
        Message message = Message.buildSystem(senderId, msg);
        messagePlusBase.onMessageBySystem(senderId, message.getData().toString());

        // 计数器减一
        CounterIdentifierUtil.numberOfSendsDecrease(senderId);
    }




    /**
     * 登出接口
     * @param token 令牌
     */
    @DeleteMapping("/logout")
    public void logout(@RequestParam String token) {
        systemManage.remove(token);
    }




    /**
     * 是否登录
     * @param token 令牌
     * @return 是否
     */
    @GetMapping("/isOnLine")
    public String isOnline(@RequestParam String token) {
        return systemManage.get(token);
    }
    /**
     * 获取服务器总人数
     * @return 总人数
     */
    @GetMapping("/onLineNum")
    public String getOnLinePeopleNum() {
        return MessageManage.getOnLinePeopleNum().toString();
    }



}
