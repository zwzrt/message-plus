package cn.redcoral.messageplus.service;

import cn.redcoral.messageplus.config.CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author mo
 * @日期: 2024-07-15 16:16
 **/
@Service
public class PublishService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void publish(String message) {
        stringRedisTemplate.convertAndSend(CacheConfig.REDIS_TOPIC, message);
    }
    public void publish(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }
}
