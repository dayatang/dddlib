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
package org.openkoala.monitor.core;

import org.apache.mina.core.session.IoSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openkoala.koala.monitor.datasync.server.DatasyncServer;
import org.openkoala.koala.monitor.service.MonitorDataService;
import org.openkoala.koala.monitor.service.remote.RemoteDataPolicyServerHandler;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-8-12 下午4:33:48  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class RemoteDataPolicyServerHandlerTest{

	private RemoteDataPolicyServerHandler instance;
	
	@Mock
    private MonitorDataService monitorDataService;
	
	@Mock
	private DatasyncServer minaServer;
	
	@Mock
	private IoSession ioSession;
	
	String clientId = "demo";
	
	@Before
	public void beforeTest() {
		instance = new RemoteDataPolicyServerHandler();
		MockitoAnnotations.initMocks(this);
		minaServer.getSessions().put(clientId, ioSession);
		instance.setMinaServer(minaServer);
		instance.setMonitorDataService(monitorDataService);
	}

	@After
	public void afterTest() {
		instance.setMinaServer(null);
		instance.setMonitorDataService(null);
		instance = null;
	}
	
	@Test
	public void testDojob() throws Exception {
		instance.doJob();
		//Mockito.verify(minaServer).sendData(clientId, new Object());
		//Mockito.verify(monitorDataService).saveMonitorData(clientId, trace);
	}
	
	
}
