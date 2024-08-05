package cn.redcoral.messageplus;

import cn.redcoral.messageplus.config.MessagePlusConfig;
import cn.redcoral.messageplus.config.WebConfig;
import cn.redcoral.messageplus.config.WebSocketConfig;
import cn.redcoral.messageplus.handler.MessageHandler;
import cn.redcoral.messageplus.manage.GroupManage;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.handler.MessagePlusService;
import cn.redcoral.messageplus.exteriorUtils.SpringUtils;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 消息增强插件启动注解
 * @author mo
 **/
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({
        MessagePlusConfig.class,
        GroupManage.class, SpringUtils.class,
        MessagePlusService.class, MessagePlusProperties.class, MessagePersistenceProperties.class,
        WebConfig.class,
        MessageHandler.class,
        WebSocketConfig.class
})
public @interface EnableMessagePlus {
}
