package com.dayatang.domain;


import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.ParseException;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataTypeTest {
	
	private final String[] DATE_FORMAT = new String[] {
		"yyyy-MM-dd",
		"hh:mm:ss",
		"yyyy-MM-dd hh:mm:ss"
	};

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetDefaultValue() throws ParseException {
		assertEquals("", DataType.STRING.getDefaultValue());
		assertEquals(0, DataType.INT.getDefaultValue());
		assertEquals(0.0, DataType.DOUBLE.getDefaultValue());
		assertEquals(BigDecimal.ZERO, DataType.BIG_DECIMAL.getDefaultValue());
		assertEquals(false, DataType.BOOLEAN.getDefaultValue());
		assertEquals(DateUtils.parseDate("1000-01-01", DATE_FORMAT), DataType.DATE.getDefaultValue());
		assertEquals(DateUtils.parseDate("00:00:00", DATE_FORMAT), DataType.TIME.getDefaultValue());
		assertEquals(DateUtils.parseDate("1000-01-01 00:00:00", DATE_FORMAT), DataType.DATE_TIME.getDefaultValue());
	}

	@Test
	public void testGetRealValue() throws ParseException {
		assertEquals("abc", DataType.STRING.getRealValue("abc"));
		assertEquals(12, DataType.INT.getRealValue("12"));
		assertEquals(12.5, DataType.DOUBLE.getRealValue("12.5"));
		assertEquals(BigDecimal.valueOf(12.5), DataType.BIG_DECIMAL.getRealValue("12.5"));
		assertEquals(true, DataType.BOOLEAN.getRealValue("true"));
		assertEquals(DateUtils.parseDate("2000-01-01", DATE_FORMAT), DataType.DATE.getRealValue("2000-01-01"));
		assertEquals(DateUtils.parseDate("00:12:00", DATE_FORMAT), DataType.TIME.getRealValue("00:12:00"));
		assertEquals(DateUtils.parseDate("2000-01-01 00:12:00", DATE_FORMAT), DataType.DATE_TIME.getRealValue("2000-01-01 00:12:00"));
	}
}
