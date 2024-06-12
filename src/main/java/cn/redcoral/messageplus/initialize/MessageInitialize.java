package cn.redcoral.messageplus.initialize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * 初始化
 * @author mo
 * @日期: 2024-06-07 16:51
 **/
@Slf4j
@Configuration
public class MessageInitialize {
    static {
        log.info("MessagePlus is enabled...");
    }
}
