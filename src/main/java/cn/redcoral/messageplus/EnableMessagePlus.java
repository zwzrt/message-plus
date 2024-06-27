package cn.redcoral.messageplus;

import cn.redcoral.messageplus.config.MessagePersistenceConfig;
import cn.redcoral.messageplus.config.MessagePlusConfig;
import cn.redcoral.messageplus.utils.GroupManage;
import cn.redcoral.messageplus.utils.SpringUtils;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 消息增强插件启动注解
 * @author mo
 * @日期: 2024-05-24 19:29
 **/
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({MessagePlusConfig.class, MessagePersistenceConfig.class, GroupManage.class, SpringUtils.class})
public @interface EnableMessagePlus {
}
