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

    /**
     * 根据类型获取对象实例。返回的对象实例所属的类是T或它的实现类或子类。如果找不到该类型的实例则抛出异常。
     * 如果有多个类都是T的实例，那么返回哪一个类实例是不确定的。这时建议采用getInstance(Class<T> beanType,
     * String beanName) 或getInstance(Class<T> beanType, Class<? extends Annotation> annotationType)
     * 方法缩小筛选范围。
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
     * 根据类型和名称获取对象实例。如果找不到该类型的实例则抛出异常。
     * 假如有两个类A和B都实现了接口T(或继承了基类T，或者就是其类型就是T)，而其中A类标注为
     * @Named("abc")，那么getInstance(T.class, "abc")将返回类A的实例。
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
     * 根据类型和Annotation获取对象实例。如果找不到该类型的实例则抛出异常。
     * 假如有两个类A和B都实现了接口T(或继承了基类T，或者就是其类型就是T)，而其中A类标注为
     * @a.b.C，那么getInstance(T.class, a.b.C.class)将返回类A的实例。
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
