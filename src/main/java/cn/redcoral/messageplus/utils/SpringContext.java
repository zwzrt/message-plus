package cn.redcoral.messageplus.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * @Description: SpringContext  获取 Spring 上下文信息
 * @Author: mingtian
 * @CreateDate: 2020/6/8 14:59
 * @Version: 1.0
 */
@Component
//@DependsOn("invocationTargetException")
public class SpringContext implements ApplicationContextAware {

    /**
     * 打印日志
     */
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取上下文对象
     */
    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.applicationContext = applicationContext;
        logger.info("set applicationContext");
    }

    /**
     * 获取 applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过 name 获取 bean 对象
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {

        return getApplicationContext().getBean(name);
    }

    /**
     * 通过 class 获取 bean 对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过 name，clazz  获取指定的 bean 对象
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
