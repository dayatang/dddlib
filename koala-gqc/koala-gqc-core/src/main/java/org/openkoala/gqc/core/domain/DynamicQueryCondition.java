package org.openkoala.gqc.core.domain;

import java.sql.Types;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import org.openkoala.gqc.core.domain.utils.SqlStatmentMode;

/**
 * 功能描述：动态查询条件	
 *  
 * 作    者：xmfang(xinmin.fang@gmail.com)
 */
@Embeddable
public class DynamicQueryCondition extends QueryCondition {

	private static final long serialVersionUID = 3341870089057147699L;
	
	/**
	 * 显示名称
	 */
	@Column(name = "LABEL")
	private String label;
	
	/**
	 * 控件类型
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "WIDGET_TYPE")
	private WidgetType widgetType;

	/**
	 * 查询值
	 */
	@Transient
	private String value;
	
	/**
	 * 查询开始值，用于区间查询
	 */
	@Transient
	private String startValue;
	
	/**
	 * 查询值结束值，用于区间查询
	 */
	@Transient
	private String endValue;
	
	public DynamicQueryCondition() {
		
	}
	
	public DynamicQueryCondition(String fieldName) {
		this.setFieldName(fieldName);
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public WidgetType getWidgetType() {
		return widgetType;
	}

	public void setWidgetType(WidgetType widgetType) {
		this.widgetType = widgetType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStartValue() {
		return startValue;
	}

	public void setStartValue(String startValue) {
		this.startValue = startValue;
	}

	public String getEndValue() {
		return endValue;
	}

	public void setEndValue(String endValue) {
		this.endValue = endValue;
	}

	@Override
    public void setFieldType(Integer fieldType) {
        super.setFieldType(fieldType);
        if ( getQueryOperation() == null ) { 
        	setQueryOperation(getDefaultByType(fieldType));
        }
	}
	
	private QueryOperation getDefaultByType(int fieldType) {
		if (fieldType == Types.BOOLEAN
			|| fieldType == Types.DOUBLE
			|| fieldType == Types.FLOAT
			|| fieldType == Types.INTEGER
			|| fieldType == Types.BIGINT
			|| fieldType == Types.DATE
			|| fieldType == Types.TIME
			|| fieldType == Types.TIMESTAMP
			|| fieldType == Types.DECIMAL
			|| fieldType == Types.ARRAY
			|| fieldType == Types.BINARY
			|| fieldType == Types.BIT
			|| fieldType == Types.TINYINT) {
			return QueryOperation.EQ;
		}
		return QueryOperation.LIKE;
	}

	@Override
	public SqlStatmentMode generateConditionStatment() {
		SqlStatmentMode result = new SqlStatmentMode();
    	StringBuilder statment = new StringBuilder("");
		
		if ((value != null && !value.isEmpty()) || (startValue != null && !startValue.isEmpty() && endValue != null && !endValue.isEmpty())) {
			statment.append(" and " + getFieldName() + " ");
			statment.append(getQueryOperation().getOperator() + " ");
			if (getQueryOperation().equals(QueryOperation.LIKE)) {
				statment.append("?");
				result.addValue("%" + value + "%");
			} else if (getQueryOperation().equals(QueryOperation.BETWEEN)) {
				statment.append("? and ?");
				result.addValue(startValue);
				result.addValue(endValue);
			} else {
				statment.append("?");
				result.addValue(value);
			}
		}
		
		result.setStatment(statment.toString());
    	return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getFieldName() == null) ? 0 : getFieldName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DynamicQueryCondition other = (DynamicQueryCondition) obj;
		if (getFieldName() == null) {
			if (other.getFieldName() != null)
				return false;
		} else if (!getFieldName().equals(other.getFieldName()))
			return false;
		return true;
	}
	
}
