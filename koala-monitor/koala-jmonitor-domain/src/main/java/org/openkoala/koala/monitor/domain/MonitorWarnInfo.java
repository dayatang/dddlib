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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openkoala.koala.base.KmBaseEntity;

/**
 * 功能描述：预警信息<br />
 *  
 * 创建日期：2013-6-25 下午3:44:01  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
//@Entity
//@Table(name = "K_M_WARN_INFO")
public class MonitorWarnInfo extends KmBaseEntity {

	private static final long serialVersionUID = -3481793288195803639L;

	@Column(name="CONTENT")
	private String content;
	
	@Column(name="LEVEL")
	private int level;
	
	@Column(name="STATUS")
	private int status;
	
	@Column(name="REMARK")
	private String remark;
	
	@ManyToOne(targetEntity = MonitorNode.class)
	@JoinColumn(name="FK_NODE_ID")
	private MonitorNode belongNode;
	
	@Column(name="WARN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date warnTime;
	
	@Column(name="PROCESS_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date processTime;
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public MonitorNode getBelongNode() {
		return belongNode;
	}

	public void setBelongNode(MonitorNode belongNode) {
		this.belongNode = belongNode;
	}

	public Date getWarnTime() {
		return warnTime;
	}

	public void setWarnTime(Date warnTime) {
		this.warnTime = warnTime;
	}

	public Date getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + getId());
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
		MonitorWarnInfo other = (MonitorWarnInfo) obj;
		if (getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
