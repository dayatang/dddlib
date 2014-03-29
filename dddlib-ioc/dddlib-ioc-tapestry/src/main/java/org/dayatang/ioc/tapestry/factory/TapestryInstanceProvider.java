package org.dayatang.ioc.tapestry.factory;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.dayatang.domain.InstanceProvider;

import java.lang.annotation.Annotation;

/**
 * IoC实例提供者接口InstanceProvider的TapestryIoC实现。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class TapestryInstanceProvider implements InstanceProvider {
	private Registry registry;

	public TapestryInstanceProvider(Registry registry) {
		this.registry = registry;
	}

	public TapestryInstanceProvider(Class<?>... modules) {
            registry = RegistryBuilder.buildAndStartupRegistry(modules);
	}

    /**
     * 根据类型获取对象实例。返回的对象实例所属的类是T或它的实现类或子类。
     * 如果TapestryIoC有没有或者有多个类都是T的实例，将返回null
     * @param <T> 类型参数
     * @param beanType 实例的类型
     * @return 指定类型的实例。
     */
	@Override
	public <T> T getInstance(Class<T> beanType) {
        try {
            return registry.getService(beanType);
        } catch (RuntimeException e) {
            return null;
        }
	}

    /**
     * 根据类型和名称获取对象实例。如果找不到该类型的实例则返回null。
     * 假如有两个类MyService1和MyService2都实现了接口Service，而在Tapestry模块中这样注册：
     * <pre>
     * binder.bind(Service.class, MyService1.class).withId("service1");
     * binder.bind(Service.class, MyService2.class).withId("service2");
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
        try {
    		return registry.getService(beanName, beanType);
        } catch (RuntimeException e) {
            return null;
        }
	}

    /**
     * 根据类型和Annotation获取对象实例。如果找不到该类型的实例则返回null。
     * 假如有两个类MyService1和MyService2都实现了接口Service，在Tapestry模块中这样注册：
     * binder.bind(Service.class, MyService1.class).withId("service1");
     * binder.bind(Service.class, MyService2.class).withMarker(TheAnnotation.class);
     * 那么getInstance(Service.class, MyAnnotation.class)将返回MyService2的实例。
     *
     * @param <T> 类型参数
     * @param beanType 实例的类型
     * @param annotationType 实现类的annotation类型
     * @return 指定类型的实例。
     */
    @Override
    public <T> T getInstance(Class<T> beanType, Class<? extends Annotation> annotationType) {
        try {
            return registry.getService(beanType, annotationType);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public void shutdown() {
		registry.shutdown();
	}
}
