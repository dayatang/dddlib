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

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.koala.config.domain.SchedulerConfg;
import org.openkoala.koala.monitor.application.ServiceMonitorApplication;
import org.openkoala.koala.monitor.model.ScheduleConfVo;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;

/**
 * 功能描述：<br />
 * 
 * 创建日期：2013-8-12 下午3:47:28 <br />
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作 者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者 修改日期 文件版本 修改说明
 */
public class ServiceMonitorApplicationTest extends KoalaBaseSpringTestCase {

	@Inject
	ServiceMonitorApplication serviceMonitorApplication;

	
	@Override
	@Before
	public void setup() {
		super.setup();
		SchedulerConfg conf = new SchedulerConfg();
		conf.setActive(true);
		conf.setCronExpr("0 0/2 * * * ");
		conf.setSchedulerName("demo");
		conf.setTriggerName("demoTrigger");
		conf.save();
	}


	@Test
	public void testQueryAllSchedulers() {
		List<ScheduleConfVo> list = serviceMonitorApplication.queryAllSchedulers();
		Assert.assertTrue(list.size()>0);
	}
	
	@Test
	public void testQueryScheduler() {
		ScheduleConfVo vo = serviceMonitorApplication.queryScheduler("demoTrigger");
		Assert.assertNotNull(vo);
	}
	
	@Test
	public void testUpdateScheduleConf() {
		ScheduleConfVo conf = new ScheduleConfVo();
		conf.setTriggerName("demoTrigger");
		conf.setInterval(15);
		serviceMonitorApplication.updateScheduleConf(conf);
		ScheduleConfVo vo = serviceMonitorApplication.queryScheduler("demoTrigger");
		Assert.assertEquals(15, vo.getInterval());
	}

}
