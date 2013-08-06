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
import java.util.Date;

import org.openkoala.koala.monitor.jwebap.NetTransObject;

/**
 * 功能描述：JDBC connection 状态<br />
 *  
 * 创建日期：2013-7-8 下午4:02:43  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class JdbcConnStatusVo extends NetTransObject{
	
	private static final long serialVersionUID = -9132629815724297395L;
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	private String id;//链接ID
	private String threadKey;//主线程标识
	private Date createTime;//创建时间
	private boolean idle;//是否空闲
	private Date lastActiveTime;//上一次激活时间
	private long activeTime;//存活时间
	private String source;//源（如：调用创建该连接的方法）
	
	/**
	 * 
	 */
	public JdbcConnStatusVo(String threadKey) {
		this.threadKey = threadKey;
		createTime = new Date();
		lastActiveTime = createTime;
		idle = false;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getThreadKey() {
		return threadKey;
	}
	public void setThreadKey(String threadKey) {
		this.threadKey = threadKey;
	}
	public String getFormatCreateTime() {
		if(createTime == null)return "-";
		return DATE_FORMAT.format(createTime);
	}

	public boolean isIdle() {
		return idle;
	}
	public void setIdle(boolean idle) {
		this.idle = idle;
	}
	public String getFormatLastActiveTime() {
		if(lastActiveTime == null)return "-";
		return TIME_FORMAT.format(lastActiveTime);
	}

	public long getActiveTime() {
		return activeTime;
	}
	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastActiveTime() {
		return lastActiveTime;
	}
	public void setLastActiveTime(Date lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
}
