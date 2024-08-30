package cn.redcoral.messageplus.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MessagePlus配置类
 * @author mo
 **/
@Data
@Component("messagePlusClusterProperties")
@ConfigurationProperties(prefix = "messageplus.cluster")
public class MessagePlusClusterProperties {
    /**
     * 是否开启持久化
     */
    public static boolean open = false;


    public static boolean isOpen() {
        return MessagePlusClusterProperties.open;
    }
    public void setOpen(boolean persistence) {
        MessagePlusClusterProperties.open = persistence;
    }

}
