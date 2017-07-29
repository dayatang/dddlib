package org.dayatang.excel;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.utils.Assert;

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

	private ExcelCell(int sheetIndex, int rowIndex, int columnIndex) {
		this.sheetIndex = sheetIndex;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	public ExcelCell(String sheetName, int rowIndex, int columnIndex) {
		this.sheetName = sheetName;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
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

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		private int sheetIndex = 0;
		private String sheetName;
		private int rowIndex;
		private int columnIndex;

		public Builder sheet(int SheetIndex) {
			this.sheetIndex = sheetIndex;
			return this;
		}

		public Builder sheet(String sheetName) {
			this.sheetName = sheetName;
			return this;
		}

		public Builder row(int row) {
			this.rowIndex = row;
			return this;
		}

		public Builder column(int columnIndex) {
			this.columnIndex = columnIndex;
			return this;
		}

		public Builder column(String columnName) {
			this.columnIndex = ExcelUtils.convertColumnNameToIndex(columnName);
			return this;
		}

		public ExcelCell build() {
			if (StringUtils.isBlank(sheetName) && sheetIndex < 0) {
				throw new ExcelException("sheet name not defined, and sheet index < 0");
			}
			if (rowIndex < 0) {
				throw new ExcelException("row index must >= 0");
			}
			if (columnIndex < 0) {
				throw new ExcelException("column index must >= 0");
			}
			if (StringUtils.isBlank(sheetName)) {
				return new ExcelCell(sheetIndex, rowIndex, columnIndex);
			}
			return new ExcelCell(sheetName, rowIndex, columnIndex);
		}
	}
}