package org.dayatang.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * JavaBean工具类。
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class BeanUtils {
    
    private final Object bean;
    private final BeanClassUtils beanClassUtils;

    public BeanUtils(final Object bean) {
        Assert.notNull(bean);
        this.bean = bean;
        beanClassUtils = new BeanClassUtils(bean.getClass());
    }
    

    /**
     * 获得指定的JavaBean类型的所有属性的类型
     *
     * @return 一个Map，Key为属性名， Value为属性所属的类
     */
    public Map<String, Class<?>> getPropTypes() {
        return beanClassUtils.getPropTypes();
    }

    /**
     * 获得JavaBean的所有属性值
     *
     * @return 一个Map，其中Key为属性名，Value为属性值。
     */
    public Map<String, Object> getPropValues() {
        Map<String, Object> results = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                String propName = propertyDescriptor.getName();
                Method readMethod = propertyDescriptor.getReadMethod();
                if (readMethod == null) {
                    continue;
                }
                Object value = readMethod.invoke(bean, new Object[]{});
                results.put(propName, value);
            }
            results.remove("class");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    /**
     * 获得指定JavaBean类型的所有属性的名字
     *
     * @return JavaBean的属性名的集合
     */
    public Set<String> getPropNames() {
        return beanClassUtils.getPropNames();
    }

    /**
     * 获得指定JavaBean类型的所有可读属性的名字
     *
     * @return JavaBean的属性名的集合
     */
    public Set<String> getReadablePropNames() {
        return beanClassUtils.getReadablePropNames();
    }

}
