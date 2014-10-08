package org.dayatang.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 数组工具
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 */
public class ArrayUtils {

    private ArrayUtils() {
    }

    /**
     * 抽取数组中每个元素的一个属性值形成新的数组。
     *
     * @param items    原始数组
     * @param property 要抽取的数组元素属性
     * @return 由原始数组每个元素的一个指定属性的值组成的数组
     */
    public static Object[] substract(Object[] items, String property) {
        Assert.notNull(items);
        Assert.notBlank(property, "property name must not empty!");
        if (items.length == 0) {
            return new Object[0];
        }
        Object[] results = new Object[items.length];
        for (int i = 0; i < items.length; i++) {
            Object item = items[i];
            Map<String, Object> propValues = new BeanUtils(item).getPropValues();
            if (!propValues.containsKey(property)) {
                throw new IllegalArgumentException("Property " + property + " not exists!");
            }
            results[i] = propValues.get(property);
        }
        return results;
    }

    /**
     * 抽取数组中每个元素的一个属性形成新的数组，然后用指定的分隔符连接起来形成一个字符串。
     *
     * @param items     原始数组
     * @param property  要抽取的数组元素属性
     * @param separator 字符串分隔符
     * @return 由原始数组每个元素的指定属性的值按指定的分隔符连接起来形成的一个字符串。
     */
    public static String join(Object[] items, String property, String separator) {
        if (items == null || items.length == 0) {
            return "";
        }
        return StringUtils.join(substract(items, property), separator);
    }
}
