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
package org.openkoala.koala.monitor.task;

import java.io.FileInputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.openkoala.koala.monitor.common.KoalaDateUtils;
import org.openkoala.koala.monitor.core.RuntimeContext;
import org.openkoala.koala.monitor.def.ServiceDef;
import org.openkoala.koala.monitor.def.TaskDef;
import org.openkoala.koala.monitor.model.CheckResult;
import org.openkoala.koala.monitor.support.ConnectionChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-7-3 下午4:09:05  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class ServiceConnectionCheckTask extends BaseMonitorTask{

	private static final Logger LOG = LoggerFactory.getLogger(ServiceConnectionCheckTask.class);
	
	public final static String TASK_KEY = "CHECK-CONNECTION"; 
	
	//上一次检测时间
	private Date lastCheckTime = new Date();
	
	//上一次检查不正常服务
	private List<CheckResult> badSevices = new Vector<CheckResult>();
	
	private List<ServiceDef> serviceList;
	
	
	public ServiceConnectionCheckTask(TaskDef taskDef){
		initServiceList(taskDef.getTaskResource());
		this.taskPeriod = taskDef.getPeriod() * 1000 * 1000;//分 -->毫秒
	}

	/**
	 * @param taskResource
	 */
	@SuppressWarnings("unchecked")
	private void initServiceList(String taskResource) {
		try {
			if(taskResource == null){
				LOG.warn("服务列表配置文件不存在");
				return;
			}
			
			if(taskResource.startsWith("classpath")){
				taskResource = taskResource.replace("classpath", "").replace("*:", "");
				URL url = Thread.currentThread().getContextClassLoader().getResource(taskResource);
				if(url == null){
					LOG.warn("服务列表配置文件[{}]不存在",taskResource);
					return;
				}
				taskResource = url.getFile();
			}else if(taskResource.startsWith("/WEB-INF")){
				String rootPath = RuntimeContext.getContext().getDeployPath();
				if(rootPath.endsWith("/")){
					rootPath = rootPath.substring(0, rootPath.length() - 1);
				}
				taskResource = rootPath + taskResource;
			}
			XStream xStream  = new XStream( new  DomDriver("utf-8"));
			xStream.alias( "service" , ServiceDef.class ); 
			xStream.alias( "services" , List.class ); 
			FileInputStream inputStream = new FileInputStream(taskResource);
			serviceList = (List<ServiceDef>)xStream.fromXML(inputStream);
		} catch (Exception e) {
			LOG.warn("解析监控第三方服务列表失败");
		}
	}
	
	

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean initConfigOK() {
		return serviceList != null && !serviceList.isEmpty();
	}

	@Override
	protected void doTask() {
		badSevices.clear();
		//
		for (ServiceDef serviceDef : serviceList) {
			CheckResult result = ConnectionChecker.checkConnectionStatus(serviceDef);
			if(!result.isSuccess()){
				badSevices.add(result);
			}
		}
		
		lastCheckTime = new Date();
	}

	@Override
	public String getDatas() {
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("checkTime", KoalaDateUtils.format(lastCheckTime));
		datas.put("badSevices", badSevices);
		return JSON.toJSONString(datas);
	}
	

	public static void main(String[] args) {
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("checkTime", new Date());
		List<CheckResult> badSevices = new Vector<CheckResult>();
		CheckResult r = new CheckResult();
		r.setTimeConsuming(100L);
		badSevices.add(r);
		
		r = new CheckResult();
		r.setTimeConsuming(200L);
		r.setDetails("haha");
		badSevices.add(r);
		
		datas.put("badSevices", badSevices);
		
		System.out.println(JSON.toJSONString(datas));
	}
}
