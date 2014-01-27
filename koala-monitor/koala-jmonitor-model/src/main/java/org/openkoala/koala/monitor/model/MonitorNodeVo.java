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

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.openkoala.koala.monitor.common.KoalaDateUtils;

/**
 * 功能描述：<br />
 * 
 * 创建日期：2013-6-4 下午3:06:15 <br />
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作 者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者 修改日期 文件版本 修改说明
 */
public class MonitorNodeVo implements Serializable {

	private static final long serialVersionUID = -2913020327300262241L;
	
	private String nodeId;

	private String nodeUri;

	private String nodeName;

	private Date latestSessionTime;
	
	private boolean active;
	
	private int maxCacheSize = 5000;//最大缓存数
	private int cacheExpireTime = 15;//缓存超时时间（单位：分钟）
	private int syncDataInterval = 120;//数据同步间隔 （秒）

	private Set<MonitorComponentVo> conponents;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeUri() {
		return nodeUri;
	}

	public void setNodeUri(String nodeUri) {
		this.nodeUri = nodeUri;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getLatestSessionTime() {
		return KoalaDateUtils.format(latestSessionTime);
	}

	public void setLatestSessionTime(Date latestSessionTime) {
		this.latestSessionTime = latestSessionTime;
	}
	

	public int getMaxCacheSize() {
		return maxCacheSize;
	}

	public void setMaxCacheSize(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
	}

	public int getCacheExpireTime() {
		return cacheExpireTime;
	}

	public void setCacheExpireTime(int cacheExpireTime) {
		this.cacheExpireTime = cacheExpireTime;
	}

	public int getSyncDataInterval() {
		return syncDataInterval;
	}

	public void setSyncDataInterval(int syncDataInterval) {
		this.syncDataInterval = syncDataInterval;
	}

	public Set<MonitorComponentVo> getConponents() {
		if(conponents == null)conponents = new HashSet<MonitorComponentVo>();
		return conponents;
	}

	public void setConponents(Set<MonitorComponentVo> conponents) {
		this.conponents = conponents;
	}
		
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setConponent(MonitorComponentVo conponent) {
		getConponents().add(conponent);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
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
		MonitorNodeVo other = (MonitorNodeVo) obj;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		return true;
	}

	

}
