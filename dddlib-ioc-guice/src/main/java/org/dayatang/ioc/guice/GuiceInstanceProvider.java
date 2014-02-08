package org.dayatang.ioc.guice;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;
import org.dayatang.domain.InstanceProvider;

import java.lang.annotation.Annotation;

/**
 * 实例提供者接口的Google Guice实现。
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 */
public class GuiceInstanceProvider implements InstanceProvider {
    private Injector injector;

    /**
     * 以一批guice模块初始化guice实例提供者。
     *
     * @param modules 一或多个guice模块
     */
    public GuiceInstanceProvider(Module... modules) {
        injector = Guice.createInjector(modules);
    }

    /**
     * 从Injector生成GuiceProvider
     *
     * @param injector
     */
    public GuiceInstanceProvider(Injector injector) {
        this.injector = injector;
    }


    /**
     * 根据类型获取对象实例。返回的对象实例所属的类是T或它的实现类或子类。如果找不到该类型的实例则抛出异常。
     * @param <T> 类型参数
     * @param beanType 实例的类型
     * @return 指定类型的实例。
     */
    @Override
    public <T> T getInstance(Class<T> beanType) {
        try {
            return injector.getInstance(beanType);
        } catch (com.google.inject.ConfigurationException e) {
            return null;
        }
    }

    /**
     * 根据类型和名称获取对象实例。如果找不到该类型的实例则抛出异常。
     * 假如有两个类MyService1和MyService2都实现了接口Service，而在Guice模块中这样注册：
     * <pre>
     * binder.bind(Service.class).to(MyService1.class)
     * binder.bind(Service.class).annotatedWith(Names.named("service2")).to(MyService2.class)
     * </pre>
     * 那么getInstance(Service.class, "service2")将返回MyService2的实例。
     *
     * @param <T> 类型参数
     * @param beanName 实现类在容器中配置的名字
     * @param beanType 实例的类型
     * @return 指定类型的实例。
     */
    @Override
    public <T> T getInstance(Class<T> beanType, String beanName) {
        Key<T> key = Key.get(beanType, Names.named(beanName));
        try {
            return injector.getInstance(key);
        } catch (com.google.inject.ConfigurationException e) {
            return null;
        }
    }

    /**
     * 根据类型和Annotation获取对象实例。如果找不到该类型的实例则抛出异常。
     * 假如有两个类MyService1和MyService2都实现了接口Service，其中MyService2标记为@MyAnnotation,
     * 同时MyAnnotation标记为@BindingAnnotation @Retention(RetentionPolicy.RUNTIME) ,
     * 而在Guice模块中这样注册：
     * binder.bind(Service.class).to(MyService1.class)
     * binder.bind(Service.class).annotatedWith(MyAnnotation.class).to(MyService2.class)
     * 那么getInstance(Service.class, MyAnnotation.class)将返回MyService2的实例。
     *
     * @param <T> 类型参数
     * @param beanType 实例的类型
     * @param annotationType 实现类的annotation类型
     * @return 指定类型的实例。
     */
    @Override
    public <T> T getInstance(Class<T> beanType, Class<? extends Annotation> annotationType) {
        Key<T> key = Key.get(beanType, annotationType);
        try {
            return injector.getInstance(key);
        } catch (com.google.inject.ConfigurationException e) {
            return null;
        }
    }
}
