package org.openkoala.bpm.application.dto;

import java.io.Serializable;
import java.util.List;

import org.openkoala.bpm.processdyna.core.DynaType;
import org.openkoala.bpm.processdyna.core.FieldType;
import org.openkoala.bpm.processdyna.core.ValidateRule;

public class DynaProcessKeyDTO implements Serializable {
	private static final long serialVersionUID = 1314875000055721456L;
	private String keyId;
	private String keyName;
	private String keyType;
	private String required;
	private String innerVariable;
	private String validationType;
	private String validationExpr;
	private int showOrder;
	private String outputVar;
	private String keyOptions;
	private String valOutputType;
	private String security = "W";
	private List<DynaProcessValueDTO> vals;

	private String keyValueForShow = "";

	public DynaProcessKeyDTO() {
	}

	public DynaProcessKeyDTO(String keyId, String keyName, String keyType) {
		super();
		this.keyId = keyId;
		this.keyName = keyName;
		this.keyType = keyType;
	}

	public String getKeyValueForShow() {
		return keyValueForShow;
	}

	public void setKeyValueForShow(String keyValueForShow) {
		this.keyValueForShow = keyValueForShow;
	}

	public String getKeyId() {
		return this.keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getKeyName() {
		return this.keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyType() {
		return this.keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getInnerVariable() {
		return innerVariable;
	}

	public void setInnerVariable(String innerVariable) {
		this.innerVariable = innerVariable;
	}

	public String getValidationType() {
		return validationType;
	}

	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}

	public String getValidationExpr() {
		return validationExpr;
	}

	public void setValidationExpr(String validationExpr) {
		this.validationExpr = validationExpr;
	}

	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

	public String getOutputVar() {
		return outputVar;
	}

	public void setOutputVar(String outputVar) {
		this.outputVar = outputVar;
	}

	public String getKeyOptions() {
		return keyOptions;
	}

	public void setKeyOptions(String keyOptions) {
		this.keyOptions = keyOptions;
	}

	public String getFieldTypeText() {
		if (keyType == null)
			return "";
		DynaType name2Enum = DynaType.name2Enum(keyType);
		return name2Enum == null ? "" : name2Enum.getText();
	}

	public String getValidateRuleText() {
		if (validationType == null)
			return "";
		ValidateRule name2Enum = ValidateRule.name2Enum(validationType);
		return name2Enum == null ? "" : name2Enum.getText();
	}
	
	public String getValOutputTypeText() {
		if (valOutputType == null)
			return "";
		FieldType name2Enum = FieldType.name2Enum(valOutputType);
		return name2Enum == null ? "" : name2Enum.getText();
	}
	

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	public List<DynaProcessValueDTO> getVals() {
		return vals;
	}

	public void setVals(List<DynaProcessValueDTO> vals) {
		this.vals = vals;
	}
	
	public String getValOutputType() {
		if(valOutputType == null)return "String";
		return valOutputType;
	}

	public void setValOutputType(String valOutputType) {
		this.valOutputType = valOutputType;
	}

	/**
	 * 是否包含默认值选项
	 * 
	 * @return
	 */
	public boolean getHasDefaultOpts() {
		return DynaType.Checkbox.name().equals(keyType)
				|| DynaType.Radio.name().equals(keyType)
				|| DynaType.DropDown.name().equals(keyType);
	}
}
