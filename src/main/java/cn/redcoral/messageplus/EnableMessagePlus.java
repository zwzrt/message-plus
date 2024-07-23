package cn.redcoral.messageplus;

import cn.redcoral.messageplus.config.MessagePersistenceConfig;
import cn.redcoral.messageplus.config.MessagePlusConfig;
import cn.redcoral.messageplus.config.WebConfig;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.service.MessagePlusService;
import cn.redcoral.messageplus.utils.GroupManage;
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
        MessagePersistenceConfig.class,
        GroupManage.class, SpringUtils.class,
        MessagePlusService.class, MessagePlusProperties.class, MessagePersistenceProperties.class,
        WebConfig.class
})
public @interface EnableMessagePlus {
}
