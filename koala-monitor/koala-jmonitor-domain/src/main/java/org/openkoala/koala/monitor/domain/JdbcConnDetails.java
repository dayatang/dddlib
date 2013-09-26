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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-7-1 下午4:52:00  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */

@Entity
@Table(name = "K_M_JDBC_CONN_DETAILS")
public class JdbcConnDetails extends BaseMonitorDetails {

	private static final long serialVersionUID = -3440499796426034417L;
	
	@OneToMany(cascade = {CascadeType.REMOVE,CascadeType.PERSIST},mappedBy ="jdbcConn",fetch = FetchType.EAGER) 
	private List<JdbcStatementDetails> statements = new ArrayList<JdbcStatementDetails>();
	
	@Column(name = "IS_CLOSED")
	private boolean closed;


	public List<JdbcStatementDetails> getStatements() {
		return statements;
	}

	public void setStatements(List<JdbcStatementDetails> statements) {
		this.statements = statements;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}
    
}
