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
package org.openkoala.koala.monitor.application;

import java.util.List;
import java.util.Map;

import org.openkoala.koala.monitor.model.GeneralMonitorStatusVo;
import org.openkoala.koala.monitor.model.JdbcPoolStatusVo;
import org.openkoala.koala.monitor.model.MonitorComponentVo;
import org.openkoala.koala.monitor.model.MonitorNodeVo;
import org.openkoala.koala.monitor.model.MonitorWarnInfoVo;
import org.openkoala.koala.monitor.model.ServerStatusVo;

import com.dayatang.querychannel.support.Page;

/**
 * 功能描述：监控节点管理应用接口<br />
 *  
 * 创建日期：2013-6-4 下午3:04:31  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public interface MonitorNodeManageApplication {

	/**
	 * 获取所有监控节点
	 * @return
	 */
	List<MonitorNodeVo> getAllNodes();
	
	/**
	 * 查询指定监控节点
	 * @param nodeId
	 * @return
	 */
	MonitorNodeVo queryNode(String nodeId);
	
	
	/**
	 * 更新节点
	 */
	void updateNode(MonitorNodeVo node);
	
	/**
	 * 更新监控配置信息
	 * @param nodeId 节点ID
	 * @param comp
	 */
	void  updateComponentConfig(String nodeId,MonitorComponentVo comp);
	
	/**
	 * 获取监控节点服务器状态
	 * @param nodeId
	 * @return
	 */
	ServerStatusVo getNodeServerStatus(String nodeId); 
	
	/**
	 * 获取指定节点综合监控信息
	 * @param nodeId
	 * @return
	 */
	GeneralMonitorStatusVo getGeneralMonitorStatus(String nodeId);
	
	/**
	 * 分页查询预警信息
	 * @param search
	 * @param page
	 * @param pagesize
	 * @return
	 */
	Page<MonitorWarnInfoVo> queryMonitorWarnInfos(MonitorWarnInfoVo search,int page, int pagesize);
	
	/**
	 * 获取指定节点数据库连接池信息
	 * @param nodeId
	 * @return
	 */
	Map<String, JdbcPoolStatusVo> getJdbcPoolStatus(String nodeId);
}
