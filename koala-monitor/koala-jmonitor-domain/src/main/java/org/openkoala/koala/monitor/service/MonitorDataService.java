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
package org.openkoala.koala.monitor.service;

import java.util.Date;
import java.util.List;

import org.openkoala.koala.monitor.def.Trace;
import org.openkoala.koala.monitor.model.CountVo;
import org.openkoala.koala.monitor.model.HttpDetailsVo;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-6-8 上午9:30:25  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public interface MonitorDataService {

	void saveMonitorData(String clientId,List<Trace> trace);
	
	/**
	 * 获取指定节点http请求统计记录
	 * @param nodeId
	 * @param statUnit
	 * @param beginTime
	 * @return
	 */
	List<CountVo> getHttpMonitorCount(String nodeId, String statUnit, Date beginTime);
	
	/**
	 * 获取监控(JDBC)统计列表
	 * @param countType 需要统计的项，对应Constant中的常量
	 * @param mainStatVo
	 * @return
	 */
//	public Map<String, List<CountVo>> getAllMainStatsForJdbc(JdbcTemplate jdbcTemplate, String countType, MainStatVo mainStatVo);
	
	/**
	 * 获取http监控明细列表
	 * @param countType
	 * @param requestDate
	 * @return
	 */
	List<HttpDetailsVo> pageGetHttpMonitorDetails(int start, int pageSize, HttpDetailsVo httpDetailsVo);
}
