package cn.redcoral.messageplus.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MessagePlus聊天室配置类
 * @author mo
 **/
@Data
@Component("messagePlusChatRoomProperties")
@ConfigurationProperties(prefix = "messageplus.chatroom")
public class MessagePlusChatRoomProperties {
}
