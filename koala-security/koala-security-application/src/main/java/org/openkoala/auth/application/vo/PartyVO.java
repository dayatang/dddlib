package org.openkoala.auth.application.vo;

import java.io.Serializable;

public class PartyVO extends TimeIntervalEntityVO implements Serializable{
	/**
     * 
     */
    private static final long serialVersionUID = -3629660836674692143L;
    private String name;
	private String serialNumber;
	private int sortOrder;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

}
