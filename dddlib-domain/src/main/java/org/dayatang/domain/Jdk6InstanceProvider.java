package org.dayatang.domain;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.util.ServiceLoader;

/**
 * 用JDK6 ServiceLoader实现的实例提供者。使用它的话，根本不需要任何IoC框架，也不需要配置文件
 * 指明接口的实现类是哪个，系统会自行查找接口的实现类。
 * 举例：假如你有一个接口（或抽象类）a.b.c.Xxx和它的一个实现类x.y.z.XxxImpl，那么你只需要在/META-INF/services
 * 目录下创建一个文本文件a.b.c.Xxx，其内容中只包含一行文字：x.y.z.XxxImpl。那么，Jdk6InstanceProvider类的
 * getInstance(a.b.c.Xxx.class)方法就会实例化一个x.y.z.XxxImpl类的实例并返回给调用者。
 * 可以为接口提供多个实现类，只需要在文件a.b.c.Xxx中添加代表其他实现类名的行。这样，getInstance(Class<T> beanType)
 * 方法就会返回列表中的第一个实现类的实例。也可以用getInstance(a.b.c.Xxx.class, "xyz")方法，获得标记为
 * Named("xyz")的实现类的实例。
 * 该方法的灵活性在于：首先你不需要通过配置告诉系统哪个接口由哪个实现类来实现，系统会自行查找接口的实现类；其次，
 * 你可以把不同的实现类放在不同的jar里，通过在相应的jar的META-INF/services中定义接口的实现类，可以通过替换实现
 * jar的方式直接替换底层实现。
 * 该方法的缺点包括：（1）JDK6以上才支持；（2）要求每个实现类都要有一个无参构造函数；（3）没有其他IoC框架那样的
 * Singleton等范围定义；
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class Jdk6InstanceProvider implements InstanceProvider {

    /**
     * 根据类型获取对象实例。返回的对象实例所属的类是T或它的实现类或子类。如果找不到该类型的实例则返回null。
     * 如果有多个类都是T的实例，那么返回哪一个类实例是不确定的。这时建议采用getInstance(Class beanType,
     * String beanName) 或getInstance(Class beanType, Class annotationType)
     * 方法缩小筛选范围。
     * @param <T> 类型参数
     * @param beanType 实例的类型
     * @return 指定类型的实例。
     */
	@Override
	public <T> T getInstance(Class<T> beanType) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(beanType);
        if (serviceLoader.iterator().hasNext()) {
            return serviceLoader.iterator().next();
        }
		return null;
	}

    /**
     * 根据类型和名称获取对象实例。如果找不到该类型的实例则返回null。
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
		if (StringUtils.isEmpty(beanName)) {
			return getInstance(beanType);
		}
		for (T instance : ServiceLoader.load(beanType)) {
			Named named = instance.getClass().getAnnotation(Named.class);
			if (named != null && beanName.equals(named.value())) {
				return instance;
			}
		}
		return null;
	}

    /**
     * 根据类型和Annotation获取对象实例。如果找不到该类型的实例则返回null。
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
        if (annotationType == null) {
            return getInstance(beanType);
        }
        for (T instance : ServiceLoader.load(beanType)) {
            Annotation beanAnnotation = instance.getClass().getAnnotation(annotationType);
            if (beanAnnotation != null && beanAnnotation.annotationType().equals(annotationType)) {
                return instance;
            }
        }
        return null;
    }
}

