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

	public TapestryInstanceProvider(Class<?>... beanClasses) {
		RegistryBuilder builder = new RegistryBuilder();
		builder.add(beanClasses);
		registry = builder.build();
		registry.performRegistryStartup();
	}

	@Override
	public <T> T getInstance(Class<T> beanClass) {
		return registry.getService(beanClass);
	}

	@Override
	public <T> T getInstance(Class<T> beanClass, String beanName) {
		return registry.getService(beanName, beanClass);
	}

    /**
     * 获取指定类型的、含有指定Annotation的对象实例。
     *
     * @param beanClass  实例的类型
     * @param annotation 实现类的annotation
     * @return 指定类型的实例。
     */
    @Override
    public <T> T getInstance(Class<T> beanClass, Annotation annotation) {
        return registry.getService(beanClass, annotation.annotationType());
    }

    public void shutdown() {
		registry.shutdown();
	}
}
