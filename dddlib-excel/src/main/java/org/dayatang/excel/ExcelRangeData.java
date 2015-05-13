package org.dayatang.excel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 代表Excel中一块Range的数据。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 */
public class ExcelRangeData {
	private List<Object[]> data = new ArrayList<Object[]>();
	private Version version;
	private boolean isDate1904;

	/**
	 * 初始化
	 * @param data 数据
	 * @param version 版本（xls或xlsx）
	 * @param isDate1904 是否基于1904年的日期系统
	 */
	public ExcelRangeData(List<Object[]> data, Version version, boolean isDate1904) {
		super();
		this.data = data;
		this.version = version;
		this.isDate1904 = isDate1904;
	}

	/**
	 * 获取行数
	 * @return 数据行数
	 */
	public int getRowCount() {
		return data.size();
	}

	/**
	 * 获取列数
	 * @return 数据列数
	 */
	public int getColumnCount() {
		return data.isEmpty() ? 0 : data.get(0).length;
	}

	/**
	 * 获取指定行、指定列的数据，以double的形式表示
	 * @param row 行号
	 * @param column 列号
	 * @return
	 */
	public Double getDouble(int row, int column) {
		return ExcelUtils.getDouble(data.get(row)[column]);
	}

	/**
	 * 获取指定行、指定列的数据，以int的形式表示
	 * @param row 行号
	 * @param column 列号
	 * @return
	 */
	public Integer getInt(int row, int column) {
		return ExcelUtils.getInt(data.get(row)[column]);
	}

	/**
	 * 获取指定行、指定列的数据，以long的形式表示
	 * @param row 行号
	 * @param column 列号
	 * @return
	 */
	public Long getLong(int row, int column) {
		return ExcelUtils.getLong(data.get(row)[column]);
	}

	/**
	 * 获取指定行、指定列的数据，以boolean的形式表示
	 * @param row 行号
	 * @param column 列号
	 * @return
	 */
	public Boolean getBoolean(int row, int column) {
		return ExcelUtils.getBoolean(data.get(row)[column]);
	}

	/**
	 * 获取指定行、指定列的数据，以字符串的形式表示
	 * @param row 行号
	 * @param column 列号
	 * @return
	 */
	public String getString(int row, int column) {
		return ExcelUtils.getString(data.get(row)[column]);
	}

	/**
	 * 获取指定行、指定列的数据，以date的形式表示
	 * @param row 行号
	 * @param column 列号
	 * @return
	 */
	public Date getDate(int row, int column) {
		return ExcelUtils.getDate(data.get(row)[column], version, isDate1904);
	}

	/**
	 * 获取数据
	 * @return 数据
	 */
	public List<Object[]> getData() {
		return data;
	}
}
