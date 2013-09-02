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
package org.openkoala.koala.monitor.domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.openkoala.koala.base.KmBaseLegacyEntity;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-6-7 上午9:53:34  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
@Entity
@Table(name = "K_M_MAIN_STAT")
public class MainStat extends KmBaseLegacyEntity {

	private static final long serialVersionUID = -4332257257969974945L;

	@Id
	@Column(name="THREAD_KEY")
	protected String threadKey;
	
	@ManyToOne(targetEntity = MonitorNode.class)
	@JoinColumn(name="FK_NODE_ID")
	private MonitorNode belongNode;
	
	@Column(name="BEGIN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date beginTime;
	
	@Column(name="END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	
	@Column(name="YEAR")
	private int year;
	
	@Column(name="MONTH")
	private int month;
	
	@Column(name="DAY")
	private int day;
	
	@Column(name="HOUR")
	private int hour;
	

	public MainStat() {}
	
	public MainStat(String clientId,Date begin) {
		this.beginTime = begin;
		this.belongNode = MonitorNode.getAllNodesCache().get(clientId);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(begin);
		this.year = calendar.get(Calendar.YEAR);
		this.month = calendar.get(Calendar.MONTH) + 1;
		this.day = calendar.get(Calendar.DATE);
		this.hour = calendar.get(Calendar.HOUR_OF_DAY);
		
	}
	
	public String getThreadKey() {
		return threadKey;
	}

	public void setThreadKey(String threadKey) {
		this.threadKey = threadKey;
	}

	public String getId() {
		return getThreadKey();
	}
	

	public MonitorNode getBelongNode() {
		return belongNode;
	}

	public void setBelongNode(MonitorNode belongNode) {
		this.belongNode = belongNode;
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

	@Override
	public boolean existed() {
		return getRepository().exists(MainStat.class, getThreadKey());
	}

	@Override
	public boolean notExisted() {

		return !existed();
	}

	@Override
	public boolean existed(String propertyName, Object propertyValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((belongNode == null) ? 0 : belongNode.hashCode());
		result = prime * result + ((threadKey == null) ? 0 : threadKey.hashCode());
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
		MainStat other = (MainStat) obj;
		if (belongNode == null) {
			if (other.belongNode != null)
				return false;
		} else if (!belongNode.equals(other.belongNode))
			return false;
		if (threadKey == null) {
			if (other.threadKey != null)
				return false;
		} else if (!threadKey.equals(other.threadKey))
			return false;
		return true;
	}

	
}
