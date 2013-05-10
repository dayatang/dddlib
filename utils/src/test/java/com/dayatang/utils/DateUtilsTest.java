package com.dayatang.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DateUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMaxDate() {
		Calendar date = Calendar.getInstance();
		date.setTime(DateUtils.MAX_DATE);
		assertEquals(8888, date.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, date.get(Calendar.MONTH));
		assertEquals(1, date.get(Calendar.DATE));
		assertEquals(0, date.get(Calendar.HOUR));
		assertEquals(0, date.get(Calendar.MINUTE));
		assertEquals(0, date.get(Calendar.SECOND));
		assertEquals(0, date.get(Calendar.MILLISECOND));
	}

	@Test
	public void testMinDate() {
		Calendar date = Calendar.getInstance();
		date.setTime(DateUtils.MIN_DATE);
		assertEquals(1000, date.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, date.get(Calendar.MONTH));
		assertEquals(1, date.get(Calendar.DATE));
		assertEquals(0, date.get(Calendar.HOUR));
		assertEquals(0, date.get(Calendar.MINUTE));
		assertEquals(0, date.get(Calendar.SECOND));
		assertEquals(0, date.get(Calendar.MILLISECOND));
	}

	@Test
	public void testDate() throws ParseException {
		assertEquals(DateUtils.date(1968, 4, 11), org.apache.commons.lang3.time.DateUtils.parseDate("1968-04-11", "yyyy-MM-dd"));
	}
	
	@Test
	public void getYearDiff() {
		assertEquals(2009 - 1968, DateUtils.getYearDiff(parseDate("1968-4-16"), parseDate("2009-5-1")));
		assertEquals(2009 - 1968 - 1, DateUtils.getYearDiff(parseDate("1968-4-16"), parseDate("2009-3-20")));
		assertEquals(2009 - 1968, DateUtils.getYearDiff(parseDate("1968-4-16"), parseDate("2009-4-17")));
		assertEquals(2009 - 1968, DateUtils.getYearDiff(parseDate("1968-4-16"), parseDate("2009-4-16")));
		assertEquals(2009 - 1968 - 1, DateUtils.getYearDiff(parseDate("1968-4-16"), parseDate("2009-4-15")));
	}

	@Test
	public void getMonthDiff() {
		assertEquals(0, DateUtils.getMonthDiff(parseDate("1968-4-16"), parseDate("1968-4-30")));
		assertEquals(0, DateUtils.getMonthDiff(parseDate("1968-4-16"), parseDate("1968-5-15")));
		assertEquals(1, DateUtils.getMonthDiff(parseDate("1968-4-16"), parseDate("1968-5-16")));
		assertEquals(1, DateUtils.getMonthDiff(parseDate("1968-4-16"), parseDate("1968-5-17")));
		assertEquals(2, DateUtils.getMonthDiff(parseDate("1968-4-16"), parseDate("1968-6-16")));
		
		assertEquals(24, DateUtils.getMonthDiff(parseDate("1968-4-16"), parseDate("1970-4-30")));
		assertEquals(24, DateUtils.getMonthDiff(parseDate("1968-4-16"), parseDate("1970-5-15")));
		assertEquals(25, DateUtils.getMonthDiff(parseDate("1968-4-16"), parseDate("1970-5-16")));
		assertEquals(25, DateUtils.getMonthDiff(parseDate("1968-4-16"), parseDate("1970-5-17")));
		assertEquals(26, DateUtils.getMonthDiff(parseDate("1968-4-16"), parseDate("1970-6-16")));
	}
	
	@Test
	public void getDayDiff() {
		assertEquals(31, DateUtils.getDayDiff(DateUtils.parseDate("2009-1-1"), DateUtils.parseDate("2009-2-1")));
		assertEquals(2, DateUtils.getDayDiff(DateUtils.parseDate("2009-1-31"), DateUtils.parseDate("2009-2-2")));
	}
	
	@Test
	public void getMinuteDiffByTime() {
		Date time1 = getTime("12:15");
		Date time2 = getTime("14:14");
		Date time3 = getTime("14:15");
		Date time4 = getTime("14:16");
		assertEquals(1, DateUtils.getMinuteDiffByTime(time1, time2));
		assertEquals(2, DateUtils.getMinuteDiffByTime(time1, time3));
		assertEquals(2, DateUtils.getMinuteDiffByTime(time1, time4));
	}

	private Date getTime(String timeString) {
		String[] ms = StringUtils.split(timeString, ":");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, Integer.parseInt(ms[0]));
		calendar.set(Calendar.SECOND, Integer.parseInt(ms[1]));
		return calendar.getTime();
	}

	@Test
	public void testGetPrevDate() {
		Date theDate = parseDate("1968-4-16");
		Date prevDate = parseDate("1968-4-15");
		assertEquals(prevDate, DateUtils.getPrevDay(theDate));
	}
	
	@Test
	public void testGetNextDate() {
		Date theDate = parseDate("1968-4-16");
		Date nextDate = parseDate("1968-4-17");
		assertEquals(nextDate, DateUtils.getNextDay(theDate));
	}
	
	private Date parseDate(String dateString) {
		try {
			return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, new String[] {"yyyy-M-d"});
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Date parseDateTime(String dateString) {
		try {
			return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, new String[] {"yyyy-M-d hh:mm:ss"});
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Test
	public void testIsDateAfter() {
		Date date1 = parseDateTime("2007-1-2 08:12:02");
		Date date2 = parseDateTime("2007-1-1 18:12:02");
		assertTrue(DateUtils.isDateAfter(date1, date2));
		date1 = parseDateTime("2007-1-1 09:12:02");
		date2 = parseDateTime("2007-1-1 08:12:02");
		assertFalse(DateUtils.isDateAfter(date1, date2));
	}
	
	@Test
	public void testIsDateBefore() {
		Date date1 = parseDateTime("2007-1-2 08:12:02");
		Date date2 = parseDateTime("2007-1-1 18:12:02");
		assertTrue(DateUtils.isDateBefore(date2, date1));
		date1 = parseDateTime("2007-1-1 09:12:02");
		date2 = parseDateTime("2007-1-1 08:12:02");
		assertFalse(DateUtils.isDateBefore(date2, date1));
	}
	
	@Test
	public void testIsTimeAfter() {
		Date date1 = parseDateTime("2007-1-1 18:12:02");
		Date date2 = parseDateTime("2007-1-2 08:12:02");
		assertTrue(DateUtils.isTimeAfter(date1, date2));
		date1 = parseDateTime("2007-1-2 08:12:02");
		date2 = parseDateTime("2007-1-1 08:12:02");
		assertFalse(DateUtils.isTimeAfter(date1, date2));
	}
	
	@Test
	public void testIsTimeBefore() {
		Date date1 = parseDateTime("2007-1-1 18:12:02");
		Date date2 = parseDateTime("2007-1-2 08:12:02");
		assertTrue(DateUtils.isTimeBefore(date2, date1));
		date1 = parseDateTime("2007-1-2 08:12:02");
		date2 = parseDateTime("2007-1-1 08:12:02");
		assertFalse(DateUtils.isTimeBefore(date2, date1));
	}

	@Test 
	public void getWeekDaysBetween() {
		Date fromDate = DateUtils.parseDate("2009-1-1");
		Date toDate = DateUtils.parseDate("2009-2-1");
		assertEquals(4, DateUtils.getWeekDaysBetween(fromDate, toDate, Calendar.SUNDAY));
		assertEquals(4, DateUtils.getWeekDaysBetween(fromDate, toDate, Calendar.MONDAY));
		assertEquals(4, DateUtils.getWeekDaysBetween(fromDate, toDate, Calendar.TUESDAY));
		assertEquals(4, DateUtils.getWeekDaysBetween(fromDate, toDate, Calendar.WEDNESDAY));
		assertEquals(5, DateUtils.getWeekDaysBetween(fromDate, toDate, Calendar.THURSDAY));
		assertEquals(5, DateUtils.getWeekDaysBetween(fromDate, toDate, Calendar.FRIDAY));
		assertEquals(5, DateUtils.getWeekDaysBetween(fromDate, toDate, Calendar.SATURDAY));
	}
	
	@Test
	public void getFirstWeekdayBetween() {
		Date fromDate = DateUtils.parseDate("2009-1-1");
		Date toDate = DateUtils.parseDate("2009-2-1");
		assertEquals(DateUtils.parseDate("2009-1-4"), DateUtils.getFirstWeekdayBetween(fromDate, toDate, Calendar.SUNDAY));
		assertEquals(DateUtils.parseDate("2009-1-5"), DateUtils.getFirstWeekdayBetween(fromDate, toDate, Calendar.MONDAY));
		assertEquals(DateUtils.parseDate("2009-1-6"), DateUtils.getFirstWeekdayBetween(fromDate, toDate, Calendar.TUESDAY));
		assertEquals(DateUtils.parseDate("2009-1-7"), DateUtils.getFirstWeekdayBetween(fromDate, toDate, Calendar.WEDNESDAY));
		assertEquals(DateUtils.parseDate("2009-1-1"), DateUtils.getFirstWeekdayBetween(fromDate, toDate, Calendar.THURSDAY));
		assertEquals(DateUtils.parseDate("2009-1-2"), DateUtils.getFirstWeekdayBetween(fromDate, toDate, Calendar.FRIDAY));
		assertEquals(DateUtils.parseDate("2009-1-3"), DateUtils.getFirstWeekdayBetween(fromDate, toDate, Calendar.SATURDAY));
	}
	
	@Test
	public void getDaysInYear() {
		assertEquals(366, DateUtils.getDaysInYear(2000));
		assertEquals(365, DateUtils.getDaysInYear(2009));
	}
	
	@Test
	public void getDaysInMonth() {
		assertEquals(29, DateUtils.getDaysInMonth(2000, Calendar.FEBRUARY));
		assertEquals(28, DateUtils.getDaysInMonth(2009, Calendar.FEBRUARY));
		assertEquals(31, DateUtils.getDaysInMonth(2009, Calendar.DECEMBER));
	}
	
	@Test
	public void testParseDate() {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(DateUtils.parseDate("1968-4-16"));
		assertEquals(1968, calendar.get(Calendar.YEAR));
		assertEquals(3, calendar.get(Calendar.MONTH));
		assertEquals(16, calendar.get(Calendar.DATE));

		calendar.setTime(DateUtils.parseDate("1968-04-16"));
		assertEquals(1968, calendar.get(Calendar.YEAR));
		assertEquals(3, calendar.get(Calendar.MONTH));
		assertEquals(16, calendar.get(Calendar.DATE));
	}
	
	@Test
	public void testParseTime() {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(DateUtils.parseTime("02:04:06"));
		assertEquals(2, calendar.get(Calendar.HOUR));
		assertEquals(4, calendar.get(Calendar.MINUTE));
		assertEquals(6, calendar.get(Calendar.SECOND));

		calendar.setTime(DateUtils.parseTime("2:4:6"));
		assertEquals(2, calendar.get(Calendar.HOUR));
		assertEquals(4, calendar.get(Calendar.MINUTE));
		assertEquals(6, calendar.get(Calendar.SECOND));
	}
	
	@Test
	public void testParseDateTime() {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(DateUtils.parseDateTime("1968-04-16 02:04:06"));
		assertEquals(1968, calendar.get(Calendar.YEAR));
		assertEquals(3, calendar.get(Calendar.MONTH));
		assertEquals(16, calendar.get(Calendar.DATE));
		assertEquals(2, calendar.get(Calendar.HOUR));
		assertEquals(4, calendar.get(Calendar.MINUTE));
		assertEquals(6, calendar.get(Calendar.SECOND));

		calendar.setTime(DateUtils.parseDateTime("1968-4-16 2:4:6"));
		assertEquals(1968, calendar.get(Calendar.YEAR));
		assertEquals(3, calendar.get(Calendar.MONTH));
		assertEquals(16, calendar.get(Calendar.DATE));
		assertEquals(2, calendar.get(Calendar.HOUR));
		assertEquals(4, calendar.get(Calendar.MINUTE));
		assertEquals(6, calendar.get(Calendar.SECOND));

		calendar.setTime(DateUtils.parseDateTime("1968-04-16 2:4:6"));
		assertEquals(1968, calendar.get(Calendar.YEAR));
		assertEquals(3, calendar.get(Calendar.MONTH));
		assertEquals(16, calendar.get(Calendar.DATE));
		assertEquals(2, calendar.get(Calendar.HOUR));
		assertEquals(4, calendar.get(Calendar.MINUTE));
		assertEquals(6, calendar.get(Calendar.SECOND));

		calendar.setTime(DateUtils.parseDateTime("1968-04-16 2:4:6"));
		assertEquals(1968, calendar.get(Calendar.YEAR));
		assertEquals(3, calendar.get(Calendar.MONTH));
		assertEquals(16, calendar.get(Calendar.DATE));
		assertEquals(2, calendar.get(Calendar.HOUR));
		assertEquals(4, calendar.get(Calendar.MINUTE));
		assertEquals(6, calendar.get(Calendar.SECOND));
	}
	
	@Test
	public void dateAfter() {
		Date origDate = DateUtils.parseDate("2000-01-02");
		assertEquals(DateUtils.parseDate("2000-01-05"), DateUtils.dateAfter(origDate, 3, Calendar.DATE));
		assertEquals(DateUtils.parseDate("2003-01-02"), DateUtils.dateAfter(origDate, 3, Calendar.YEAR));
	}
	
	@Test
	public void dateBefore() {
		Date origDate = DateUtils.parseDate("2000-01-05");
		assertEquals(DateUtils.parseDate("2000-01-02"), DateUtils.dateBefore(origDate, 3, Calendar.DATE));
		assertEquals(DateUtils.parseDate("1997-01-05"), DateUtils.dateBefore(origDate, 3, Calendar.YEAR));
	}
}

