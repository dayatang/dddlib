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
package org.openkoala.koala.monitor.model;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openkoala.koala.monitor.jwebap.NetTransObject;

/**
 * 功能描述：综合监控状态信息<br />
 *  
 * 创建日期：2013-6-26 下午2:31:14  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class GeneralMonitorStatusVo extends NetTransObject {

	private static final long serialVersionUID = 1L;
	
	private int activeCount;//在线人数
	
	//异常IP
	private List<String> abnormalIps = new ArrayList<String>();
	
	//服务
	private List<String> abnormalServices = new ArrayList<String>();
	private String serviceCheckTime;


	private double pageAvgResponseTime;//页面平均响应速度
    
	private String maxAvgTimePage;//平均耗时最长页面
    
	private int methodCallCount;//方法调用次数
	private int methodExceptionCount;//异常次数
    
	private String mostCallMethod;//调用最多方法
    
	private String maxAvgTimeMethod;//平均耗时最长方法
	
	private ServerStatusVo serverStatus;
	


	public int getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(int activeCount) {
		this.activeCount = activeCount;
	}

	public List<String> getAbnormalIps() {
		return abnormalIps;
	}

	public void setAbnormalIps(List<String> abnormalIps) {
		this.abnormalIps = abnormalIps;
	}

	public List<String> getAbnormalServices() {
		return abnormalServices;
	}

	public void setAbnormalServices(List<String> abnormalServices) {
		this.abnormalServices = abnormalServices;
	}

	public double getPageAvgResponseTime() {
		return pageAvgResponseTime;
	}

	public void setPageAvgResponseTime(double pageAvgResponseTime) {
		this.pageAvgResponseTime = pageAvgResponseTime;
	}

	public String getMaxAvgTimePage() {
		return maxAvgTimePage;
	}

	public void setMaxAvgTimePage(String maxAvgTimePage) {
		this.maxAvgTimePage = maxAvgTimePage;
	}

	public int getMethodCallCount() {
		return methodCallCount;
	}

	public void setMethodCallCount(int methodCallCount) {
		this.methodCallCount = methodCallCount;
	}

	public int getMethodExceptionCount() {
		return methodExceptionCount;
	}

	public void setMethodExceptionCount(int methodExceptionCount) {
		this.methodExceptionCount = methodExceptionCount;
	}

	public String getMostCallMethod() {
		return mostCallMethod;
	}

	public void setMostCallMethod(String mostCallMethod) {
		this.mostCallMethod = mostCallMethod;
	}

	public String getMaxAvgTimeMethod() {
		return maxAvgTimeMethod;
	}

	public void setMaxAvgTimeMethod(String maxAvgTimeMethod) {
		this.maxAvgTimeMethod = maxAvgTimeMethod;
	}

	public ServerStatusVo getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(ServerStatusVo serverStatus) {
		this.serverStatus = serverStatus;
	}
	
	public String getServiceCheckTime() {
		return serviceCheckTime;
	}

	public void setServiceCheckTime(String serviceCheckTime) {
		this.serviceCheckTime = serviceCheckTime;
	}

	/**
	 * 异常率
	 * @return
	 */
	public String getExceptionRate(){
		if(methodExceptionCount == 0)return "0%";
		return new BigDecimal(methodExceptionCount * 100 / methodCallCount).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() + "%" ; 
	}
	
	
	public Map<String, String> getServerInfo(){
		Map<String, String> map = new HashMap<String, String>();
		
		if(serverStatus != null){
			map.put("cpu", MessageFormat.format("使用率:{0}", serverStatus.getCpuUsage()));
			map.put("mem", MessageFormat.format("使用率:{0},空闲：{1} MB", serverStatus.getMemUsage(),serverStatus.getFreeMem()));
			
		}
		
		return map;
	}
	
	public Map<String, Object> formatAsMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("activeCount", activeCount);
		map.put("serviceCheckTime", serviceCheckTime);
		map.put("pageAvgResponseTime", pageAvgResponseTime);
		map.put("maxAvgTimePage", maxAvgTimePage);
		map.put("methodCallCount", methodCallCount);
		map.put("methodExceptionCount", methodExceptionCount);
		map.put("mostCallMethod", mostCallMethod);
		map.put("maxAvgTimeMethod", maxAvgTimeMethod);
		map.put("exceptionRate", getExceptionRate());
		if(abnormalIps != null && abnormalIps.size()>0){
			map.put("abnormalIps", abnormalIps);
		}
		if(abnormalServices != null && abnormalServices.size()>0){
			map.put("abnormalServices", abnormalServices);
		}
		if(serverStatus != null){
			map.put("serverInfo", getServerInfo());
		}
		return map;
		
    } 
}
