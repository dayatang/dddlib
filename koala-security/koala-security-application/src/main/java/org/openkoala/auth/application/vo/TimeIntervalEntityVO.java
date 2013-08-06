package org.openkoala.auth.application.vo;

import java.io.Serializable;

public class TimeIntervalEntityVO extends AbstractEntityVO implements Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 1847082899082428859L;
    private String createDate;
	private String abolishDate;
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getAbolishDate() {
		return abolishDate;
	}
	public void setAbolishDate(String abolishDate) {
		this.abolishDate = abolishDate;
	}
	
}
