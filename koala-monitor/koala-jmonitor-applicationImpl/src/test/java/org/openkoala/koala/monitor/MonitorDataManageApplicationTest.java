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
package org.openkoala.koala.monitor;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.koala.monitor.application.MonitorDataManageApplication;
import org.openkoala.koala.monitor.common.Constant;
import org.openkoala.koala.monitor.common.KoalaDateUtils;
import org.openkoala.koala.monitor.model.CountVo;
import org.openkoala.koala.monitor.model.HttpDetailsVo;
import org.openkoala.koala.monitor.model.JdbcStatementDetailsVo;
import org.openkoala.koala.monitor.model.MainStatVo;
import org.openkoala.koala.monitor.model.MethodDetailsVo;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;

import com.dayatang.querychannel.support.Page;

/**
 * 功能描述：<br />
 * 
 * 创建日期：2013-6-26 下午4:45:04 <br />
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作 者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者 修改日期 文件版本 修改说明
 */
public class MonitorDataManageApplicationTest extends KoalaBaseSpringTestCase {

	@Inject
	private MonitorDataManageApplication application;

	@Before
	public void beforeTest() {
		
		/*Date now = new Date();
		MonitorNode node = new MonitorNode();
		node.setLatestSessionTime(now);
		node.setNodeId("demo-node");
		node.setNodeName("node");
		node.setNodeUri("127.0.0.1");
		node.active();
		
		String traceId = UUID.randomUUID().toString().replaceAll("\\-", "");
		
		MainStat main = new MainStat("demo-node", now);
		main.setTraceId(traceId);
		main.save();
		
		HttpDetails http = new HttpDetails();
		http.setBeginTime(now);
		http.setIp("111.111.111.111");
		http.setNodeId("demo-node");
		http.setPrincipal("admin");
		http.setTimeConsume(100L);
		http.setTraceKey(traceId);
		http.setUri("/login.jsp");
		http.save();
		
		MethodDetails method = new MethodDetails();
		method.setBeginTime(now);
		method.setMethod("");
		method.setNodeId("");
		method.setStackTracesDetails("ex");
		method.setSuccessed(true);
		method.setTimeConsume(100L);
		method.setTraceKey(traceId);
		
		method.save();*/
	}

	@AfterClass
	public static void afterTest() {
		
	}

	@Test
	public void testGetHttpMonitorCount() {
		MainStatVo mainStatVo = new MainStatVo();
		mainStatVo.setBeginTimeStr("2013-01-01");
		mainStatVo.setUnit(Constant.UNIT_HOUR);
		mainStatVo.setPrincipal("demo-node");
		List<CountVo> count = application.getHttpMonitorCount(mainStatVo);
		Assert.assertEquals(24, count.size());
	}

	@Test
	public void testPageGetHttpMonitorDetails() {

		HttpDetailsVo search = new HttpDetailsVo();
		search.setBeginTime(KoalaDateUtils.parseDate("2013-08-01"));
		search.setEndTime(KoalaDateUtils.parseDate("2013-08-02"));
		search.setSystem("demo-node");
		search.setSortname("timeConsume");
		search.setSortorder("desc");

		Page<HttpDetailsVo> details = application.pageGetHttpMonitorDetails(1, 10, search);
		Assert.assertNotNull(details);
	}
	
	@Test
	public void testPageGetMethodMonitorCount() {

		MainStatVo mainStatVo = new MainStatVo();
		mainStatVo.setPrincipal("demo-node");
		mainStatVo.setBeginTime(KoalaDateUtils.parseDate("2013-08-01"));
		mainStatVo.setEndTime(KoalaDateUtils.parseDate("2013-08-02"));

		Page<CountVo> page = application.pageGetMethodMonitorCount(1, 10, mainStatVo);
		Assert.assertNotNull(page);
	}
	
	@Test
	public void testGetMethodMonitorAvgTimeConsume() {

		MainStatVo mainStatVo = new MainStatVo();
		mainStatVo.setPrincipal("demo-node");
		mainStatVo.setBeginTime(KoalaDateUtils.parseDate("2013-08-01"));
		mainStatVo.setEndTime(KoalaDateUtils.parseDate("2013-08-02"));

		Page<CountVo> page = application.pageGetMethodMonitorAvgTimeConsume(1, 10, mainStatVo);
		Assert.assertNotNull(page);
	}
	
	@Test
	public void testGetMethodMonitorExceptionCount() {

		MainStatVo mainStatVo = new MainStatVo();
		mainStatVo.setPrincipal("demo-node");
		mainStatVo.setBeginTime(KoalaDateUtils.parseDate("2013-08-01"));
		mainStatVo.setEndTime(KoalaDateUtils.parseDate("2013-08-02"));

		Page<CountVo> page = application.pageGetMethodMonitorExceptionCount(1, 10, mainStatVo);
		Assert.assertNotNull(page);
	}
	
	
	@Test
	public void testGetMethodMonitorDetails() {

		MethodDetailsVo methodDetailsVo = new MethodDetailsVo();
		methodDetailsVo.setThreadKey("xxxxx");
		methodDetailsVo.setSystem("demo-node");
		methodDetailsVo.setMethod("add");
		methodDetailsVo.setBeginTime(KoalaDateUtils.parseDate("2013-08-01"));
		methodDetailsVo.setEndTime(KoalaDateUtils.parseDate("2013-08-02"));

		Page<MethodDetailsVo> page = application.pageGetMethodMonitorDetails(1, 10, methodDetailsVo);
		Assert.assertNotNull(page);
	}
	
	
	@Test
	public void testGetSqlsMonitorDetails() {

		JdbcStatementDetailsVo search = new JdbcStatementDetailsVo();
		search.setMethodId(1000L);
		search.setSortname("timeConsume");
		search.setSortorder("desc");

		Page<JdbcStatementDetailsVo> page = application.getSqlsMonitorDetails(1, 10, search);
		Assert.assertNotNull(page);
	}
	
	@Test
	public void testGetStackTracesDetails(){
		List<String> details = application.getStackTracesDetails(Constant.MONITOR_TYPE_METHOD, "1000");
		Assert.assertNotNull(details);
	}
	
	@Test
	public void testGetJdbcConnTimeStat(){
		Map<Integer, Integer> map = application.getJdbcConnTimeStat("demo-node", 2*1000);
		Assert.assertNotNull(map);
	}
	
}
