package com.dayatang.utils;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DateRangeTest {
	
	private DateRange range;

	@Before
	public void setUp() throws Exception {
		range = new DateRange(
				DateUtils.parseDate("2007-12-5 11:29:15", new String[] {"yyyy-M-d hh:mm:ss"}), 
				DateUtils.parseDate("2007-12-15 11:11:15", new String[] {"yyyy-M-d hh:mm:ss"}));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testContains() throws ParseException {
		Date date = DateUtils.parseDate("2007-12-05 04:10:15", new String[] {"yyyy-M-d hh:mm:ss"});
		assertTrue(range.contains(date));
		date = DateUtils.parseDate("2007-12-15 23:10:15", new String[] {"yyyy-M-d hh:mm:ss"});
		assertTrue(range.contains(date));
		date = DateUtils.parseDate("2007-12-10 23:10:15", new String[] {"yyyy-M-d hh:mm:ss"});
		assertTrue(range.contains(date));
		date = DateUtils.parseDate("2007-11-10 23:10:15", new String[] {"yyyy-M-d hh:mm:ss"});
		assertFalse(range.contains(date));
		date = DateUtils.parseDate("2008-11-10 23:10:15", new String[] {"yyyy-M-d hh:mm:ss"});
		assertFalse(range.contains(date));
	}
	
	@Test
	public void testEqualsObject() throws ParseException {
		DateRange range1 = new DateRange(
				DateUtils.parseDate("2007-12-05 04:10:15", new String[] {"yyyy-M-d hh:mm:ss"}), 
				DateUtils.parseDate("2007-12-15 23:11:15", new String[] {"yyyy-M-d hh:mm:ss"}));
		assertEquals(range1, range);
		range1 = new DateRange(
				DateUtils.parseDate("2007-12-6 11:29:15", new String[] {"yyyy-M-d hh:mm:ss"}), 
				DateUtils.parseDate("2007-12-15 11:11:15", new String[] {"yyyy-M-d hh:mm:ss"}));
		assertFalse(range1.equals(range));
		range1 = new DateRange(
				DateUtils.parseDate("2007-12-5 11:29:15", new String[] {"yyyy-M-d hh:mm:ss"}), 
				DateUtils.parseDate("2007-12-16 11:11:15", new String[] {"yyyy-M-d hh:mm:ss"}));
		assertFalse(range1.equals(range));
	}

	@Test
	public void testToString() throws ParseException {
		Date from = DateUtils.parseDate("2007-12-05 04:10:15", new String[] {"yyyy-M-d hh:mm:ss"});
		Date to = DateUtils.parseDate("2007-12-15 23:10:15", new String[] {"yyyy-M-d hh:mm:ss"});
		String fromString = DateFormat.getDateInstance().format(from);
		String toString = DateFormat.getDateInstance().format(to);
		assertEquals("[" + fromString + " - " + toString + "]", range.toString());
	}

}
