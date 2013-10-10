package org.openkoala.gqc.core.domain.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL语句模式（可以是整条sql语句或者是条件语句）
 * @author xmfang
 *
 */
public class SqlStatmentMode {

	private String statment;
	
	private List<Object> values = new ArrayList<Object>();

	public String getStatment() {
		return statment;
	}

	public void setStatment(String statment) {
		this.statment = statment;
	}

	public List<Object> getValues() {
		return new ArrayList<Object>(values);
	}

	public void addValues(List<Object> values) {
		this.values.addAll(values);
	}

	public void addValue(Object value) {
		values.add(value);
	}
	
	public void removeValue(Object value) {
		values.remove(value);
	}
	
}
