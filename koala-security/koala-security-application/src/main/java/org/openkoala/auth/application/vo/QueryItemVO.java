package org.openkoala.auth.application.vo;

import java.io.Serializable;

public class QueryItemVO implements Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = -6615625925240665213L;
    private String propName;
	private int operaType;
	private String propValue;
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public int getOperaType() {
		return operaType;
	}
	public void setOperaType(int operaType) {
		this.operaType = operaType;
	}
	public String getPropValue() {
		return propValue;
	}
	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}
	
	
	
}
