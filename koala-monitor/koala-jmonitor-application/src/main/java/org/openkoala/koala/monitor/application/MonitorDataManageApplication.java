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

import org.openkoala.koala.monitor.model.CountVo;
import org.openkoala.koala.monitor.model.HttpDetailsVo;
import org.openkoala.koala.monitor.model.JdbcDetailsVo;
import org.openkoala.koala.monitor.model.JdbcStatementDetailsVo;
import org.openkoala.koala.monitor.model.MainStatVo;
import org.openkoala.koala.monitor.model.MethodDetailsVo;

import com.dayatang.querychannel.support.Page;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-6-19 下午2:07:57  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public interface MonitorDataManageApplication {
	
	
	/**
	 * 获取http访问次数统计列表
	 * @param mainStatVo
	 * @return
	 */
	List<CountVo> getHttpMonitorCount(MainStatVo mainStatVo);
	
	/**
	 * 获取监控(JDBC)统计列表
	 * @param countType 需要统计的项，对应Constant中的常量
	 * @param mainStatVo
	 * @return
	 */
//	public Map<String, List<CountVo>> getAllMainStatsForJdbc(String countType, MainStatVo mainStatVo);
	
	/**
	 * 获取http监控明细列表
	 * @param currentPage 第几页
	 * @param pageSize 每页大小
	 * @param httpDetailsVo 参数封装成bean
	 * @return
	 */
	Page<HttpDetailsVo> pageGetHttpMonitorDetails(int currentPage, int pageSize, HttpDetailsVo httpDetailsVo);
	
	/**
	 * 获取method调用次数统计列表
	 * @param currentPage
	 * @param pageSize
	 * @param mainStatVo
	 * @return
	 */
	Page<CountVo> pageGetMethodMonitorCount(int currentPage, int pageSize, MainStatVo mainStatVo);
	
	/**
	 * 获取method调用平均耗时统计列表
	 * @param currentPage
	 * @param pageSize
	 * @param mainStatVo
	 * @return
	 */
	public Page<CountVo> pageGetMethodMonitorAvgTimeConsume(int currentPage, int pageSize, MainStatVo mainStatVo);
	
	/**
	 * 获取method调用出错次数统计列表
	 * @param currentPage
	 * @param pageSize
	 * @param mainStatVo
	 * @return
	 */
	public Page<CountVo> pageGetMethodMonitorExceptionCount(int currentPage, int pageSize, MainStatVo mainStatVo);
	
	
	/**
	 * 获取method监控明细列表
	 * @param currentPage 第几页
	 * @param pageSize 每页大小
	 * @param methodDetailsVo 参数封装成bean
	 * @return
	 */
	Page<MethodDetailsVo> pageGetMethodMonitorDetails(int currentPage, int pageSize, MethodDetailsVo methodDetailsVo);
	
	/**
	 * 获取jdbc监控明细列表
	 * @param currentPage 第几页
	 * @param pageSize 每页大小
	 * @param jdbcDetailsVo 参数封装成bean
	 * @return
	 */
	Page<JdbcDetailsVo> pageGetJdbcMonitorDetails(int currentPage, int pageSize, JdbcDetailsVo jdbcDetailsVo);
	
	/**
	 * 获取sql监控明细列表
	 * @param jdbcStatementDetailsVo
	 * @return
	 */
	Page<JdbcStatementDetailsVo> getSqlsMonitorDetails(int currentPage, int pageSize, JdbcStatementDetailsVo jdbcStatementDetailsVo);
	
	/**
	 * 获取堆栈信息
	 * @param jdbcDetailsId
	 * @return
	 */
	List<String> getStackTracesDetails(String monitorType, String detailsId);
	
	/**
	 * 获取JDBC连接耗时汇总信息（近24小时）
	 * @param nodeId
	 * @param timeoutLimit  超时阀值
	 * @return
	 */
	Map<Integer, Integer> getJdbcConnTimeStat(String nodeId,long timeoutLimit);
}
