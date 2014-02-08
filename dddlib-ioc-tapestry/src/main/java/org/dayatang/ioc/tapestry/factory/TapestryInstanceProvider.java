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
		RegistryBuilder builder = new RegistryBuilder();
		builder.add(modules);
		registry = builder.build();
		registry.performRegistryStartup();
	}

	@Override
	public <T> T getInstance(Class<T> beanType) {
        try {
            return registry.getService(beanType);
        } catch (RuntimeException e) {
            return null;
        }
	}

	@Override
	public <T> T getInstance(Class<T> beanType, String beanName) {
        try {
    		return registry.getService(beanName, beanType);
        } catch (RuntimeException e) {
            return null;
        }
	}

    /**
     * 获取指定类型的、含有指定Annotation的对象实例。
     *
     * @param beanType  实例的类型
     * @param annotationType 实现类的annotation
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
