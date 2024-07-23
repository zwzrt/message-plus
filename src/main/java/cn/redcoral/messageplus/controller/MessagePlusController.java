package cn.redcoral.messageplus.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.entity.Group;
import cn.redcoral.messageplus.entity.Message;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.service.PublishService;
import cn.redcoral.messageplus.utils.BeanUtil;
import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.utils.CounterIdentifierUtil;
import cn.redcoral.messageplus.utils.MessagePlusUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 发送消息
 * @author mo
 **/
@RestController
@RequestMapping("/messageplus")
public class MessagePlusController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private MessagePlusBase messagePlusBase;
    @Autowired
    private PublishService publishService;


    /**
     * 获取当前在线人数
     * @return 在线人数
     */
    @GetMapping
    public String getOnLinePeopleNum() {
        return MessagePlusUtils.getOnLinePeopleNum().toString();
    }

    /**
     * 发送单发类消息
     * @param receiverId 用户ID
     * @param msg 消息体
     */
    @PostMapping("/send/single")
    public void sendSingleMessage(HttpServerRequest request, @RequestParam("id1") String myId, @RequestParam("id2") String receiverId, @RequestBody Object msg) {
        // 短时间发送消息达到上限，禁止发送消息
        if (CounterIdentifierUtil.isLessThanOrEqual(myId, MessagePersistenceProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(myId);
        // 进行权限校验
        boolean bo = messagePlusBase.onMessageCheck(request, myId);
        // 权限校验不通过
        if (!bo) return;
        // 查看用户是否在线
        String onLineTag = MessagePlusUtils.isOnLine(receiverId);
        switch (onLineTag) {
            // 不在线
            case "-1": {
                // 做持久化存储
                Message message = Message.buildSingle(myId, receiverId, msg);
                stringRedisTemplate.opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX+ receiverId, JSON.toJSONString(message));
                break;
            }
            // 本地在线
            case "0": {
                // 调用接收方法
                bo = messagePlusBase.onMessageByInboxAndSingle(myId, receiverId, msg);
                break;
            }
            // 其它服务器在线
            default: {
                // 确保开启持久化以及消息持久化，并且消息发送失败
                if (MessagePlusProperties.persistence && MessagePersistenceProperties.messagePersistence) {
                    Message message = Message.buildSingle(myId, receiverId, msg);
                    // 存储消息到对方会话的数组中
                    stringRedisTemplate.opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX + receiverId, JSON.toJSONString(message));
                    // 提示指定服务端该用户有新消息
                    publishService.publishByServiceId(onLineTag, receiverId);
                }
            }
        }
        CounterIdentifierUtil.numberOfSendsDecrease(myId);
    }

    /**
     * 发送群发类消息
     * @param groupId 群组ID
     * @param msg 消息体
     */
    @PostMapping("/send/mass")
    public void sendMassMessage(HttpServerRequest request, @RequestParam("id1") String senderId, @RequestParam("id2") String groupId, @RequestBody Object msg) {
        // 短时间发送消息达到上限，禁止发送消息
        if (CounterIdentifierUtil.isLessThanOrEqual(senderId, MessagePersistenceProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(senderId);
        // 进行权限校验
        boolean bo = BeanUtil.messagePlusBase().onMessageCheck(request, senderId);
        // 权限校验不通过
        if (!bo) return;
        // 调用开发者实现的群发接口
        Group group = MessagePlusUtils.getGroupById(groupId);
        for (String receiverId : group.getClientIdList()) {
            // 查看用户是否在线
            String onLineTag = MessagePlusUtils.isOnLine(receiverId);
            switch (onLineTag) {
                // 不在线
                case "-1": {
                    // 做持久化存储
                    Message message = Message.buildMass(senderId, groupId, receiverId, msg);
                    stringRedisTemplate.opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX+receiverId, JSON.toJSONString(message));
                    break;
                }
                // 本地在线
                case "0": {
                    // 调用接收方法
                    bo = messagePlusBase.onMessageByInboxAndByMass(senderId, groupId, receiverId, msg);
                    break;
                }
                // 其它服务器在线
                default: {
                    // 确保开启持久化以及消息持久化，并且消息发送失败
                    if (MessagePlusProperties.persistence && MessagePersistenceProperties.messagePersistence) {
                        Message message = Message.buildMass(senderId, groupId, receiverId, msg);
                        // 存储消息到对方会话的数组中
                        stringRedisTemplate.opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX + receiverId, JSON.toJSONString(message));
                        // 提示指定服务端该用户有新消息
                        publishService.publishByServiceId(onLineTag, receiverId);
                    }
                }
            }
        }
        CounterIdentifierUtil.numberOfSendsDecrease(senderId);
    }

    /**
     * 发送系统类消息
     * @param msg 消息体
     */
    @PostMapping("/send/system")
    public void sendSystemMessage(HttpServerRequest request, @RequestParam("id1") String myId, @RequestBody Object msg) {
        // 短时间发送消息达到上限，禁止发送消息
        if (CounterIdentifierUtil.isLessThanOrEqual(myId, MessagePersistenceProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(myId);
        // 进行权限校验
        boolean bo = BeanUtil.messagePlusBase().onMessageCheck(request, myId);
        // 权限校验不通过
        if (!bo) return;
        // 发送消息
        messagePlusBase.onMessageBySystem(myId, msg);
        CounterIdentifierUtil.numberOfSendsDecrease(myId);
    }

}
