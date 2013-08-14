package com.dayatang.tapestry.factory;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

import com.dayatang.domain.InstanceProvider;

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

	public void shutdown() {
		registry.shutdown();
	}
}
