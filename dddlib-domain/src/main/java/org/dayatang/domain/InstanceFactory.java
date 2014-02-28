package org.dayatang.domain;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * <p>
 * 实例工厂类，充当IoC容器的门面，通过它可以获得部署在IoC容器中的Bean的实例。 InstanceFactory向客户代码隐藏了IoC
 * 工厂的具体实现。在后台，它通过InstanceProvider策略接口，允许选择不同的IoC工厂，例如Spring， Google Guice和
 * TapestryIoC等等。
 * <p>
 * IoC工厂应该在应用程序启动时装配好，也就是把初始化好的InstanceProvider实现类提供给InstanceFactory。对于web应用
 * 来说，最佳的初始化方式是创建一个Servlet过滤器或监听器，并部署到web.xml里面；对普通java应用程序来说，最佳的初始化
 * 位置是在main()函数里面；对于单元测试，最佳的初始化位置是@BeforeClass或@Before标注的方法内部。<br>
 * <p>
 * InstanceFactor顺序通过三种途径获取Bean实例。（1）如果已经给InstanceFactory设置了InstanceProvider，那么就通过后者
 * 查找Bean；（2）如果没有设置InstanceProvider，或者通过InstanceProvider无法找到Bean，就通过JDK6的ServiceLoader查找（通
 * 过在类路径或jar中的/META-INF/services/a.b.c.Abc文件中设定内容为x.y.z.Xyz，就表明类型a.b.c.Abc将通过类x.y.z.Xyz
 * 的实例提供）；（3）如果仍然没找到Bean实例，那么将返回那些通过bind()方法设置的Bean实例。（4）如果最终仍然找不到，就抛出
 * IocInstanceNotFoundException异常。
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 */
public class InstanceFactory {

    //实例提供者，代表真正的IoC容器
    private static InstanceProvider instanceProvider;

    private InstanceFactory() {
    }

    /**
     * 设置实例提供者。
     *
     * @param provider 一个实例提供者的实例。
     */
    public static void setInstanceProvider(InstanceProvider provider) {
        instanceProvider = provider;
    }

