package org.dayatang.utils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaBean工具类。
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
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
     * 在两个Bean之间复制属性值
     *
     * @param fromBean 作为复制源的Bean
     * @param toBean   作为复制目标的Bean
     */
    private static void copyProperties(Object fromBean, Object toBean, String... excludeProps) {
        BeanUtils from = new BeanUtils(fromBean);
        BeanUtils to = new BeanUtils(toBean);
        Map<String, Object> values = from.getPropValues();
        Set<String> propsToCopy = to.getWritablePropNames();
        if (excludeProps != null) {
            propsToCopy.removeAll(Arrays.asList(excludeProps));
        }
        for (String prop : propsToCopy) {
            if (values.containsKey(prop)) {
                to.setPropValue(prop, values.get(prop));
            }
        }
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
     * @param excludePropNames 要排除的属性名
     * @return 一个Map，其中Key为属性名，Value为属性值。
     */
    public Map<String, Object> getPropValuesExclude(String... excludePropNames) {
        return getPropValues(beanClassUtils.getReadablePropNamesExclude(excludePropNames));
    }

    /**
     * 获得JavaBean的属性值的值，包括从父类继承的属性，不包含指定的属性。
     *
     * @param excludeAnnotations 一批Annotation，被这些Annotation标注的属性将被排除
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

    /**
     * 获得指定JavaBean类型的所有可读属性的名字，包括从父类继承的属性
     *
     * @return JavaBean的属性名的集合
     */
    public Set<String> getWritablePropNames() {
        return beanClassUtils.getWritablePropNames();
    }

    /**
     * 获得指定属性的值
     *
     * @param propName 属性名
     * @return 属性值
     */
    public Object getPropValue(String propName) {
        return getPropValues().get(propName);
    }

    /**
     * 设置属性值
     *
     * @param key   要设置值的属性名
     * @param value 要设置的值
     */
    public void setPropValue(String key, Object value) {
        for (Map.Entry<String, PropertyDescriptor> entry : beanClassUtils.getPropertyDescriptors().entrySet()) {
            if (!entry.getKey().equals(key)) {
                continue;
            }
            PropertyDescriptor propertyDescriptor = entry.getValue();
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }
            try {
                writeMethod.invoke(bean, value);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 从properties填充属性值
     *
     * @param properties 表示一批属性值的Map，Key为属性名，Value为属性值
     */
    public void populate(Map<String, ? extends Object> properties) {
        for (Map.Entry<String, ? extends Object> entry : properties.entrySet()) {
            setPropValue(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 从另一个Bean提取属性值，填充当前Bean的同名属性
     *
     * @param otherBean 另外的JavaBean
     */
    public void copyPropertiesFrom(Object otherBean) {
        copyProperties(otherBean, bean);
    }

    /**
     * 将当前Bean的属性值填充到另一个Bean的同名属性
     *
     * @param otherBean 另外的JavaBean
     */
    public void copyPropertiesTo(Object otherBean) {
        copyProperties(bean, otherBean);
    }

    /**
     * 从另一个Bean提取属性值，填充当前Bean的同名属性
     *
     * @param otherBean    另外的JavaBean
     * @param excludeProps 不参与复制的属性名
     */
    public void copyPropertiesFrom(Object otherBean, String... excludeProps) {
        copyProperties(otherBean, bean, excludeProps);
    }

    /**
     * 将当前Bean的属性值填充到另一个Bean的同名属性
     *
     * @param otherBean    另外的JavaBean
     * @param excludeProps 不参与复制的属性名
     */
    public void copyPropertiesTo(Object otherBean, String... excludeProps) {
        copyProperties(bean, otherBean, excludeProps);
    }
}
