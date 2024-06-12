package cn.redcoral.messageplus.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MessagePlus配置类
 * @author mo
 * @日期: 2024-06-11 13:52
 **/
@Data
@Component("messagePlusProperties")
@ConfigurationProperties(prefix = "messageplus")
public class MessagePlusProperties {
    /**
     * 是否开启持久化
     */
    private boolean persistence = false;

    public boolean isPersistence() {
        return persistence;
    }

    public void setPersistence(boolean persistence) {
        this.persistence = persistence;
    }
}
