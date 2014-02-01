package com.dayatang.excel;

import com.dayatang.utils.Assert;

/**
 * Excel单元格
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class ExcelCell {
	private int sheetIndex = 0;
	private String sheetName;
	private int rowIndex;
	private int columnIndex;
	
	/**
	 * 指定工作表序号，生成ExcelCell实例
	 * @param sheetIndex
	 * @return
	 */
	public static ExcelCell sheetIndex(int sheetIndex) {
		return new ExcelCell(sheetIndex);
	}
	
	private ExcelCell(int sheetIndex) {
		if (sheetIndex < 0) {
			throw new IllegalArgumentException("Sheet index cannot less than 0!");
		}
		this.sheetIndex = sheetIndex;
	}
	
	/**
	 * 指定工作表名称，生成ExcelCell实例
	 * @param sheetName
	 * @return
	 */
	public static ExcelCell sheetName(String sheetName) {
		return new ExcelCell(sheetName);
	}

	private ExcelCell(String sheetName) {
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

	public int getRowIndex() {
		return rowIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public ExcelCell rowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
		return this;
	}
	
	public ExcelCell row(int rowIndex) {
		this.rowIndex = rowIndex;
		return this;
		
	}

	public ExcelCell column(int columnIndex) {
		this.columnIndex = columnIndex;
		return this;
	}

	public ExcelCell column(String columnName) {
		return column(ExcelUtils.convertColumnNameToIndex(columnName));
	}
}