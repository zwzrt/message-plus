package cn.redcoral.messageplus.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author mo
 **/
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 通过bean的id获取bean对象
     */
    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    public static <R> R getBean(Class<R> type) {
        return applicationContext.getBean(type);
    }

    /**
     * 根据bean的id和类型获取bean对象
     */
    public static <T> T getBean(String beanName,Class<T> clazz){
        return clazz.cast(getBean(beanName));
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
