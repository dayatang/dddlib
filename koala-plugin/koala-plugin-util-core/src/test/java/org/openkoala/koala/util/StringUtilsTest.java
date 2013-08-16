package org.openkoala.koala.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void handleSpecialCharactersInNameTest() {
		assertEquals("abc", StringUtils.handleSpecialCharactersInName("a_b-c"));
	}
	
	@Test
	public void generateJavaClassNameTest() {
		//字母开头的情况
		assertEquals("Abcd", StringUtils.generateJavaClass("1abcd"));
		
		//特殊字符开头的情况
		assertEquals("Abcd", StringUtils.generateJavaClass("@abcd"));
		
		//中间有特殊字符的情况
		assertEquals("Abcd", StringUtils.generateJavaClass("ab@cd"));

		//多个特殊字符的情况
		assertEquals("Abcd", StringUtils.generateJavaClass("ab@c#d"));

		//多个数字的情况
		assertEquals("Abcd", StringUtils.generateJavaClass("a3b2cd"));
		
		//判别下划线的情况
		assertEquals("AbCd", StringUtils.generateJavaClass("ab_cd"));

		//判别横杠的情况
		assertEquals("AbCd", StringUtils.generateJavaClass("ab-cd"));

		//数字，特殊字符，下划线和横杠同时存在的情况
		assertEquals("AbCD", StringUtils.generateJavaClass("a2b$-c_d"));
		
		//下划线在最前的情况
		assertEquals("ABcd", StringUtils.generateJavaClass("_a_b2cd"));
		
		//横杠在最后的情况
		assertEquals("Abcd", StringUtils.generateJavaClass("-%a_3b2cd_"));
		
		//下划线在最后的情况
		assertEquals("Abcd", StringUtils.generateJavaClass("1%a_3b2cd_"));
		
		//横杠在最后的情况
		assertEquals("Abcd", StringUtils.generateJavaClass("1%a_3b2cd@-"));
	}
	
	@Test
	public void generateJavaVariNameTest() {
		//字母开头的情况
		assertEquals("abcd", StringUtils.generateJavaVari("1abcd"));
		
		//特殊字符开头的情况
		assertEquals("abcd", StringUtils.generateJavaVari("@abcd"));
		
		//中间有特殊字符的情况
		assertEquals("abcd", StringUtils.generateJavaVari("ab@cd"));

		//多个特殊字符的情况
		assertEquals("abcd", StringUtils.generateJavaVari("ab@c#d"));

		//多个数字的情况
		assertEquals("abcd", StringUtils.generateJavaVari("a3b2cd"));
		
		//判别下划线情况
		assertEquals("abCd", StringUtils.generateJavaVari("ab_cd"));

		//判别横杠的情况
		assertEquals("abCd", StringUtils.generateJavaVari("ab-cd"));

		//数字，特殊字符，下划线和横杠同时存在的情况
		assertEquals("abCD", StringUtils.generateJavaVari("a2b$-c_d"));
		
		//下划线在最前的情况
		assertEquals("aBcd", StringUtils.generateJavaVari("_a_b2cd"));
		
		//横杠在最后的情况
		assertEquals("abcd", StringUtils.generateJavaVari("-%a_3b2cd_"));
		
		//下划线在最后的情况
		assertEquals("abcd", StringUtils.generateJavaVari("1%a_3b2cd_"));
		
		//横杠在最后的情况
		assertEquals("abcd", StringUtils.generateJavaVari("1%a_3b2cd@-"));
	}

}
