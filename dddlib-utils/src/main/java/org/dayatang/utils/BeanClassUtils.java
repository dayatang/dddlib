package org.dayatang.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 针对JavaBean Class的工具类。
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class BeanClassUtils {
    
    private final Class<?> clazz;

    public BeanClassUtils(final Class<?> clazz) {
        Assert.notNull(clazz);
        this.clazz = clazz;
    }
    

    /**
     * 获得所有属性的类型
     *
     * @return 一个Map，Key为属性名， Value为属性所属的类
     */
    public Map<String, Class<?>> getPropTypes() {
        Map<String, Class<?>> results = new HashMap<String, Class<?>>();
            for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors()) {
                String propName = propertyDescriptor.getName();
                results.put(propName, propertyDescriptor.getPropertyType());
            }
        return results;
    }
    
    Set<PropertyDescriptor> getPropertyDescriptors() {
        Set<PropertyDescriptor> results = new HashSet<PropertyDescriptor>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                if (propertyDescriptor.getName().equals("class")) {
                    continue;
                }
                results.add(propertyDescriptor);
            }
        } catch (IntrospectionException e) {
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
        return getPropTypes().keySet();
    }

    /**
     * 获得指定JavaBean类型的所有可读属性的名字
     *
     * @return JavaBean的属性名的集合
     */
    public Set<String> getReadablePropNames() {
        Set<String> results = new HashSet<String>();
            for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors()) {
                if (propertyDescriptor.getReadMethod() != null) {
                    results.add(propertyDescriptor.getName());
                }
            }
        return results;
    }

}
