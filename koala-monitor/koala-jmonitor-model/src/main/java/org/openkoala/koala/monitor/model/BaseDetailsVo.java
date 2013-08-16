package org.openkoala.koala.monitor.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class BaseDetailsVo {

	// 堆栈信息
	private String stackTracesDetails;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date beginTime;
	private String beginTimeStr;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	private String endTimeStr;

	private String system;
	
	/**
	 * 耗时（单位：毫秒）
	 */
	private long timeConsume;

	// 排序
	private String sortname;
	private String sortorder;

	public String getStackTracesDetails() {
		return stackTracesDetails;
	}

	public void setStackTracesDetails(String stackTracesDetails) {
		this.stackTracesDetails = stackTracesDetails;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public String getBeginTimeStr() {
		return beginTimeStr;
	}

	public long getTimeConsume() {
		return timeConsume;
	}

	public void setTimeConsume(long timeConsume) {
		this.timeConsume = timeConsume;
	}

	public void setBeginTimeStr(String beginTimeStr) {
		this.beginTimeStr = beginTimeStr;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

}
