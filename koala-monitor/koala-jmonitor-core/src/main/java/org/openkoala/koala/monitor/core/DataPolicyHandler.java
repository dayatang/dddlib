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
package org.openkoala.koala.monitor.core;

import java.util.Map;

import org.openkoala.koala.monitor.def.ComponentDef;
import org.openkoala.koala.monitor.model.GeneralMonitorStatusVo;
import org.openkoala.koala.monitor.model.JdbcPoolStatusVo;
import org.openkoala.koala.monitor.model.ServerStatusVo;


/**
 * 功能描述：数据处理策略接口<br />
 *  
 * 创建日期：2013-6-7 下午3:57:34  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public interface DataPolicyHandler {
	/**
	 * 更新监控配置
	 * @param clientId
	 * @param comp
	 */
	void updateComponentConfig(String clientId,ComponentDef comp);
	
	/**
	 * 获取服务器状态信息
	 * @param nodeId
	 * @return
	 */
	ServerStatusVo getServerStatusInfo(String nodeId);
	
	/**
	 * 获取指定节点综合监控信息
	 * @param nodeId
	 * @return
	 */
	GeneralMonitorStatusVo getGeneralMonitorStatus(String nodeId);
	
	/**
	 * 获取指定节点数据库连接池信息
	 * @param nodeId
	 * @return
	 */
	Map<String, JdbcPoolStatusVo> getJdbcPoolStatus(String nodeId);
	
}
