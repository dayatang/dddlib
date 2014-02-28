package org.dayatang.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
     * 获得所有属性的类型，包括从父类继承的属性
     *
     * @return 一个Map，Key为属性名， Value为属性所属的类
     */
    public Map<String, Class<?>> getPropTypes() {
        Map<String, Class<?>> results = new HashMap<String, Class<?>>();
        for (Map.Entry<String, PropertyDescriptor> each : getPropertyDescriptors().entrySet()) {
            results.put(each.getKey(), each.getValue().getPropertyType());
        }
        return results;
    }

    /**
     * 获得指定JavaBean类型的所有属性的名字，包括从父类继承的属性
     *
     * @return JavaBean的属性名的集合
     */
    public Set<String> getPropNames() {
        return getPropertyDescriptors().keySet();
    }

    /**
     * 获得指定JavaBean类型的所有可读属性的名字，包括从父类继承的属性
     *
     * @return JavaBean的属性名的集合
     */
    public Set<String> getReadablePropNames() {
        Set<String> results = new HashSet<String>();
        for (Map.Entry<String, PropertyDescriptor> each : getPropertyDescriptors().entrySet()) {
            if (each.getValue().getReadMethod() == null) {
                continue;
            }
            results.add(each.getKey());
        }
        return results;
    }

    /**
     * 获得指定JavaBean类型的所有可写属性的名字，包括从父类继承的属性
     *
     * @return JavaBean的属性名的集合
     */
    public Set<String> getWritablePropNames() {
        Set<String> results = new HashSet<String>();
        for (Map.Entry<String, PropertyDescriptor> each : getPropertyDescriptors().entrySet()) {
            if (each.getValue().getWriteMethod() == null) {
                continue;
            }
            results.add(each.getKey());
        }
        return results;
    }

    /**
     * 获得JavaBean的属性值的值，包括从父类继承的属性，不包含指定的属性。
     *
     * @param excludePropNames
     * @return 一个Map，其中Key为属性名，Value为属性值。
     */
    public Set<String> getReadablePropNamesExclude(String... excludePropNames) {
        List<String> propNamesExclude = Arrays.asList(excludePropNames);
        Set<String> results = new HashSet<String>();
        for (String propName : getReadablePropNames()) {
            if (propNamesExclude.contains(propName)) {
                continue;
            }
            results.add(propName);
        }
        return results;
    }

    /**
     * 获得JavaBean的属性值的值，包括从父类继承的属性，不包含指定由指定Annotation标记的的属性。
     *
     * @param excludeAnnotations
     * @return 一个Map，其中Key为属性名，Value为属性值。
     */
    public Set<String> getReadablePropNamesExclude(Class<? extends Annotation>... excludeAnnotations) {
        List<Class<? extends Annotation>> annotationsExclude = Arrays.asList(excludeAnnotations);
        Set<String> results = new HashSet<String>();
        Map<String, PropertyDescriptor> props = getPropertyDescriptors();
        for (String propName : getReadablePropNames()) {
            PropertyDescriptor propertyDescriptor = props.get(propName);
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            if (methodContainsAnnotation(readMethod, annotationsExclude)) {
                continue;
            }
            results.add(propName);
        }
        return results;
    }

    Map<String, PropertyDescriptor> getPropertyDescriptors() {
        Map<String, PropertyDescriptor> results = new HashMap<String, PropertyDescriptor>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                results.put(propertyDescriptor.getName(), propertyDescriptor);
            }
            results.remove("class");
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    private boolean methodContainsAnnotation(Method readMethod, List<Class<? extends Annotation>> annotationsExclude) {
        for (Class<? extends Annotation> annotationClass : annotationsExclude) {
            if (readMethod.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
    }

}
