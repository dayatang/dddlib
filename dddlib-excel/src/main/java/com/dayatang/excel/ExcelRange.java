package com.dayatang.excel;

import com.dayatang.utils.Assert;

/**
 * Excel工作表范围，代表指定工作表中的一个矩形区域
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class ExcelRange {
	private int sheetIndex = 0;
	private String sheetName;
	private int rowFrom;
	private int rowTo = -1;
	private int columnFrom;
	private int columnTo;
	
	public static ExcelRange sheetIndex(int sheetIndex) {
		return new ExcelRange(sheetIndex);
	}
	
	private ExcelRange(int sheetIndex) {
		if (sheetIndex < 0) {
			throw new IllegalArgumentException("Sheet index cannot less than 0!");
		}
		this.sheetIndex = sheetIndex;
	}
	
	public static ExcelRange sheetName(String sheetName) {
		return new ExcelRange(sheetName);
	}

	private ExcelRange(String sheetName) {
		Assert.notBlank(sheetName, "Sheet name cannot be null or blank!");
		this.sheetName = sheetName;
		this.sheetIndex = -1;
	}

	public int getSheetIndex() {
		return sheetIndex;
	}

	public String getSheetName() {
		return sheetName;
	}

	public int getRowFrom() {
		return rowFrom;
	}

	public int getRowTo() {
		return rowTo;
	}

	public int getColumnFrom() {
		return columnFrom;
	}

	public int getColumnTo() {
		return columnTo;
	}

	public ExcelRange rowFrom(int rowFrom) {
		this.rowFrom = rowFrom;
		return this;
	}
	
	public ExcelRange rowTo(int rowTo) {
		this.rowTo = rowTo;
		return this;
	}
	
	public ExcelRange rowRange(int rowFrom, int rowTo) {
		if (rowTo < rowFrom) {
			throw new IllegalArgumentException("Last row is less than first row!");
		}
		this.rowFrom = rowFrom;
		this.rowTo = rowTo;
		return this;
		
	}

	public ExcelRange columnRange(int columnFrom, int columnTo) {
		if (columnTo < columnFrom) {
			throw new IllegalArgumentException("Last column is less than first column!");
		}
		this.columnFrom = columnFrom;
		this.columnTo = columnTo;
		return this;
	}

	public ExcelRange columnRange(String columnFrom, String columnTo) {
		return columnRange(convertColumnLabelToIndex(columnFrom), convertColumnLabelToIndex(columnTo));
	}

	private int convertColumnLabelToIndex(String columnLabel) {
		if (columnLabel.length() > 2) {
			throw new IllegalArgumentException("Column index too large!");
		}
		String theColumn = columnLabel.toUpperCase();
		if (theColumn.length() == 1) {
			int letter = theColumn.charAt(0);
			return letter - 65;
		}
		int firstLetter = theColumn.charAt(0);
		int lastLetter = theColumn.charAt(1);
		return (firstLetter - 64) * 26 + lastLetter - 65;
	}
}