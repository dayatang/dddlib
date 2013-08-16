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

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jwebap.plugin.analyser.CommonAnalyser;
import org.openkoala.koala.monitor.model.JdbcConnStatusVo;
import org.openkoala.koala.monitor.model.JdbcPoolStatusVo;

/**
 * 功能描述：JDBC数据库连接池信息采集<br />
 *  
 * 创建日期：2013-7-4 下午2:14:34  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class JdbcPoolStatusCollector {

	private static final Log log = LogFactory.getLog(JdbcPoolStatusCollector.class);
	
	private Map<DataSource, Collector> collectors = new HashMap<DataSource, Collector>();
	
	//连接池状态
	private Map<DataSource, Map<String, JdbcConnStatusVo>> connPoolStats= new HashMap<DataSource, Map<String, JdbcConnStatusVo>>();
	
	private static JdbcPoolStatusCollector instance = new JdbcPoolStatusCollector();
	
	private JdbcPoolStatusCollector() {}
	

	public static synchronized JdbcPoolStatusCollector getInstance() {
		if(instance == null)instance = new JdbcPoolStatusCollector();
		return instance;
	}



	/**
	 * 获取当前连接池状态
	 * @return
	 */
	public Map<String, JdbcPoolStatusVo> currentAllDataSourceStatus(){
		try {
			Map<String, JdbcPoolStatusVo> result = new HashMap<String, JdbcPoolStatusVo>();
			if(collectors.isEmpty())return result;
			Set<DataSource> dataSources = collectors.keySet();
			for (DataSource ds : dataSources) {
				JdbcPoolStatusVo poolStatus = collectors.get(ds).currentPoolStatus();
				addConnPoolDetails(ds, poolStatus);
				result.put(ds.toString(), poolStatus);
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}


	/**
	 * 设置连接明细
	 * @param ds
	 * @param poolStatus
	 */
	private void addConnPoolDetails(DataSource ds, JdbcPoolStatusVo poolStatus) {
		if(connPoolStats.containsKey(ds)){
			List<JdbcConnStatusVo> connDetails = new ArrayList<JdbcConnStatusVo>(connPoolStats.get(ds).values());
			poolStatus.setConnDetails(connDetails);
		}
	}

	/**
	 * 注册数据源
	 */
	public void registerDataSource(DataSource ds) {
		if(collectors.containsKey(ds))return;
		Class<?> clazz = null;
		try {
			String clazzName = ds.getClass().getName();
			if(clazzName.toLowerCase().contains("dbcp")){
				clazz = Class.forName("org.openkoala.koala.monitor.support.DbcpDataSourceCollector");
			}else if(clazzName.toLowerCase().contains("c3p0")){
				clazz = Class.forName("org.openkoala.koala.monitor.support.C3p0DataSourceCollector");
			}else if(clazzName.toLowerCase().contains("proxool")){
				clazz = Class.forName("org.openkoala.koala.monitor.support.ProxoolDataSourceCollector");
			}
			Collector collector = (Collector) clazz.newInstance();
			collector.assignDataSource(ds);
			collectors.put(ds, collector);
		} catch (Exception e) {
			clazz = null;
			log.error("初始化数据库连接池["+ds+"]采集器错误",e);
		}
	
	}
	
	/**
	 * 刷新连接池状态
	 * @param ds 
	 * @param conn
	 * @param threadKey 线程标识
	 * @param act 1:激活  0：释放
 	 */
	public void refreshConnPoolStatus(DataSource ds,Connection conn,String threadKey,int act){
		String connectionId = getConnectionId(conn);
		if(connectionId != null){
			synchronized (connPoolStats) {
				Map<String, JdbcConnStatusVo> map = connPoolStats.get(ds);
				if(map == null){
					map = new HashMap<String, JdbcConnStatusVo>();
					connPoolStats.put(ds, map);
				}
				JdbcConnStatusVo status = map.get(connectionId);
				if(act == 0){//释放
					status.setActiveTime(new Date().getTime() - status.getLastActiveTime().getTime());
					status.setIdle(true);
					if(status.getSource() == null){
						//设置调用源
						String m = CommonAnalyser.getTraceEndpointMethod(threadKey);
						status.setSource(m);
					}
				}else if(act == 1){//使用
					if(status == null){
						status = new JdbcConnStatusVo(threadKey);
						status.setId(connectionId);
						map.put(connectionId, status);
						
						//设置调用源
						String m = CommonAnalyser.getTraceEndpointMethod(threadKey);
						status.setSource(m);
					}else{
						status.setLastActiveTime(new Date());
						status.setIdle(false);
					}
				}
			}
		}
	}
	
	/**
	 * 获取连接池连接的唯一标识
	 * @param conn
	 * @return
	 */
	private String getConnectionId(Connection conn){
		if(conn.getClass().getName().contains("Proxool")){
			return conn.toString().split("\\(")[1].replace(")", "");
		}
		return null;
	}
	
}
