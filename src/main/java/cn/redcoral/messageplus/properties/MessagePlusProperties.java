package cn.redcoral.messageplus.properties;

import cn.hutool.core.lang.UUID;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MessagePlus配置类
 * @author mo
 **/
@Data
@Component("messagePlusProperties")
@ConfigurationProperties(prefix = "messageplus")
public class MessagePlusProperties {
    /**
     * 服务ID
     */
    public static String serviceId = UUID.randomUUID().toString(true);
    /**
     * 路径参数名称
     */
    public final static String pathParamName = "sid";
    /**
     * 自动发送消息
     */
    public static boolean autoSend = false;

    public static String getServiceId() {
        return MessagePlusProperties.serviceId;
    }
    public void setServiceId(String serviceId) {
        MessagePlusProperties.serviceId = serviceId;
    }
    public static boolean isAutoSend() {
        return MessagePlusProperties.autoSend;
    }
    public void setAutoSend(boolean autoSend) {
        MessagePlusProperties.autoSend = autoSend;
    }
}
