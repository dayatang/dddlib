package org.openkoala.gqc.core.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.openkoala.gqc.core.domain.utils.SqlStatmentMode;

/**
 * 静态查询条件，即某个查询的先决条件	
 *  
 * 作    者：xmfang(xinmin.fang@gmail.com)
 */
@Embeddable
public class PreQueryCondition extends QueryCondition {

	private static final long serialVersionUID = 3027819613759885894L;

	/**
	 * 查询值
	 */
	@Column(name = "VALUE")
	private String value;
	
	/**
	 * 查询开始值，用于区间查询
	 */
	@Column(name = "START_VALUE")
	private String startValue;
	
	/**
	 * 查询值结束值，用于区间查询
	 */
	@Column(name = "END_VALUE")
	private String endValue;

	/**
	 * 该条件是否显示在查询页面上
	 */
	@Column(name = "VISIBLE")
	private Boolean visible = false;
	
	public PreQueryCondition() {
		
	}
	
	public PreQueryCondition(String fieldName) {
		this.setFieldName(fieldName);
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
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
		PreQueryCondition other = (PreQueryCondition) obj;
		if (getFieldName() == null) {
			if (other.getFieldName() != null)
				return false;
		} else if (!getFieldName().equals(other.getFieldName()))
			return false;
		return true;
	}
	
}
