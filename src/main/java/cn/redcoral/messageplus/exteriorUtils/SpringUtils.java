package cn.redcoral.messageplus.exteriorUtils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Repository;

/**
 * Spring工具类，用于在非Spring管理的类中获取Spring Bean
 * 通过实现BeanFactoryPostProcessor接口，可以在Spring启动时获取BeanFactory
 */
@Repository
public class SpringUtils implements BeanFactoryPostProcessor {

    //Spring应用上下文环境
    private static ConfigurableListableBeanFactory beanFactory;

    /**
     * 在Spring加载bean定义后立即调用此方法，用于保存beanFactory的引用
     * @param beanFactory Spring的bean工厂
     * @throws BeansException 如果出现异常
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }

    /**
     * 获取BeanFactory方法，用于外部获取Spring Bean
     * @return ConfigurableListableBeanFactory Spring的bean工厂
     */
    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * 通过Bean的名称获取Bean实例
     * @param name Bean的名称
     * @return T Bean实例
     * @throws BeansException 如果出现异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) getBeanFactory().getBean(name);
    }

    /**
     * 通过Class类型获取Bean实例
     * @param clz Bean的类型
     * @return T Bean实例
     * @throws BeansException 如果出现异常
     */
    public static <T> T getBean(Class<T> clz) throws BeansException {
        T result = (T) getBeanFactory().getBean(clz);
        return result;
    }
}
