package org.dayatang.domain;

import org.dayatang.IocException;
import org.dayatang.IocInstanceNotFoundException;

import java.util.HashMap;
import java.util.Map;


/**
 * 实例工厂类，充当IoC容器的门面，通过它可以获得部署在IoC容器中的Bean的实例。 InstanceFactory向客户代码隐藏了IoC
 * 工厂的具体实现。在后台，它通过InstanceProvider策略接口，允许选择不同的IoC工厂，例如Spring， Google Guice和
 * TapestryIoC等等。<br>
 *     IoC工厂应该在应用程序启动时装配好，也就是把初始化好的InstanceProvider实现类提供给InstanceFactory。对于web应用
 * 来说，最佳的初始化方式是创建一个Servlet过滤器或监听器，并部署到web.xml里面；对普通java应用程序来说，最佳的初始化
 * 位置是在main()函数里面；对于单元测试，最佳的初始化位置是@BeforeClass或@Before标注的方法内部。<br>
 *     InstanceFactor顺序通过三种途径获取Bean实例。（1）如果已经给InstanceFactory设置了InstanceProvider，那么就通过后者
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

    //缺省实例提供者，当没有设置实例提供者时使用。
    private static InstanceProvider defaultInstanceProvider = new Jdk6InstanceProvider();

    private InstanceFactory() {
        super();
    }

    /**
     * 获取实例提供者。
     *
     * @return 实体提供者的一个实现类。
     */
    private static InstanceProvider getInstanceProvider() {
        return instanceProvider;
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
     * 获取指定类型的对象实例。如果IoC容器没配置好或者IoC容器中找不到该类型的实例则抛出异常。
     *
     * @param <T>       对象的类型
     * @param beanClass 对象的类
     * @return 类型为T的对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> beanClass) {
        T result = getInstanceFromInstanceProvider(beanClass);
        if (result != null) {
            return result;
        }
        result = getInstanceFromDefaultInstanceProvider(beanClass);
        if (result != null) {
            return result;
        }
        result = (T) instances.get(beanClass);
        if (result != null) {
            return result;
        }
        throw new IocInstanceNotFoundException("There's not bean of type '" + beanClass + "' exists in IoC container!");
    }

    private static <T> T getInstanceFromInstanceProvider(Class<T> beanClass) {
        if (instanceProvider == null) {
            return null;
        }
        return instanceProvider.getInstance(beanClass);
    }

    private static <T> T getInstanceFromDefaultInstanceProvider(Class<T> beanClass) {
        return defaultInstanceProvider.getInstance(beanClass);
    }

    /**
     * 获取指定类型的对象实例。如果IoC容器没配置好或者IoC容器中找不到该实例则抛出异常。
     *
     * @param <T>       对象的类型
     * @param beanName  实现类在容器中配置的名字
     * @param beanClass 对象的类
     * @return 类型为T的对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> beanClass, String beanName) {
        T result = null;
        try {
            result = getInstanceProvider().getInstance(beanClass, beanName);
        } catch (Exception e) {
            throw new IocException("IoC container exception!", e);
        }
        if (result != null) {
            return result;
        }
        result = (T) instances.get(toName(beanClass.getName(), beanName));
        if (result != null) {
            return result;
        }
        throw new IocInstanceNotFoundException("There's not bean '" + beanName + "' of type '" + beanClass + "' exists in IoC container!");
    }

    /**
     * 以下部分仅用于提供代码测试功能，产品代码不要用
     */

    private static Map<Object, Object> instances = new HashMap<Object, Object>();

    /**
     * 将服务绑定到具体实例
     *
     * @param serviceInterface
     * @param serviceImplementation
     */
    public static <T> void bind(Class<T> serviceInterface, T serviceImplementation) {
        instances.put(serviceInterface, serviceImplementation);
    }

    /**
     * 将服务绑定到具体实例并指定名字
     *
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

}
