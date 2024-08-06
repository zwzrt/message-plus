package cn.redcoral.messageplus;

import cn.redcoral.messageplus.config.MessagePlusConfig;
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
@Import(MessagePlusConfig.class)
public @interface EnableMessagePlus {
}
