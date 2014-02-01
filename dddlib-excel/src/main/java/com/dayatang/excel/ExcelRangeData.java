package com.dayatang.excel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelRangeData {
	private List<Object[]> data = new ArrayList<Object[]>();
	private Version version;
	private boolean isDate1904;
	
	public ExcelRangeData(List<Object[]> data, Version version, boolean isDate1904) {
		super();
		this.data = data;
		this.version = version;
		this.isDate1904 = isDate1904;
	}
	
	public int getRowCount() {
		return data.size();
	}

	public int getColumnCount() {
		return data.isEmpty() ? 0 : data.get(0).length;
	}
	
	public Double getDouble(int row, int column) {
		return ExcelUtils.getDouble(data.get(row)[column]);
	}
	
	public Integer getInt(int row, int column) {
		return ExcelUtils.getInt(data.get(row)[column]);
	}
	
	public Long getLong(int row, int column) {
		return ExcelUtils.getLong(data.get(row)[column]);
	}
	
	public Boolean getBoolean(int row, int column) {
		return ExcelUtils.getBoolean(data.get(row)[column]);
	}
	
	public String getString(int row, int column) {
		return ExcelUtils.getString(data.get(row)[column]);
	}
	
	public Date getDate(int row, int column) {
		return ExcelUtils.getDate(data.get(row)[column], version, isDate1904);
	}

	public List<Object[]> getData() {
		return data;
	}
}
