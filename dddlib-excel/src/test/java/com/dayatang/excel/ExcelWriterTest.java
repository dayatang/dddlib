package com.dayatang.excel;

import static org.junit.Assert.*;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

public class ExcelWriterTest {

	private File outputFile;
	private File inputFile;
	private ExcelHandler instance;
	
	@Before
	public void setUp() throws Exception {
		String inputFileName = getClass().getResource("/import.xls").getFile();
		inputFile = new File(inputFileName);
		instance = new ExcelHandler(inputFile);
		String outFileName = getClass().getResource("/export.xls").getFile();
		outputFile = new File(outFileName);
	}

	@Test
	public void testExportDataRange() throws Exception {
		List<Object[]> data = createData();
		ExcelCell excelCell = ExcelCell.sheetName("Company").row(0).column(0);
		instance.writeRange(excelCell, data);
		instance.outputTo(outputFile);
		ExcelRange range = ExcelRange.sheetName("Company").rowFrom(0).columnRange(0, 6);
		instance = new ExcelHandler(outputFile);
		ExcelRangeData results = instance.readRange(range);
		assertEquals("编号", results.getString(0, 0));
		assertEquals("公司", results.getString(0, 1));
		assertEquals("创建日期", results.getString(0, 2));
		assertEquals("撤销日期", results.getString(0, 3));
		assertEquals("排序号", results.getString(0, 4));
		assertEquals("上级机构", results.getString(0, 5));
		assertEquals("已撤销", results.getString(0, 6));
		
		assertEquals("suilink", results.getString(1, 0));
		assertEquals("穗灵公司", results.getString(1, 1));
		assertTrue(DateUtils.isSameDay(results.getDate(1, 2), parseDate(2002, 7, 1)));
		assertTrue(DateUtils.isSameDay(results.getDate(1, 3), parseDate(8888, 1, 1)));
		assertEquals(1, results.getInt(1, 4).intValue());
		System.out.println();
		assertNull(results.getString(1, 5));
		assertFalse(results.getBoolean(1, 6));
		
		assertEquals("dayatang", results.getString(2, 0));
		assertEquals("大雅堂公司", results.getString(2, 1));
		assertTrue(DateUtils.isSameDay(results.getDate(2, 2), parseDate(2004, 10, 1)));
		assertTrue(DateUtils.isSameDay(results.getDate(2, 3), parseDate(8888, 1, 1)));
		assertEquals(2, results.getInt(2, 4).intValue());
		assertEquals("suilink", results.getString(2, 5));
		assertTrue(results.getBoolean(2, 6));
	}


	private List<Object[]> createData() {
		List<Object[]> results = new ArrayList<Object[]>();
		results.add(new Object[] {"编号", "公司", "创建日期", "撤销日期", "排序号", "上级机构", "已撤销"});
		results.add(new Object[] {"suilink", "穗灵公司", createDate("2002-7-1"), createDate("8888-1-1"), 1, null, false});
		results.add(new Object[] {"dayatang", "大雅堂公司", createDate("2004-10-1"), createDate("8888-1-1"), 2, "suilink", true});
		return results;
	}

	private Object createDate(String value) {
		try {
			return DateUtils.parseDate(value, new String[] {
					"yyyy-MM-dd",
					"yyyy-M-d",
					"yy-MM-dd",
					"yy-M-d"
			});
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Date parseDate(int year, int month, int date) {
		Calendar result = Calendar.getInstance();
		result.set(year, month - 1, date);
		return result.getTime();
	}

	@Test
	public void testExportDataCell() throws Exception {
		ExcelCell excelCell = ExcelCell.sheetName("Company").row(2).column(1);
		instance.writeCell(excelCell, parseDate(2012, 2, 5));
		instance.outputTo(outputFile);
		instance = new ExcelHandler(outputFile);
		assertTrue(DateUtils.isSameDay(instance.readDate(excelCell), parseDate(2012, 2, 5)));
	}

}
