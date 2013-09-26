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
package org.openkoala.koala.monitor.support;

import javax.sql.DataSource;

import org.logicalcobwebs.proxool.ConnectionPoolDefinitionIF;
import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.admin.SnapshotIF;
import org.openkoala.koala.monitor.model.JdbcPoolStatusVo;

/**
 * 功能描述：<br />
 * 
 * 创建日期：2013-7-4 下午3:15:37 <br />
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作 者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者 修改日期 文件版本 修改说明
 */
public class ProxoolDataSourceCollector implements Collector {
	//
	private String alias;
	private DataSource dataSoure;

	/**
	 * @param dataSoure
	 */
	public ProxoolDataSourceCollector() {

	}

	public synchronized JdbcPoolStatusVo currentPoolStatus() {
		JdbcPoolStatusVo poolStatus = new JdbcPoolStatusVo();
		try {
			ConnectionPoolDefinitionIF cpd = getConnectionPoolDefinitionIF();
			SnapshotIF snapshot = ProxoolFacade.getSnapshot(cpd.getAlias(),
					true);
			poolStatus = new JdbcPoolStatusVo();
			poolStatus.setPoolAlias(cpd.getAlias());
			poolStatus.setDriverName(cpd.getDriver());
			poolStatus.setStartTime(snapshot.getDateStarted());
			poolStatus.setProvider(dataSoure.getClass().getName());
			poolStatus.setInitConnectionCount(cpd
					.getMinimumConnectionCount());
			poolStatus.setMaxConnectionCount(cpd
					.getMaximumConnectionCount());
			poolStatus.setMaxActiveTime(cpd.getMaximumActiveTime()/1000);
			poolStatus.setSnapshotTime(snapshot.getSnapshotDate());
			poolStatus.setIdleConnectionCount(snapshot
					.getAvailableConnectionCount());
			poolStatus.setActiveConnectionCount(snapshot
					.getActiveConnectionCount());
			poolStatus.setRefusedCount(snapshot.getServedCount());
			poolStatus.setServedCount(snapshot.getRefusedCount());	
		} catch (Exception e) {
			poolStatus.setErrorTip("获取实时连接池信息失败");
		}

		return poolStatus;
	}

	private ConnectionPoolDefinitionIF getConnectionPoolDefinitionIF() {
		ConnectionPoolDefinitionIF def = null;
		
		try {
			if (alias == null) {
				ProxoolDataSource realDS = (ProxoolDataSource)dataSoure;
				alias = realDS.getAlias();
			}
			def = ProxoolFacade.getConnectionPoolDefinition(alias);
		} catch (ProxoolException e) {
			throw new RuntimeException("Couldn't find Proxool Pool'alias ");
		}
		return def;
	}

	@Override
	public void assignDataSource(DataSource dataSoure) {
		this.dataSoure = dataSoure;
	}
}
