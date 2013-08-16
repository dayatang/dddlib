package org.jwebap.util;

import java.util.Collection;
import java.util.Map;

/**
 * 前置条件断言(引自Common Template,www.commontemplate.com)
 * 
 * @author liangfei0201@163.com
 *
 */
public final class Assert {
	
	private Assert(){}
	
	public static void fail() {
		fail("断言此处不会被运行到，但却运行了!");
	}
	
	public static void fail(String errorMessage) {
		throw new IllegalStateException(errorMessage);
	}
	
	public static void assertEquals(String value1, String value2) {
		assertEquals(value1, value2, "断言两值相等, 但" + value1 + "不等于" + value2 + "!");
	}
	
	public static void assertEquals(String value1, String value2, String errorMessage) {
		if (value1 == null || ! value1.equals(value2))
			throw new IllegalStateException(errorMessage);
	}
	
	public static void assertMatches(String value, String regex) {
		assertMatches(value, regex, "断言此值匹配表达式" + regex + ", 但" + value + "不匹配!");
	}
	
	public static void assertMatches(String value, String regex, String errorMessage) {
		if (! value.matches(regex))
			throw new IllegalStateException(errorMessage);
	}
	
	public static void assertAssignableFrom(Class targetClass, Class baseClass) {
		assertAssignableFrom(targetClass, baseClass, "断言" + targetClass.getName() + "继承于" + baseClass.getName() + ",但却不是!");
	}
	
	public static void assertAssignableFrom(Class targetClass, Class baseClass, String errorMessage) {
		if (! baseClass.isAssignableFrom(targetClass))
			throw new IllegalStateException(errorMessage);
	}
	
	public static void assertTrue(boolean value) {
		assertTrue(value, "断言此命题应该为真, 但却为假!");
	}
	
	public static void assertTrue(boolean value, String errorMessage) {
		if (! value)
			throw new IllegalStateException(errorMessage);
	}
	
	public static void assertFalse(boolean value) {
		assertFalse(value, "断言此命题应该为假, 但却为真!");
	}
	
	public static void assertFalse(boolean value, String errorMessage) {
		if (value)
			throw new IllegalStateException(errorMessage);
	}
	
	public static void assertNotNull(Object value) {
		assertNotNull(value, "断言此对象不为空, 但却为空!");
	}
	
	public static void assertNotNull(Object value, String errorMessage) {
		if (value == null)
			throw new NullPointerException(errorMessage);
	}
	
	public static void assertEmpty(Object value) {
		assertEmpty(value, "断言此对象为空, 但却不为空!");
	}
	
	public static void assertEmpty(Object value, String errorMessage) {
		if (value != null)
			throw new IllegalStateException(errorMessage);
	}
	
	public static void assertNotEmpty(Object value) {
		assertNotEmpty(value, "断言此对象不为空, 但却为空!");
	}
	
	public static void assertNotEmpty(Object value, String errorMessage) {
		if (value == null)
			throw new IllegalStateException(errorMessage);
	}
	
	public static void assertNotEmpty(String value) {
		assertNotEmpty(value, "断言此对象不为空, 但却为空!");
	}
	
	public static void assertNotEmpty(String value, String errorMessage) {
		if (value == null || value.trim().length() == 0)
			throw new IllegalStateException(errorMessage);
	}
	
	public static void assertNotEmpty(Collection value) {
		assertNotEmpty(value, "断言此对象不为空, 但却为空!");
	}
	
	public static void assertNotEmpty(Collection value, String errorMessage) {
		if (value == null || value.size() == 0)
			throw new IllegalStateException(errorMessage);
	}
	
	public static void assertNotEmpty(Map value) {
		assertNotEmpty(value, "断言此对象不为空, 但却为空!");
	}
	
	public static void assertNotEmpty(Map value, String errorMessage) {
		if (value == null || value.size() == 0)
			throw new IllegalArgumentException(errorMessage);
	}
	
	public static void assertNotEmpty(Object[] value) {
		assertNotEmpty(value, "断言此对象不为空, 但却为空!");
	}
	
	public static void assertNotEmpty(Object[] value, String errorMessage) {
		if (value == null || value.length == 0)
			throw new IllegalArgumentException(errorMessage);
	}
	
	public static void assertContain(String value, String sub) {
		assertContain(value, sub, "断言\"" + value + "\"将包含\"" + sub + "\"子串，但却不包含!");
	}
	
	public static void assertContain(String value, String sub, String errorMessage) {
		if (value == null || sub == null
				|| value.indexOf(sub) == -1) 
			throw new IllegalStateException(errorMessage);
	}
	
	public static void assertGreaterThan(int var, int value) {
		assertGreaterThan(var, value, "断言" + var + "应大于\"" + value + "\"!");
	}
	
	public static void assertGreaterThan(int var, int value, String errorMessage) {
		if (var <= value)
			throw new IllegalStateException(errorMessage);
	}
	
	public static void assertGreaterEqual(int var, int value) {
		assertGreaterEqual(var, value, "断言" + var + "应大于或等于\"" + value + "\"!");
	}
	
	public static void assertGreaterEqual(int var, int value, String errorMessage) {
		if (var < value)
			throw new IllegalStateException(errorMessage);
	}
}

