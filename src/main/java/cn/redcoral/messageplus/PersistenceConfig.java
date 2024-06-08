package cn.redcoral.messageplus;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 持久化配置类
 * @author mo
 * @日期: 2024-06-07 15:48
 **/
@Component
@ConditionalOnClass({RedisTemplate.class})
//@ConditionalOnClass(name = "org.springframework.data.redis.RedisSystemException")
public class PersistenceConfig {
    static {
        System.out.println("持久化配置类静态加载...");
    }
}
