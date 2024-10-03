package cn.redcoral.messageplus.properties;

import cn.hutool.core.lang.UUID;
import cn.redcoral.messageplus.data.dictionary.PropertiesDictionary;
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
     * Token过期时间（默认：0，单位：分钟）
     */
    public static int tokenExpirationTime = 0;



    static {
        // 加入字典
        PropertiesDictionary.put("serviceId", "服务ID");
        PropertiesDictionary.put("tokenExpirationTime", "Token过期时间");
    }



    public static String getServiceId() {
        return MessagePlusProperties.serviceId;
    }
    public void setServiceId(String serviceId) {
        MessagePlusProperties.serviceId = serviceId;
    }
    public static int getTokenExpirationTime() {
        return MessagePlusProperties.tokenExpirationTime;
    }
    public void setTokenExpirationTime(int tokenExpirationTime) {
        MessagePlusProperties.tokenExpirationTime = tokenExpirationTime;
    }

}
