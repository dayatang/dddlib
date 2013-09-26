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

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.openkoala.koala.config.domain.SchedulerConfg;
import org.openkoala.koala.monitor.application.ServiceMonitorApplication;
import org.openkoala.koala.monitor.model.ScheduleConfVo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-7-3 上午9:56:53  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
@Named(value="serviceMonitorApplication")
@Transactional(value="km_transactionManager")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "ServiceMonitorApplication")
@Remote
public class ServiceMonitorApplicationIpml implements ServiceMonitorApplication {

	@Override
	public List<ScheduleConfVo> queryAllSchedulers() {
		List<ScheduleConfVo> result = new ArrayList<ScheduleConfVo>();
		try {
			List<SchedulerConfg> schedulers = SchedulerConfg.getRepository().findAll(SchedulerConfg.class);
	        if(schedulers == null)return result;
	        ScheduleConfVo vo;
			for (SchedulerConfg schedulerConfg : schedulers) {
				vo = new ScheduleConfVo();
				BeanUtils.copyProperties(vo, schedulerConfg);
				result.add(vo);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public void updateScheduleConf(ScheduleConfVo confVo) {
		try {
			SchedulerConfg confg = SchedulerConfg.get(SchedulerConfg.class, confVo.getTriggerName());
			if(StringUtils.isNotBlank(confVo.getCronExpr())){
				confg.setCronExpr(confVo.getCronExpr());
			}else{
				confg.setActive(confVo.isActive());
			}
			confg.save();
		} catch (Exception e) {
			throw new RuntimeException("更新失败");
		}
	}

	@Override
	public ScheduleConfVo queryScheduler(String triggerName) {
		ScheduleConfVo result = new ScheduleConfVo();
		try {
			SchedulerConfg confg = SchedulerConfg.get(SchedulerConfg.class, triggerName);
			BeanUtils.copyProperties(result, confg);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}

}
