package com.dayatang.tapestry.factory;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

import com.dayatang.domain.InstanceProvider;

/**
 * 因命名不恰当，请改用TapestryInstanceProvider。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
@Deprecated
public class TapestryProvider implements InstanceProvider {
	private Registry registry;

	public TapestryProvider(Registry registry) {
		this.registry = registry;
	}

	public TapestryProvider(Class<?>... beanClasses) {
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
