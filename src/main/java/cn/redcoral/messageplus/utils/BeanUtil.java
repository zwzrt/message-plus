package cn.redcoral.messageplus.utils;

import cn.redcoral.messageplus.config.CacheConfig;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.service.PublishService;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 获取对象
 * @author mo
 **/
public class BeanUtil {
    private static MessagePlusProperties messagePlusProperties1;
    private static StringRedisTemplate stringRedisTemplate1;
    private static PublishService publishService1;

    public static MessagePlusProperties messagePlusProperties() {
        if (messagePlusProperties1 == null) messagePlusProperties1 = SpringUtils.getBean(MessagePlusProperties.class);
        return messagePlusProperties1;
    }
    public static StringRedisTemplate stringRedisTemplate() {
        if (stringRedisTemplate1 == null) stringRedisTemplate1 = SpringUtils.getBean(StringRedisTemplate.class);
        return stringRedisTemplate1;
    }
    public static PublishService publishService() {
        if (publishService1 == null) publishService1 = SpringUtils.getBean(PublishService.class);
        return publishService1;
    }
    public static Cache<String, Long> stringLongCache() {
        return CacheConfig.stringLongCache;
    }
}
