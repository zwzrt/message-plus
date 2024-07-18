package cn.redcoral.messageplus.controller;

import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.utils.BeanUtil;
import cn.redcoral.messageplus.utils.CachePrefixConstant;
import cn.redcoral.messageplus.utils.MessagePlusUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.redcoral.messageplus.utils.BeanUtil.publishService;
import static cn.redcoral.messageplus.utils.BeanUtil.stringRedisTemplate;

/**
 * @author mo
 **/
@RestController
@RequestMapping("/messageplus")
public class MessagePlusController {

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
     * @param userId 用户ID
     * @param msg 消息体
     */
    @PostMapping("/send/single")
    public void sendSingleMessage(@RequestParam("id") String userId, @RequestBody Object msg) {
        // 调用开发者实现的单发接口
        boolean bo = BeanUtil.messagePlusBase().onMessageBySingle(userId, msg);
        // 确保开启持久化以及消息持久化，并且消息发送失败
        if (MessagePlusProperties.persistence && MessagePlusProperties.messagePersistence && !bo) {
            // 存储消息到对方会话的数组中
            stringRedisTemplate().opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX+"SINGLE:"+ userId, JSON.toJSONString(msg));
            // 提示系统该用户有新消息
            publishService().publish(userId);
        }
    }

    /**
     * 发送群发类消息
     * @param groupId 群组ID
     * @param msg 消息体
     */
    @PostMapping("/send/mass")
    public void sendMassMessage(@RequestParam String groupId, @RequestBody Object msg) {
        // 调用开发者实现的群发接口
        List<String> offLineClientIdList = BeanUtil.messagePlusBase().onMessageByMass(groupId, msg);
        // 确保开启持久化以及消息持久化，并且消息发送失败
        if (MessagePlusProperties.persistence && MessagePlusProperties.messagePersistence && !offLineClientIdList.isEmpty()) {// 群发
            // 存储到群组中每个成员的消息队列
            for (String cid : offLineClientIdList) {
                // 存储消息到对方会话的数组中
                stringRedisTemplate().opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX+"MASS:"+cid, JSON.toJSONString(msg));
                // 提示系统该用户有新消息
                publishService().publish(cid);
            }
        }
    }

    /**
     * 发送系统类消息
     * @param msg 消息体
     */
    @PostMapping("/send/system")
    public void sendSystemMessage(@RequestBody Object msg) {
        boolean bo = BeanUtil.messagePlusBase().onMessageBySystem(msg);
        // 确保开启持久化以及消息持久化，并且消息发送失败
        if (MessagePlusProperties.persistence && MessagePlusProperties.messagePersistence && !bo) {
            // 存储消息到对方会话的数组中
            stringRedisTemplate().opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX+":SYSTEM", JSON.toJSONString(msg));
        }
    }

}
