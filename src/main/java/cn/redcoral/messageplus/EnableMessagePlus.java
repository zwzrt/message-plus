package cn.redcoral.messageplus;

import cn.redcoral.messageplus.config.SpringConfig;
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
@Import({SpringConfig.class})
public @interface EnableMessagePlus {
}
