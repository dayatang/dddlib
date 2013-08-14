package com.dayatang.excel;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.dayatang.utils.Assert;

/**
 * Excel工具类
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class ExcelUtils {

	private ExcelUtils() {
	}

	/**
	 * 将列名转换为列索引，例如将列"A"转换为0
	 * @param columnName 要转换的列名
	 * @return 参数columnName代表的列的索引
	 */
	public static int convertColumnNameToIndex(String columnName) {
		Assert.notBlank(columnName);
		String theColumn = columnName.toUpperCase();
		int length = theColumn.length();
		int result = letterToInt(theColumn.charAt(length - 1));
		if (length == 1) {
			return result;
		}
		for (int i = 1; i < length; i++) {
			int letter = theColumn.charAt(length - i - 1);
			result = (letterToInt(letter) + 1) * ((int) Math.pow(26, i)) + result;
		}
		return result;
	}
	
	private static int letterToInt(int letter) {
		return letter - 65;
	}
	
	public static Double getDouble(Object data) {
		if (data == null) {
			return null;
		}
		if (! (data instanceof Double)) {
			throw new IllegalStateException("数据类型错误：单元格中的数据不是数值类型");
		}
		return (Double) data;
	}
	
	public static Integer getInt(Object data) {
		Double value = getDouble(data);
		return value == null ? null : value.intValue();
	}
	
	public static Long getLong(Object data) {
		Double value = getDouble(data);
		return value == null ? null : value.longValue();
	}
	
	public static Boolean getBoolean(Object data) {
		if (data == null) {
			return null;
		}
		if (! (data instanceof Boolean)) {
			throw new IllegalStateException("数据类型错误：单元格中的数据不是布尔类型");
		}
		return (Boolean) data;
	}
	
	public static String getString(Object data) {
		if (data == null) {
			return null;
		}
		if (StringUtils.isBlank(data.toString())) {
			return null;
		}
		return data.toString();
	}
	
	public static Date getDate(Object data, Version version, boolean isDate1904) {
		Double value = getDouble(data);
		return version.getDate(value, isDate1904);
	}
	
}
