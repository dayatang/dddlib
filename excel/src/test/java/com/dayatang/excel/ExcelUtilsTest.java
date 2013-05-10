package com.dayatang.excel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExcelUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConvertColumnNameToIndex() {
		assertEquals(0, ExcelUtils.convertColumnNameToIndex("A"));
		assertEquals(25, ExcelUtils.convertColumnNameToIndex("Z"));
		assertEquals(26, ExcelUtils.convertColumnNameToIndex("AA"));
		assertEquals(52, ExcelUtils.convertColumnNameToIndex("BA"));
		assertEquals(26 * 26 + 2 * 26 + 1, ExcelUtils.convertColumnNameToIndex("ABB"));
	}

	@Test
	public void testGetDouble() {
		assertNull(ExcelUtils.getDouble(null));
		assertEquals(10.5, ExcelUtils.getDouble(10.5), 0.001);
	}

	@Test
	public void testGetInt() {
		assertNull(ExcelUtils.getInt(null));
		assertEquals(10, ExcelUtils.getInt(10.0).intValue());
	}

	@Test
	public void testGetLong() {
		assertNull(ExcelUtils.getLong(null));
		assertEquals(10L, ExcelUtils.getLong(10.0).longValue());
	}

	@Test
	public void testGetBoolean() {
		assertNull(ExcelUtils.getBoolean(null));
		assertTrue(ExcelUtils.getBoolean(true).booleanValue());
	}

	@Test
	public void testGetString() {
		assertNull(ExcelUtils.getString(null));
		assertNull(ExcelUtils.getString(" "));
		assertEquals("abc", ExcelUtils.getString("abc"));
	}

	@Test
	public void testGetDate() {
		assertNull(ExcelUtils.getDate(null, Version.XLS, true));
		//assertTrue(ExcelUtils.getDate(true));
	}

}
