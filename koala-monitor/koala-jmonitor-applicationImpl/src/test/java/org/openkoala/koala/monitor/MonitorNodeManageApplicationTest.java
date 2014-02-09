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

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.koala.monitor.application.MonitorNodeManageApplication;
import org.openkoala.koala.monitor.core.ContextStartup;
import org.openkoala.koala.monitor.model.GeneralMonitorStatusVo;
import org.openkoala.koala.monitor.model.JdbcPoolStatusVo;
import org.openkoala.koala.monitor.model.MonitorNodeVo;
import org.openkoala.koala.monitor.model.ServerStatusVo;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;

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
public class MonitorNodeManageApplicationTest extends KoalaBaseSpringTestCase {

	@Inject
	private MonitorNodeManageApplication application;

	@BeforeClass
	public static void beforeTest() {
		// 启动监控context
		String configPath = "META-INF/monitor/koala-monitor.xml";
		URL url = Thread.currentThread().getContextClassLoader().getResource(configPath);
		configPath = url.getFile();
		Map<String, String> serverInfos = new HashMap<String, String>();
		ContextStartup.startup(configPath, serverInfos);
	}

	@AfterClass
	public static void afterTest() {
		//MonitorNode node = MonitorNode.get(MonitorNode.class, "junit-test");
		//node.inactive();
	}

	@Ignore
	@Test
	public void testGetAllNodes() {
		List<MonitorNodeVo> nodes = application.getAllNodes();
		org.junit.Assert.assertTrue(nodes.size() > 0);

	}

	@Test
	public void testQueryNode() {
		MonitorNodeVo node = application.queryNode("junit-test");
		org.junit.Assert.assertNotNull(node);

	}

	@Ignore
	@Test
	public void testUpdateNode() {
		application.updateNode(new MonitorNodeVo());
	}

	@Ignore
	@Test
	public void testGetNodeServerStatus() {
		ServerStatusVo status = application.getNodeServerStatus("junit-test");
		org.junit.Assert.assertNotNull(status);
	}

	@Ignore
	@Test
	public void testGetGeneralMonitorStatus() {
		GeneralMonitorStatusVo status = application
				.getGeneralMonitorStatus("junit-test");
		org.junit.Assert.assertNotNull(status);
	}

	@Ignore
	@Test
	public void testGetJdbcPoolStatus() {
		Map<String, JdbcPoolStatusVo> status = application.getJdbcPoolStatus("junit-test");
		org.junit.Assert.assertNotNull(status);
	}

}
