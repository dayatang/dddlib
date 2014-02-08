package org.dayatang.domain;


/**
 * 实例提供者接口，其实现类以适配器的方式将Bean查找的任务委托给真正的IoC容器，如SpringIoC或Google Guice。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public interface InstanceProvider {

	/**
	 * 获取指定接口或基类的实现类或子类的实例
	 * @param <T> 类型参数
	 * @param beanClass 实例的类型
	 * @return 指定类型的实例。
	 */
	<T> T getInstance(Class<T> beanClass);

	/**
	 * 根据beanName指定的名字获取指定接口或基类的实现类或子类的实例
	 * @param <T> 类型参数
	 * @param beanName 实现类在容器中配置的名字
	 * @param beanClass 实例的类型
	 * @return 指定类型的实例。
	 */
	<T> T getInstance(Class<T> beanClass, String beanName);
}
