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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openkoala.koala.monitor.jwebap.NetTransObject;

/**
 * 功能描述：JDBC连接池状态<br />
 *  
 * 创建日期：2013-7-4 下午2:39:45  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class JdbcPoolStatusVo extends NetTransObject{

	private static final long serialVersionUID = 2323195613726740830L;
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String poolAlias;//线程池别名
	private String provider;//提供者
	private Date startTime;//开始服务时间
	private Date snapshotTime;//快照时间
	private String driverName;//驱动
	private int maxConnectionCount;//最大连接池数量
	private int initConnectionCount;//初始化连接池数量
	private long maxActiveTime;//最大存活时间
	private int activeConnectionCount;//当前正在使用连接数量
	private int idleConnectionCount;//当前可用连接数量（已经创建并且空闲）
	private int maxOpenStatements;
	private long servedCount;//
	private long refusedCount;
	
	private String errorTip;
	
	private List<JdbcConnStatusVo> connDetails = new ArrayList<JdbcConnStatusVo>();//连接池明细
	
	public String getPoolAlias() {
		return poolAlias;
	}
	public void setPoolAlias(String poolAlias) {
		this.poolAlias = poolAlias;
	}

	public String getStartTime() {
		if(startTime == null)return null;
		return DATE_FORMAT.format(startTime);
	}
	
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getSnapshotTime() {
		if(snapshotTime == null)return null;
		return DATE_FORMAT.format(snapshotTime);
	}
	public void setSnapshotTime(Date snapshotTime) {
		this.snapshotTime = snapshotTime;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public int getMaxConnectionCount() {
		return maxConnectionCount;
	}
	public void setMaxConnectionCount(int maxConnectionCount) {
		this.maxConnectionCount = maxConnectionCount;
	}
	public int getInitConnectionCount() {
		return initConnectionCount;
	}
	public void setInitConnectionCount(int initConnectionCount) {
		this.initConnectionCount = initConnectionCount;
	}
	
	public int getActiveConnectionCount() {
		return activeConnectionCount;
	}
	public void setActiveConnectionCount(int activeConnectionCount) {
		this.activeConnectionCount = activeConnectionCount;
	}
	public int getIdleConnectionCount() {
		return idleConnectionCount;
	}
	public void setIdleConnectionCount(int idleConnectionCount) {
		this.idleConnectionCount = idleConnectionCount;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public long getMaxActiveTime() {
		return maxActiveTime;
	}
	public void setMaxActiveTime(long maxActiveTime) {
		this.maxActiveTime = maxActiveTime;
	}
	
	public long getServedCount() {
		return servedCount;
	}
	public void setServedCount(long servedCount) {
		this.servedCount = servedCount;
	}
	public long getRefusedCount() {
		return refusedCount;
	}
	public void setRefusedCount(long refusedCount) {
		this.refusedCount = refusedCount;
	}
	public String getErrorTip() {
		return errorTip;
	}
	public void setErrorTip(String errorTip) {
		this.errorTip = errorTip;
	}
	public int getMaxOpenStatements() {
		return maxOpenStatements;
	}
	public void setMaxOpenStatements(int maxOpenStatements) {
		this.maxOpenStatements = maxOpenStatements;
	}
	public List<JdbcConnStatusVo> getConnDetails() {
		return connDetails;
	}
	public void setConnDetails(List<JdbcConnStatusVo> connDetails) {
		this.connDetails = connDetails;
	}
	
	
}
