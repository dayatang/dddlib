package com.dayatang.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 数组工具
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class ArrayUtils {
	
	/**
	 * 抽取数组中每个元素的一个属性形成新的数组。
	 * @param items 原始数组
	 * @param property 要抽取的数组元素属性
	 * @return
	 */
	public static Object[] substract(Object[] items, String property) {
		if (items == null) {
			return null;
		}
		if (items.length == 0) {
			return new Object[0];
		}
		if (StringUtils.isEmpty(property)) {
			throw new IllegalArgumentException("property name must not empty!");
		}
		Object[] results = new Object[items.length];
		for (int i = 0; i < items.length; i++) {
			Object item = items[i];
			Map<String, Object> propValues = BeanUtils.getPropValues(item);
			if (!propValues.containsKey(property)) {
				throw new IllegalArgumentException("Property " + property  + " not exists!");
			}
			results[i] = propValues.get(property);
		}
		return results;
	}

	/**
	 * 抽取数组中每个元素的一个属性形成新的数组，然后用指定的分隔符连接起来形成一个字符串。
	 * @param items 原始数组
	 * @param property 要抽取的数组元素属性
	 * @param separator 字符串分隔符
	 * @return
	 */
	public static String join(Object[] items, String property, String separator) {
		if (items == null || items.length == 0) {
			return "";
		}
		return StringUtils.join(substract(items, property), separator);
	}

	private ArrayUtils() {
		super();
	}
}
