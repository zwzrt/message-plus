package cn.redcoral.messageplus.utils;

import cn.redcoral.messageplus.config.CacheConfig;
import cn.redcoral.messageplus.handler.MessageHandler;
import cn.redcoral.messageplus.manage.ChatRoomManage;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.exteriorUtils.SpringUtils;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * 获取对象
 * @author mo
 **/
public class BeanUtil {
    private static MessagePlusProperties messagePlusProperties1;
    private static MessagePlusBase messagePlusBase1;
    private static MessageHandler messageHandler1;
    private static ChatRoomManage chatRoomManage1;
    private static SimpMessagingTemplate simpMessagingTemplate1;

    public static MessagePlusProperties messagePlusProperties() {
        if (messagePlusProperties1 == null) messagePlusProperties1 = SpringUtils.getBean(MessagePlusProperties.class);
        return messagePlusProperties1;
    }
    public static Cache<String, Long> stringLongCache() {
        return CacheConfig.stringLongCache;
    }
    public static MessagePlusBase messagePlusBase() {
        if (messagePlusBase1 == null) messagePlusBase1 = SpringUtils.getBean(MessagePlusBase.class);
        return messagePlusBase1;
    }
    public static MessageHandler messageHandler() {
        if (messageHandler1 == null) messageHandler1 = SpringUtils.getBean(MessageHandler.class);
        return messageHandler1;
    }
    public static ChatRoomManage chatRoomManage() {
        if (chatRoomManage1 == null) chatRoomManage1 = SpringUtils.getBean(ChatRoomManage.class);
        return chatRoomManage1;
    }
    public static SimpMessagingTemplate simpMessagingTemplate() {
        if (simpMessagingTemplate1 == null) simpMessagingTemplate1 = SpringUtils.getBean(SimpMessagingTemplate.class);
        return simpMessagingTemplate1;
    }
}
