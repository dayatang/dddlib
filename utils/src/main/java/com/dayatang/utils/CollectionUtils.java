package com.dayatang.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 集合工具
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class CollectionUtils {
	
	/**
	 * 抽取集合中每个元素的一个属性形成新的集合。
	 * @param items 原始集合
	 * @param property 要抽取的集合元素属性
	 * @return
	 */
	public static Collection<?> substract(Collection<?> items, String property) {
		if (items == null) {
			return null;
		}
		if (StringUtils.isEmpty(property)) {
			throw new IllegalArgumentException("property name must not empty!");
		}
		Collection<Object> results = new ArrayList<Object>();
		for (Object item : items) {
			Map<String, Object> propValues = BeanUtils.getPropValues(item);
			if (!propValues.containsKey(property)) {
				throw new IllegalArgumentException("Property " + property  + " not exists!");
			}
			results.add(propValues.get(property));
		}
		return results;
	}

	
	/**
	 * 抽取集合中每个元素的一个属性形成新的集合，然后用指定的分隔符连接起来形成一个字符串。
	 * @param items 原始集合
	 * @param property 要抽取的集合元素属性
	 * @param separator 字符串分隔符
	 * @return
	 */
	public static String join(Collection<?> items, String property, String separator) {
		if (items == null) {
			return "";
		}
		return StringUtils.join(substract(items, property), separator);
	}

	private CollectionUtils() {
		super();
	}
}
