package cn.redcoral.messageplus.properties;

import cn.redcoral.messageplus.data.dictionary.PropertiesDictionary;
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
    /**
     * 消息存活时间（单位：s）
     */
    public static int survivalTime = 120;
    /**
     * 消息临时存储最大条数
     */
    public static int messageMaxSize = 20;


    static {
        // 加入字典
        PropertiesDictionary.putChatroom("survivalTime", "消息存活时间");
        PropertiesDictionary.putChatroom("messageMaxSize", "消息临时存储最大条数");
    }


    public static int getSurvivalTime() {
        return survivalTime;
    }
    public void setSurvivalTime(int survivalTime) {
        MessagePlusChatRoomProperties.survivalTime = survivalTime;
    }
    public static int getMessageMaxSize() {
        return messageMaxSize;
    }
    public void setMessageMaxSize(int messageMaxSize) {
        MessagePlusChatRoomProperties.messageMaxSize = messageMaxSize;
    }
}
