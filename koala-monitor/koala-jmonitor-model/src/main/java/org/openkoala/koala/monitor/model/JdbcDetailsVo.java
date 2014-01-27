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


/**
 * 功能描述：<br />
 * 
 * 创建日期：2013-6-7 上午9:56:29 <br />
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作 者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者 修改日期 文件版本 修改说明
 */
public class JdbcDetailsVo extends BaseDetailsVo {

	private static final long serialVersionUID = 3667605232310643782L;

	private Long id;

	private int connOpenCount;

	private int connCloseCount;

	private int pstmtCreateCount;

	private int pstmtCloseCount;

	private int stmtCreateCount;

	private int stmtCloseCount;

	private String sqlDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPstmtCreateCount() {
		return pstmtCreateCount;
	}

	public void setPstmtCreateCount(int pstmtCreateCount) {
		this.pstmtCreateCount = pstmtCreateCount;
	}

	public int getPstmtCloseCount() {
		return pstmtCloseCount;
	}

	public void setPstmtCloseCount(int pstmtCloseCount) {
		this.pstmtCloseCount = pstmtCloseCount;
	}

	public int getStmtCreateCount() {
		return stmtCreateCount;
	}

	public void setStmtCreateCount(int stmtCreateCount) {
		this.stmtCreateCount = stmtCreateCount;
	}

	public int getStmtCloseCount() {
		return stmtCloseCount;
	}

	public void setStmtCloseCount(int stmtCloseCount) {
		this.stmtCloseCount = stmtCloseCount;
	}

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

	public String getSqlDetails() {
		return sqlDetails;
	}

	public void setSqlDetails(String sqlDetails) {
		this.sqlDetails = sqlDetails;
	}


}
