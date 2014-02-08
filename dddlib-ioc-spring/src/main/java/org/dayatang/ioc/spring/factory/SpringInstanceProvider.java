package org.dayatang.ioc.spring.factory;

import org.dayatang.domain.InstanceProvider;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 实例提供者接口的Spring实现。
 * SpringProvider内部通过Spring IoC的ApplicationContext实现对象创建。
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 */
public class SpringInstanceProvider implements InstanceProvider {

    private ApplicationContext applicationContext;

    /**
     * 以一批spring配置文件的路径初始化spring实例提供者。
     *
     * @param locations spring配置文件的路径的集合。spring将从类路径开始获取这批资源文件。
     */
    public SpringInstanceProvider(String... locations) {
        applicationContext = new ClassPathXmlApplicationContext(locations);
    }

    /**
     * 从ApplicationContext生成SpringProvider
     *
     * @param applicationContext
     */
    public SpringInstanceProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 根据一批Spring配置文件初始化spring实例提供者。
     *
     * @param annotatedClasses
     */
    public SpringInstanceProvider(Class<?>... annotatedClasses) {
        applicationContext = new AnnotationConfigApplicationContext(annotatedClasses);
    }

    /**
     * 返回指定类型的实例。
     *
     * @param beanType 实例的类型
     * @return 指定类型的实例。
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getInstance(Class<T> beanType) {
        try {
            return (T) applicationContext.getBean(beanType);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    @Override
    public <T> T getInstance(Class<T> beanType, String beanName) {
        try {
            return (T) applicationContext.getBean(beanName, beanType);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }


    /**
     * 获取指定类型的、含有指定Annotation的对象实例。
     *
     * @param beanType       实例的类型
     * @param annotationType 实现类的annotation
     * @return 指定类型的实例。
     */
    @Override
    public <T> T getInstance(Class<T> beanType, Class<? extends Annotation> annotationType) {
        if (annotationType == null) {
            return getInstance(beanType);
        }
        Map<String, T> results = applicationContext.getBeansOfType(beanType);
        for (Map.Entry<String, T> entry : results.entrySet()) {
            if (applicationContext.findAnnotationOnBean(entry.getKey(), annotationType) != null) {
                return entry.getValue();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getByBeanName(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }
}
