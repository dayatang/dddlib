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
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dayatang.domain.AbstractEntity;

/**
 * 功能描述：SQL执行明细<br />
 *  
 * 创建日期：2013-7-1 下午4:42:55  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
@Entity
@Table(name = "K_M_STMT_DETAILS")
public class JdbcStatementDetails extends AbstractEntity{

	private static final long serialVersionUID = -8440851401946155623L;

	@ManyToOne(optional = true)  
	@JoinColumn(name="FK_CONN_ID")//
	private JdbcConnDetails jdbcConn;
	
	@Column(name="BEGIN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date beginTime;
	
	@Column(name="TIME_CONSUME")
	private Long timeConsume;
	
	@Column(name = "EXECUTE_SQL",length = 1000)
	private String sql;
	
	@Column(name = "DML_TYPE")
	private String type;
	
	@Column(name = "IS_CLOSED")
	private boolean closed;


	public JdbcConnDetails getJdbcConn() {
		return jdbcConn;
	}

	public void setJdbcConn(JdbcConnDetails jdbcConn) {
		this.jdbcConn = jdbcConn;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Long getTimeConsume() {
		return timeConsume;
	}

	public void setTimeConsume(Long timeConsume) {
		this.timeConsume = timeConsume;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		if(sql != null){
			this.sql = sql.trim();
			this.type = this.sql.split("\\s+")[0].toUpperCase();
		}else{
			this.sql = null;
		}
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getId().hashCode();
		result = prime * result
				+ ((jdbcConn == null) ? 0 : jdbcConn.getId().hashCode());
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
		JdbcStatementDetails other = (JdbcStatementDetails) obj;
		if (!getId().equals(other.getId()))
			return false;
		if (jdbcConn == null) {
			if (other.jdbcConn != null)
				return false;
		} else if (!jdbcConn.getId().equals(other.jdbcConn.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JdbcSqlDetails [jdbcConn=" + jdbcConn.getId() + ", beginTime="
				+ beginTime + ", timeConsume=" + timeConsume + ", sql=" + sql
				+ ", type=" + type + "]";
	}

	

}
