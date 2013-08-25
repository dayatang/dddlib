package com.dayatang.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * JavaBean工具类。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class BeanUtils {
	
	/**
	 * 获得指定的JavaBean类型的所有属性的类型
	 * @param clazz JavaBean的类
	 * @return
	 */
	public static Map<String, Class<?>> getPropTypes(Class<?> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("clazz must not null!");
		}
		Map<String, Class<?>> results = new HashMap<String, Class<?>>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
				String propName = propertyDescriptor.getName();
				if (propertyDescriptor.getReadMethod() == null) {
					continue;
				}
				results.put(propName, propertyDescriptor.getPropertyType());
			}
			results.remove("class");
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
		return results;
	}
	
	/**
	 * 获得指定JavaBean类型的所有属性的名字
	 * @param clazz JavaBean的类
	 * @return
	 */
	public static Set<String> getPropNames(Class<?> clazz) {
		return getPropTypes(clazz).keySet();
	}
	
	/**
	 * 获得指定JavaBean的所有属性值
	 * @param bean 目标JavaBean
	 * @return
	 */
	public static Map<String, Object> getPropValues(Object bean) {
		if (bean == null) {
			throw new IllegalArgumentException("Target object must not null!");
		}
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

}
