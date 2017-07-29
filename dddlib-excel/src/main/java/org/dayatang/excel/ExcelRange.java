package org.dayatang.excel;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.utils.Assert;

/**
 * Excel工作表范围，代表指定工作表中的一个矩形区域，行从rowFrom到rowTo，列从columnFrom到columnTo。行和列都是从0开始，即0代表第一行/列
 * 如果没指定rowTo，则从rowFrom开始往下读，一直到遇到第一个空行为止。
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

	private ExcelRange(int sheetIndex, int rowFrom, int rowTo, int columnFrom, int columnTo) {
		this.sheetIndex = sheetIndex;
		this.rowFrom = rowFrom;
		this.rowTo = rowTo;
		this.columnFrom = columnFrom;
		this.columnTo = columnTo;
	}

	private ExcelRange(String sheetName, int rowFrom, int rowTo, int columnFrom, int columnTo) {
		this.sheetName = sheetName;
		this.rowFrom = rowFrom;
		this.rowTo = rowTo;
		this.columnFrom = columnFrom;
		this.columnTo = columnTo;
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

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		private int sheetIndex = 0;
		private String sheetName;
		private int rowFrom;
		private int rowTo = -1;
		private int columnFrom;
		private int columnTo;

		public Builder sheet(int SheetIndex) {
			this.sheetIndex = sheetIndex;
			return this;
		}

		public Builder sheet(String sheetName) {
			this.sheetName = sheetName;
			return this;
		}
		
		public Builder rowFrom(int row) {
			this.rowFrom = row;
			return this;
		}
		
		public Builder rowTo(int row) {
			this.rowTo = row;
			return this;
		}
		
		public Builder columnFrom(int column) {
			this.columnFrom = column;
			return this;
		}
		
		public Builder columnFrom(String columnName) {
			this.columnFrom = ExcelUtils.convertColumnNameToIndex(columnName);
			return this;
		}

		public Builder columnTo(int column) {
			this.columnTo = column;
			return this;
		}

		public Builder columnTo(String columnName) {
			this.columnTo = ExcelUtils.convertColumnNameToIndex(columnName);
			return this;
		}

		public Builder rowRange(int rowFrom, int rowTo) {
			this.rowFrom = rowFrom;
			this.rowTo = rowTo;
			return this;

		}

		public Builder columnRange(int columnFrom, int columnTo) {
			this.columnFrom = columnFrom;
			this.columnTo = columnTo;
			return this;
		}

		public Builder columnRange(String columnFrom, String columnTo) {
			return columnRange(ExcelUtils.convertColumnNameToIndex(columnFrom), ExcelUtils.convertColumnNameToIndex(columnTo));
		}

		public ExcelRange build() {
			if (StringUtils.isBlank(sheetName) && sheetIndex < 0) {
				throw new ExcelException("sheet name not defined, and sheet index < 0");
			}
			if (rowFrom < 0) {
				throw new ExcelException("rowFrom must >= 0");
			}
//			if (rowTo < 0) {
//				throw new ExcelException("rowTo must >= 0");
//			}
			if (columnFrom < 0) {
				throw new ExcelException("columnFrom must >= 0");
			}
			if (columnTo < 0) {
				throw new ExcelException("columnTo must >= 0");
			}
//			if (rowTo < rowFrom) {
//				System.out.println("rowFrom: " + rowFrom);
//				System.out.println("rowTo:" + rowTo);
//				throw new IllegalArgumentException("Last row is less than first row!");
//			}
			if (columnTo < columnFrom) {
				throw new IllegalArgumentException("Last column is less than first column!");
			}
			if (StringUtils.isBlank(sheetName)) {
				return new ExcelRange(sheetIndex, rowFrom, rowTo, columnFrom, columnTo);
			}
			return new ExcelRange(sheetName, rowFrom, rowTo, columnFrom, columnTo);
		}
		
	}
}