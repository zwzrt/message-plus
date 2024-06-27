package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.initialize.MessagePersistenceInitialize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * message-plus的持久化配置类
 * @author mo
 * @日期: 2024-06-07 15:48
 **/
@Slf4j
@Component
@ConditionalOnProperty(name = "messageplus.persistence", havingValue = "true")
@ConditionalOnClass({RedisTemplate.class})
@Import({MessagePersistenceInitialize.class})
public class MessagePersistenceConfig {
    static {
        log.info("Persistence function of message enhancer has been turned on...");
    }
}
