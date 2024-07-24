package cn.redcoral.messageplus.handler;

import cn.redcoral.messageplus.config.RedisListenerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 用于通知其它服务器
 * @author mo
 **/
@Service
public class PublishService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void publish(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }
    /**
     * 广播所有服务器
     * @param message 消息
     */
    public void publish(String message) {
        stringRedisTemplate.convertAndSend(RedisListenerConfig.REDIS_TOPIC, message);
    }
    /**
     * 发布到指定服务器
     * @param serviceId 服务器ID
     * @param hasNewMsgUserId 有新消息的用户ID
     */
    public void publishByServiceId(String serviceId, String hasNewMsgUserId) {
        stringRedisTemplate.convertAndSend(RedisListenerConfig.REDIS_TOPIC+":"+serviceId, hasNewMsgUserId);
    }
}
