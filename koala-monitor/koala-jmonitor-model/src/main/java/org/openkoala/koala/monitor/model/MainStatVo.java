/*
 * Copyright (c) Koala 2012-2014 All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openkoala.koala.monitor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能描述：<br />
 * 
 * 创建日期：2013-6-7 上午9:53:34 <br />
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作 者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者 修改日期 文件版本 修改说明
 */
public class MainStatVo implements Serializable {

	private static final long serialVersionUID = -4332257257969974945L;

	private String traceId;

	private String principal;//

	private String clientId;

	private Date beginTime;
	private String beginTimeStr;

	private Date endTime;
	private String endTimeStr;

	private int year;

	private int month;

	private int day;

	private int hour;

	// 按小时/天/月查询
	private String unit;

	private List<HttpDetailsVo> httpDetails = new ArrayList<HttpDetailsVo>();

	private List<JdbcDetailsVo> jdbcDetails = new ArrayList<JdbcDetailsVo>();

	private List<MethodDetailsVo> methodDetails = new ArrayList<MethodDetailsVo>();

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getId() {
		return getTraceId();
	}

	public String getUnit() {
		return unit;
	}

	public String getBeginTimeStr() {
		return beginTimeStr;
	}

	public void setBeginTimeStr(String beginTimeStr) {
		this.beginTimeStr = beginTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public List<HttpDetailsVo> getHttpDetails() {
		return httpDetails;
	}

	public void setHttpDetails(List<HttpDetailsVo> httpDetails) {
		this.httpDetails = httpDetails;
	}

	public List<JdbcDetailsVo> getJdbcDetails() {
		return jdbcDetails;
	}

	public void setJdbcDetails(List<JdbcDetailsVo> jdbcDetails) {
		this.jdbcDetails = jdbcDetails;
	}

	public List<MethodDetailsVo> getMethodDetails() {
		return methodDetails;
	}

	public void setMethodDetails(List<MethodDetailsVo> methodDetails) {
		this.methodDetails = methodDetails;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
