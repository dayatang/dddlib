package org.dayatang.ioc.guice;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;
import org.dayatang.domain.InstanceProvider;

/**
 * 实例提供者接口的Google Guice实现。
 * 
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 * 
 */
public class GuiceInstanceProvider implements InstanceProvider {
	private Injector injector;

	/**
	 * 以一批guice模块初始化guice实例提供者。 
	 * @param modules 一或多个guice模块
	 */
	public GuiceInstanceProvider(Module... modules) {
		injector = Guice.createInjector(modules);
	}

	/**
	 * 从Injector生成GuiceProvider
	 * @param injector
	 */
	public GuiceInstanceProvider(Injector injector) {
		this.injector = injector;
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.dayatang.domain.InstanceProvider#getInstance(java.lang.Class)
	 */
	@Override
	public <T> T getInstance(Class<T> beanClass) {
		return injector.getInstance(beanClass);
	}

    /*
     * (non-Javadoc)
     * @see org.dayatang.domain.InstanceProvider#getInstance(java.lang.Class, java.lang.String)
     */
	@Override
	public <T> T getInstance(Class<T> beanClass, String beanName) {
		Key<T> key = Key.get(beanClass, Names.named(beanName));
		return injector.getInstance(key);
	}
}
