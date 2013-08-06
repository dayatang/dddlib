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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-6-7 上午9:56:29  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
@Entity
@Table(name = "K_M_JDBC_DETAILS")
public class JdbcDetails extends BaseMonitorDetails {

	private static final long serialVersionUID = 3667605232310643782L;
	
	@Column(name = "CONN_OPEN_COUNT")
    private int connOpenCount = 1;
	
	@Column(name = "STMT_CREATE_COUNT")
	private int stmtCreateCount;
	
	@Column(name = "PSTMT_CREATE_COUNT")
	private int pstmtCreateCount;
	
	@Column(name = "CONN_CLOSE_COUNT")
    private int connCloseCount;
	
	@Column(name = "STMT_CLOSE_COUNT")
	private int stmtCloseCount;
	
	@Column(name = "PSTMT_CLOSE_COUNT")
	private int pstmtCloseCount;
	
	@Lob
	@Column(name = "SQLS")
	private String sqlDetails;
	

	public int getConnOpenCount() {
		return connOpenCount;
	}

	public void setConnOpenCount(int connOpenCount) {
		this.connOpenCount = connOpenCount;
	}

	public int getConnCloseCount() {
		return connCloseCount;
	}

	public void setConnCloseCount(int connCloseCount) {
		this.connCloseCount = connCloseCount;
	}
	
	public int getStmtCreateCount() {
		return stmtCreateCount;
	}

	public void setStmtCreateCount(int stmtCreateCount) {
		this.stmtCreateCount = stmtCreateCount;
	}

	public int getPstmtCreateCount() {
		return pstmtCreateCount;
	}

	public void setPstmtCreateCount(int pstmtCreateCount) {
		this.pstmtCreateCount = pstmtCreateCount;
	}

	public int getStmtCloseCount() {
		return stmtCloseCount;
	}

	public void setStmtCloseCount(int stmtCloseCount) {
		this.stmtCloseCount = stmtCloseCount;
	}

	public int getPstmtCloseCount() {
		return pstmtCloseCount;
	}

	public void setPstmtCloseCount(int pstmtCloseCount) {
		this.pstmtCloseCount = pstmtCloseCount;
	}

	public String getSqlDetails() {
		return sqlDetails;
	}

	public void setSqlDetails(String sqlDetails) {
		this.sqlDetails = sqlDetails;
	}

	@Override
	public String toString() {
		return "JdbcDetails [connOpenCount=" + connOpenCount
				+ ", stmtCreateCount=" + stmtCreateCount
				+ ", pstmtCreateCount=" + pstmtCreateCount
				+ ", connCloseCount=" + connCloseCount + ", stmtCloseCount="
				+ stmtCloseCount + ", pstmtCloseCount=" + pstmtCloseCount
				+ ", beginTime=" + beginTime + "]";
	}

	

}
