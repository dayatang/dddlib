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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openkoala.koala.base.KmBaseLegacyEntity;
import org.openkoala.koala.monitor.def.ComponentDef;

/**
 * 功能描述：监控节点<br />
 *  
 * 创建日期：2013-6-4 下午2:27:53  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
@Entity
@Table(name = "K_M_NODE")
public class MonitorNode extends KmBaseLegacyEntity {
	
	private static final long serialVersionUID = -6627155720605604832L;
	
	private static final Map<String,MonitorNode> nodeCache = new HashMap<String, MonitorNode>();

	@Id
	@Column(name="NODE_ID")
	private String nodeId;
	
	@Column(name="NODE_URI")
	private String nodeUri;
	
	@Column(name="NODE_NAME")
	private String nodeName;
	
	@Column(name="IS_ACTIVE")
	private boolean active;
	
	private transient Date latestSessionTime;
	
	private transient int maxCacheSize;//最大缓存数
	
	private transient int cacheExpireTime;//缓存超时时间（单位：分钟）
	
	private transient Set<MonitorComponent> conponents;
	

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
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Date getLatestSessionTime() {
		return latestSessionTime;
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

	public Set<MonitorComponent> getConponents() {
		if(conponents == null)conponents = new HashSet<MonitorComponent>();
		return conponents;
	}

	public void setConponents(Set<MonitorComponent> conponents) {
		this.conponents = conponents;
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
		MonitorNode other = (MonitorNode) obj;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		return true;
	}

	@Override
	public String getId() {
		return getNodeId();
	}

	@Override
	public boolean existed() {
		return get(MonitorNode.class, getId()) != null;
	}

	@Override
	public boolean notExisted() {
		return existed() == false;
	}

	@Override
	public boolean existed(String propertyName, Object propertyValue) {
		MonitorNode result = getRepository().getSingleResult("from MonitorNode where "+propertyName+" = ?", new Object[]{propertyValue}, MonitorNode.class);
		return result != null;
	}
	
	
	public void inactive(){
		synchronized (nodeCache) {
			MonitorNode remove = nodeCache.remove(this.getNodeId());
			this.nodeName = remove.nodeName;
			this.nodeUri = remove.nodeUri;
		}
		setActive(false);
		this.save();
	}
	
	public void active(){
		synchronized (nodeCache) {
			nodeCache.put(this.getNodeId(), this);
		}
		setActive(true);
		this.save();
	}

	/**
	 * 刷新节点最后活动时间
	 * @return
	 */
	public static Map<String,MonitorNode> getAllNodesCache(){
		return nodeCache;
	}
	
	public static void refreshNodeLastActiveTime(String... nodes){
		List<String> nodeIds = new ArrayList<String>();
		if(nodes != null && nodes.length>0){
			nodeIds.addAll(Arrays.asList(nodes));
		}
		for (String key : nodeCache.keySet()) {
			if(nodeIds.isEmpty() || nodeIds.contains(key)){
				nodeCache.get(key).setLatestSessionTime(new Date());
			}
		}
	}

	public static class MonitorComponent implements Serializable {
		private static final long serialVersionUID = -2788616949480203707L;
		private String type = null;

		private String name = null;
		
		private boolean active;//是否激活
		
		protected Map<String, String> properties=new HashMap<String, String>();
		

		public MonitorComponent() {}
		
		public MonitorComponent(ComponentDef compDef) {
			this.type = compDef.getType();
			this.name = compDef.getName();
			this.active = compDef.isActive();
			Set<String> keys = compDef.getProperties().keySet();
			for (String key : keys) {
				properties.put(key, compDef.getProperty(key));
			}
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public Map<String, String> getProperties() {
			return properties;
		}

		public void setProperties(Map<String, String> properties) {
			this.properties = properties;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
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
			MonitorComponent other = (MonitorComponent) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
	}

}
