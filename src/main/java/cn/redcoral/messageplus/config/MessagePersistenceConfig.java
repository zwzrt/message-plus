package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.initialize.MessagePersistenceInitialize;
import cn.redcoral.messageplus.utils.cache.UserRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * message-plus的持久化配置类
 * @author mo
 **/
@Slf4j
@Component
@ConditionalOnProperty(name = "messageplus.persistence", havingValue = "true")
@ComponentScan({
        "cn.redcoral.messageplus.config.cluster"
})
@Import({MessagePersistenceInitialize.class, CacheConfig.class})
public class MessagePersistenceConfig {
    static {
        log.info("Persistence function of message enhancer has been turned on...");
    }
}