    /**
     * 根据类型获取对象实例。返回的对象实例所属的类是T或它的实现类或子类。如果找不到该类型的实例则抛出异常。
     *
     * @param <T> 对象的类型
     * @param beanType 对象所属的类型
     * @return 类型为T的对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> beanType) {
        T result = null;
        if (instanceProvider != null) {
            result = getInstanceFromProvider(beanType);
        }
        if (result != null) {
            return result;
        }
        result = getInstanceFromServiceLoader(beanType);
        if (result != null) {
            return result;
        }
        result = (T) instances.get(beanType);
        if (result != null) {
            return result;
        }
        throw new IocInstanceNotFoundException("There's not bean of type '" + beanType + "' exists in IoC container!");
    }

    private static <T> T getInstanceFromProvider(Class<T> beanType) {
        try {
            return instanceProvider.getInstance(beanType);
        } catch (IocInstanceNotFoundException e) {
            return null;
        }
    }

    private static <T> T getInstanceFromServiceLoader(Class<T> beanType) {
        Set<T> results = new HashSet<T>();
        for (T instance : ServiceLoader.load(beanType)) {
            results.add(instance);
        }
        if (results.size() > 1) {
            throw new IocInstanceNotUniqueException("There're more than one bean of type '" + beanType + "'");
        }
        if (results.size() == 1) {
            return results.iterator().next();
        }
        return null;
    }

    /**
     * 根据类型和名称获取对象实例。返回的对象实例所属的类是T或它的实现类或子类。不同的IoC容器用不同的方式解释beanName。
     * 具体的解释方式请参见各种InstanceProvider实现类的Javadoc。 如果找不到该类型的实例则抛出异常。
     *
     * @param <T> 类型参数
     * @param beanName bean的名称
     * @param beanType 实例的类型
     * @return 指定类型的实例。
     */
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> beanType, String beanName) {
        T result = null;
        if (instanceProvider != null) {
            result = getInstanceFromProvider(beanType, beanName);
        }
        if (result != null) {
            return result;
        }
        result = getInstanceFromServiceLoader(beanType, beanName);
        if (result != null) {
            return result;
        }
        result = (T) instances.get(toName(beanType, beanName));
        if (result != null) {
            return result;
        }
        throw new IocInstanceNotFoundException("There's not bean of type '" + beanType + "' exists in IoC container!");
    }

    private static <T> T getInstanceFromProvider(Class<T> beanType, String beanName) {
        try {
            return instanceProvider.getInstance(beanType, beanName);
        } catch (IocInstanceNotFoundException e) {
            return null;
        }
    }

    private static <T> T getInstanceFromServiceLoader(Class<T> beanType, String beanName) {
        Set<T> results = new HashSet<T>();
        for (T instance : ServiceLoader.load(beanType)) {
            Named named = instance.getClass().getAnnotation(Named.class);
            if (named != null && beanName.equals(named.value())) {
                results.add(instance);
            }
        }
        if (results.size() > 1) {
            throw new IocInstanceNotUniqueException("There're more than one bean of type '"
                    + beanType + "' and named '" + beanName + "'");
        }
        if (results.size() == 1) {
            return results.iterator().next();
        }
        return null;

    }

    /**
     * 根据类型和Annotation获取对象实例。返回的对象实例所属的类是T或它的实现类或子类。不同的IoC容器用不同的方式解释annotation。
     * 具体的解释方式请参见各种InstanceProvider实现类的Javadoc。 如果找不到该类型的实例则抛出异常。
     *
     * @param <T> 类型参数
     * @param beanType 实例的类型
     * @param annotationType 实现类的annotation类型
     * @return 指定类型的实例。
     */
    public static <T> T getInstance(Class<T> beanType, Class<? extends Annotation> annotationType) {
        T result = null;
        if (instanceProvider != null) {
            result = getInstanceFromProvider(beanType, annotationType);
        }
        if (result != null) {
            return result;
        }
        result = getInstanceFromServiceLoader(beanType, annotationType);
        if (result != null) {
            return result;
        }
        result = (T) instances.get(toName(beanType, annotationType));
        if (result != null) {
            return result;
        }
        throw new IocInstanceNotFoundException("There's not bean '" 
                + annotationType + "' of type '" + beanType + "' exists in IoC container!");
    }

    private static <T> T getInstanceFromProvider(Class<T> beanType, Class<? extends Annotation> annotationType) {
        try {
            return instanceProvider.getInstance(beanType, annotationType);
        } catch (IocInstanceNotFoundException e) {
            return null;
        }
    }

    private static <T> T getInstanceFromServiceLoader(Class<T> beanType, Class<? extends Annotation> annotationType) {
        Set<T> results = new HashSet<T>();
        for (T instance : ServiceLoader.load(beanType)) {
            Annotation beanAnnotation = instance.getClass().getAnnotation(annotationType);
            if (beanAnnotation != null && beanAnnotation.annotationType().equals(annotationType)) {
                results.add(instance);
            }
        }
        if (results.size() > 1) {
            throw new IocInstanceNotUniqueException("There're more than one bean of type '"
                    + beanType + "' and annotated with '" + annotationType + "'");
        }
        if (results.size() == 1) {
            return results.iterator().next();
        }
        return null;
    }

    /**
     * 以下部分仅用于提供代码测试功能，产品代码不要用
     */
    private static final Map<Object, Object> instances = new HashMap<Object, Object>();

    /**
     * 将服务绑定到具体实例
     *
     * @param <T> Bean实例的类型
     * @param serviceInterface 注册类型
     * @param serviceImplementation 对象实例
     */
    public static <T> void bind(Class<T> serviceInterface, T serviceImplementation) {
        instances.put(serviceInterface, serviceImplementation);
    }

    /**
     * 将服务绑定到具体实例并指定名字
     *
     * @param <T> Bean实例的类型
     * @param serviceInterface 注册类型
     * @param serviceImplementation 对象实例
     * @param beanName 实例名称
     */
    public static <T> void bind(Class<T> serviceInterface, T serviceImplementation, String beanName) {
        instances.put(toName(serviceInterface, beanName), serviceImplementation);
    }

    /**
     * 删除缓存的bean实例
     */
    public static void clear() {
        instances.clear();
    }

    /**
     * 将服务绑定到具体实例并指定关联的Annotation
     *
     * @param <T> Bean实例的类型
     * @param serviceInterface 注册类型
     * @param serviceImplementation 对象实例
     * @param annotationType 标注类型
     */
    public static <T> void bind(Class<T> serviceInterface, T serviceImplementation, Class<? extends Annotation> annotationType) {
        instances.put(toName(serviceInterface, annotationType), serviceImplementation);
    }

    private static String toName(Class<?> beanType, String beanName) {
        return beanType.getName() + ":" + beanName;
    }

    private static String toName(Class<?> beanType, Class<? extends Annotation> annotationType) {
        return beanType.getName() + ":" + annotationType.getName();
    }
}
