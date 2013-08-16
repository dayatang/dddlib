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

import org.openkoala.koala.monitor.model.ScheduleConfVo;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-7-3 上午9:52:55  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public interface ServiceMonitorApplication {

	/**
	 * 查询指定定时任务
	 * @param triggerNames
	 * @return
	 */
	ScheduleConfVo queryScheduler(String triggerName);
	
	/**
	 * 查询定时任务列表
	 * @param triggerNames
	 * @return
	 */
	List<ScheduleConfVo> queryAllSchedulers();
	
	/**
	 * 更新定时任务配置
	 * @param conf
	 */
	void updateScheduleConf(ScheduleConfVo conf);
}
