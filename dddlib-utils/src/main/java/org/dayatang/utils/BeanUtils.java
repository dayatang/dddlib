package org.dayatang.utils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
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
     * 获得指定的JavaBean类型的所有属性的类型，包括从父类继承的属性
     *
     * @return 一个Map，Key为属性名， Value为属性所属的类
     */
    public Map<String, Class<?>> getPropTypes() {
        return beanClassUtils.getPropTypes();
    }

    /**
     * 获得JavaBean的所有属性值，包括从父类继承的属性
     *
     * @return 一个Map，其中Key为属性名，Value为属性值。
     */
    public Map<String, Object> getPropValues() {
        return getPropValues(getReadablePropNames());
    }

    /**
     * 获得JavaBean的属性值的值，包括从父类继承的属性，不包含指定的属性。
     *
     * @param excludePropNames
     * @return 一个Map，其中Key为属性名，Value为属性值。
     */
    public Map<String, Object> getPropValuesExclude(String... excludePropNames) {
        return getPropValues(beanClassUtils.getReadablePropNamesExclude(excludePropNames));
    }

    /**
     * 获得JavaBean的属性值的值，包括从父类继承的属性，不包含指定的属性。
     *
     * @param excludeAnnotations
     * @return 一个Map，其中Key为属性名，Value为属性值。
     */
    public Map<String, Object> getPropValuesExclude(Class<? extends Annotation>... excludeAnnotations) {
        return getPropValues(beanClassUtils.getReadablePropNamesExclude(excludeAnnotations));
    }
    
    private Map<String, Object> getPropValues(Set<String> propNames) {
        Map<String, Object> results = new HashMap<String, Object>();
        Map<String, PropertyDescriptor> props = beanClassUtils.getPropertyDescriptors();
        try {
            for (String propName : propNames) {
                PropertyDescriptor propertyDescriptor = props.get(propName);
                Method readMethod = propertyDescriptor.getReadMethod();
                if (readMethod == null) {
                    continue;
                }
                Object value = readMethod.invoke(bean, new Object[]{});
                results.put(propName, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    /**
     * 获得指定JavaBean类型的所有属性的名字，包括从父类继承的属性
     *
     * @return JavaBean的属性名的集合
     */
    public Set<String> getPropNames() {
        return beanClassUtils.getPropNames();
    }

    /**
     * 获得指定JavaBean类型的所有可读属性的名字，包括从父类继承的属性
     *
     * @return JavaBean的属性名的集合
     */
    public Set<String> getReadablePropNames() {
        return beanClassUtils.getReadablePropNames();
    }

    public Object getPropValue(String propName) {
        return getPropValues().get(propName);
    }
}
