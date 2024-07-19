package cn.redcoral.messageplus.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author mo
 **/
@Data
@Component("messagePlusProperties")
@ConfigurationProperties(prefix = "messageplus.message")
public class MessagePersistenceProperties {
    /**
     * 消息持久化
     */
    public static boolean messagePersistence = true;
    /**
     * 消息过期时间（默认-1永不过期）
     */
    public static int expirationTime = -1;

    public static boolean isMessagePersistence() {
        return MessagePersistenceProperties.messagePersistence;
    }
    public void setMessagePersistence(boolean messagePersistence) {
        MessagePersistenceProperties.messagePersistence = messagePersistence;
    }
}
