package com.dayatang.domain;

import java.util.HashMap;
import java.util.Map;

import com.dayatang.IocException;
import com.dayatang.IocInstanceNotFoundException;
import com.dayatang.InstanceProviderNotFoundException;


/**
 * 实例工厂类。通过它可以获得其管理的类的实例。 InstanceFactory向客户代码隐藏了IoC工厂的具体实现。在后台，它通过
 * InstanceProvider策略接口，允许选择不同的IoC工厂，例如Spring， Google Guice和TapestryIoC等等。
 * IoC工厂应该在应用程序启动时装配好，也就是把初始化好的InstanceProvider
 * 实现类提供给InstanceFactory。对于web应用来说，最佳的初始化方式是创
 * 建一个Servlet过滤器或监听器，并部署到web.xml里面；对普通java应用程
 * 序来说，最佳的初始化位置是在main()函数里面；对于单元测试，最佳的初始 化位置是setUp()方法内部。
 * 
 * 
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 * 
 */
public class InstanceFactory {

	private static InstanceProvider instanceProvider;

	private InstanceFactory() {
		super();
	}

	/**
	 * 设置实例提供者。
	 * 
	 * @param provider
	 *            一个实例提供者的实例。
	 */
	public static void setInstanceProvider(InstanceProvider provider) {
		instanceProvider = provider;
	}

	/**
	 * 获取指定类型的对象实例。如果IoC容器没配置好或者IoC容器中找不到该类型的实例则抛出异常。
	 * 
	 * @param <T>
	 *            对象的类型
	 * @param beanClass
	 *            对象的类
	 * @return 类型为T的对象实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> beanClass) {
		T result = null;
		result = (T) instances.get(beanClass.getName());
		if (result != null) {
			return result;
		}
		checkInstanceProviderExistance();
		try {
			result = getInstanceProvider().getInstance(beanClass);
		} catch (Exception e) {
			throw new IocException("IoC container exception!", e);
		}
		if (result == null) {
			throw new IocInstanceNotFoundException("There's not bean of type '" + beanClass + "' exists in IoC container!");
		}
		return result;
	}

	/**
	 * 获取指定类型的对象实例。如果IoC容器没配置好或者IoC容器中找不到该实例则抛出异常。
	 * 
	 * @param <T>
	 *            对象的类型
	 * @param beanName
	 *            实现类在容器中配置的名字
	 * @param beanClass
	 *            对象的类
	 * @return 类型为T的对象实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> beanClass, String beanName) {
		T result = (T) instances.get(toName(beanClass.getName(), beanName));
		if (result != null) {
			return result;
		}
		checkInstanceProviderExistance();
		try {
			result = getInstanceProvider().getInstance(beanClass, beanName);
		} catch (Exception e) {
			throw new IocException("IoC container exception!", e);
		}
		if (result == null) {
			throw new IocInstanceNotFoundException("There's not bean '" + beanName + "' of type '" + beanClass + "' exists in IoC container!");
		}
		return result;
	}
	
	/**
	 * 获取实例提供者。
	 * 
	 * @return 实体提供者的一个实现类。
	 */
	private static InstanceProvider getInstanceProvider() {
		return instanceProvider;
	}

	private static void checkInstanceProviderExistance() {
		if (instanceProvider == null) {
			throw new InstanceProviderNotFoundException("No IoC instance provider exists!");
		}
	}
	
	/**
	 * 以下部分仅用于提供代码测试功能，产品代码不要用
	 */
	
	private static Map<String, Object> instances = new HashMap<String, Object>();
	
	/**
	 * 将服务绑定到具体实例
	 * @param serviceInterface
	 * @param serviceImplementation
	 */
    public static <T> void bind(Class<T> serviceInterface, T serviceImplementation) {
    	instances.put(serviceInterface.getName(), serviceImplementation);
    }

    /**
	 * 将服务绑定到具体实例并指定名字
     * @param serviceInterface
     * @param serviceImplementation
     * @param beanName
     */
    public static <T> void bind(Class<T> serviceInterface, T serviceImplementation, String beanName) {
    	instances.put(toName(serviceInterface.getName(), beanName), serviceImplementation);
    }

	private static String toName(String className, String beanName) {
		return className + ":" + beanName;
	}
	
	/**
	 * 判断是否已经初始化，也就是设置了InstanceProvider
	 * @return
	 */
	public static boolean isInitialized() {
		return instanceProvider != null;
	}
}
