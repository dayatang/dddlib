package org.openkoala.koala.deploy.db2entity.vo;

import java.io.Serializable;
import java.sql.Types;

import org.openkoala.koala.util.StringUtils;

/**
 * 表格中列的信息
 * 
 * @author lingen
 * 
 */
public class DBColumnVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5623738314732064246L;

	/**
	 * 列名
	 */
	protected String columnName;

	/**
	 * 列的类型
	 */
	protected int dataType;

	/**
	 * 列的类型
	 */
	protected String typeName;

	/**
	 * 此列的描述
	 */
	protected String remarks;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getName() {
		return StringUtils.generateJavaVari(columnName);
	}

	public String getType() {
		switch (this.dataType) {
		case Types.CHAR:
			return "String";
		case Types.VARCHAR:
			return "String";
		case Types.LONGVARCHAR:
			return "String";
		case Types.BINARY:
			return "byte[]";
		case Types.LONGVARBINARY:
			return "byte[]";
		case Types.VARBINARY:
			return "byte[]";
		case Types.BIT:
			return "Boolean";
		case Types.BOOLEAN:
			return "Boolean";
		case Types.TINYINT:
			return "short";
		case Types.SMALLINT:
			return "short";
		case Types.INTEGER:
			return "int";
		case Types.BIGINT:
			return "Long";
		case Types.REAL:
			return "float";
		case Types.DOUBLE:
			return "double";
		case Types.FLOAT:
			return "double";
		case Types.DECIMAL:
			return "BigDecimal";
		case Types.NUMERIC:
			return "BigDecimal";
		case Types.DATE:
			return "Date";
		case Types.TIME:
			return "Date";
		case Types.TIMESTAMP:
			return "Timestamp";
		case Types.CLOB:
			return "String";
		case Types.BLOB:
			return "String";
		}
		return null;
	}

	@Override
	public String toString() {
		return "DBColumnVO [columnName=" + columnName + ", dataType="
				+ dataType + ", typeName=" + typeName + ", remarks=" + remarks
				+ "]";
	}
	
}
