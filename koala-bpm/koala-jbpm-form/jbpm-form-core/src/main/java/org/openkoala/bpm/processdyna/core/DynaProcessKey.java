package org.openkoala.bpm.processdyna.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.dayatang.domain.AbstractEntity;

/**
 * 流程表单自定义中的 KEY 表值
 * 
 * @author lingen
 * 
 */
@Entity
@Table(name = "DYNA_PROCESS_KEY")
public class DynaProcessKey extends AbstractEntity implements Comparable<DynaProcessKey>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8632362177641945740L;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "DYNA_ID")
	private DynaProcessForm dynaTable;

	@Column(name = "KEY_ID")
	private String keyId;

	@Column(name = "KEY_NAME")
	private String keyName;

	@Column(name = "KEY_TYPE")
	private String keyType;
	
	//值输出类型
	@Column(name = "VAL_OUTPUT_TYPE")
	private String valOutputType = "String";

	// 可选值
	@Column(name = "KEY_OPTS")
	private String keyOptions;

	/**
	 * 字段是否必填
	 */
	@Column(name = "REQUIRED")
	private boolean required;

	// 是否变量
	@Column(name = "IS_VARIABLE")
	private boolean innerVariable;

	/**
	 * 字段的检验类型
	 */
	@Column(name = "VALIDATION_TYPE")
	private String validationType;

	/**
	 * 字段的检验表达式
	 */
	@Column(name = "VALIDATION_EXPR")
	private String validationExpr;

	// 显示顺序
	@Column(name = "SHOW_ORDER")
	private int showOrder;

	// 是否显示在待办事项列表
	@Column(name = "IS_OUTPUT_VAR")
	private boolean outputVar;

	@Transient
	private String keyValueForShow = "";
	
	@Transient
	private String security = "W";

	public DynaProcessKey(String keyId, String keyName, String keyType) {
		super();
		this.keyId = keyId;
		this.keyName = keyName;
		this.keyType = keyType;
	}

	public DynaProcessKey() {
		super();
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	public DynaProcessForm getDynaTable() {
		return dynaTable;
	}

	public void setDynaTable(DynaProcessForm dynaTable) {
		this.dynaTable = dynaTable;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getKeyOptions() {
		if(keyOptions != null)return keyOptions.replaceAll("\"", "'");
		return "";
	}

	public void setKeyOptions(String keyOptions) {
		this.keyOptions = keyOptions;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getValidationType() {
		return StringUtils.trimToEmpty(validationType);
	}

	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}

	public String getValidationExpr() {
		return StringUtils.trimToEmpty(validationExpr);
	}

	public void setValidationExpr(String validationExpr) {
		this.validationExpr = validationExpr;
	}

	public boolean isInnerVariable() {
		return innerVariable;
	}

	public void setInnerVariable(boolean innerVariable) {
		this.innerVariable = innerVariable;
	}

	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

	public boolean isOutputVar() {
		return outputVar;
	}

	public void setOutputVar(boolean outputVar) {
		this.outputVar = outputVar;
	}

	public String getKeyValueForShow() {
		return keyValueForShow;
	}

	public void setKeyValueForShow(String keyValueForShow) {
		this.keyValueForShow = keyValueForShow;
	}
	
	public String getValOutputType() {
		return valOutputType;
	}

	public void setValOutputType(String valOutputType) {
		this.valOutputType = valOutputType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyId == null) ? 0 : keyId.hashCode());
		result = prime * result + ((keyName == null) ? 0 : keyName.hashCode());
		result = prime * result + ((keyType == null) ? 0 : keyType.hashCode());
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
		DynaProcessKey other = (DynaProcessKey) obj;
		if (keyId == null) {
			if (other.keyId != null)
				return false;
		} else if (!keyId.equals(other.keyId))
			return false;
		if (keyName == null) {
			if (other.keyName != null)
				return false;
		} else if (!keyName.equals(other.keyName))
			return false;
		if (keyType != other.keyType)
			return false;
		return true;
	}

	public String getWidget() {
		return "<@" + this.keyType + " keyId=\"" + keyId + "\" keyName=\""
				+ keyName + "\" keyType=\"" + keyType
				+ "\" security=\"" + security 
				+ "\" value=\"" + keyValueForShow 
				+ "\" validationType=\"" + getValidationType() 
				+ "\" validationExpr=\"" + getValidationExpr() 
				+ "\" keyOptions=\"" + getKeyOptions() + "\" />";
		
	}
	

	@Override
	public String toString() {
		return "DynaProcessKey [keyId=" + keyId + ", keyName=" + keyName
				+ ", keyType=" + keyType + ", showOrder=" + getShowOrder()
				+ "]";
	}

	public int compareTo(DynaProcessKey key) {
		if(this.getShowOrder() == key.getShowOrder()){
			return this.getKeyId().compareTo(key.getKeyId());
		}else{
			return this.getShowOrder() - key.getShowOrder();
		}
	}

}
