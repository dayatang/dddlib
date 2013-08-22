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
package org.openkoala.koala.monitor.component.tracer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.time.DateUtils;
import org.openkoala.koala.monitor.def.HttpRequestTrace;
import org.openkoala.koala.monitor.def.Trace;
import org.openkoala.koala.monitor.def.HttpRequestTrace.ActiveUser;

/**
 * 功能描述：缓存数据池（用于轨迹收集器与数据处理服务之间）<br />
 *  
 * 创建日期：2013-5-30 上午11:02:34  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class CacheDataPool {

	/**
	 * 	缓存最大记录数
	 */
	private int maxCacheSize = 10000;
	
	/**
	 * 	过期时间（单位：分）
	 */
	private  int expireTime = 15;
	
	/**
	 * 当前活动用户
	 */
	private static final Map<String, ActiveUser> activeSessions = new TreeMap<String,ActiveUser>();
	
	
	
	/**
	 * 待同步监控数据缓存
	 */
	private static final Map<String, List<Trace>> cache = new ConcurrentHashMap<String,List<Trace>>();
	
	
	/**
	 * @param maxCacheSize
	 * @param expireTime
	 */
	public CacheDataPool(int maxCacheSize, int expireTime) {
		super();
		this.maxCacheSize = maxCacheSize;
		this.expireTime = expireTime;
	}
	
	/**
	 * 刷新当前激活会话列表
	 * @param sessionId
	 */
	public void refreshActiveSessions(HttpRequestTrace trace){
		synchronized (activeSessions) {
			
			Iterator<ActiveUser> iterator = activeSessions.values().iterator();
			while(iterator.hasNext()){
				ActiveUser user = iterator.next();
				if(DateUtils.addMinutes(user.getActiveTime(), expireTime).after(new Date()))
					break;
				if(!user.getIp().equals(trace.getIp())){
					iterator.remove();
				}
			}
			
			ActiveUser currentUser=activeSessions.remove(trace.getIp());
			if(currentUser == null){
				currentUser = new ActiveUser();
				currentUser.setIp(trace.getIp());
				currentUser.setBeginTime(trace.getBeginTime());
			}
			currentUser.setActiveTime(trace.getEndTime());
			currentUser.setPrincipal(trace.getPrincipal());
			currentUser.setReferer(trace.getReferer());
			currentUser.addCount();
			activeSessions.put(trace.getIp(), currentUser);
		}
	}

	/**
	 * 
	 * @param trace
	 */
    public void push(Trace trace){
    	
    	if(cache.size()>maxCacheSize){
    		clearExpireTrace();
    	}
    	
    	List<Trace> list = cache.get(trace.getThreadId());
    	if(list == null){
    		list = new ArrayList<Trace>();
    		cache.put(trace.getThreadId(), list);
    	}
    	list.add(trace);
    }
    

	public void pushAll(Map<String, List<Trace>> traces){
    	cache.putAll(traces);
    }
   
	/**
	 * 获取当前缓存中所有监控数据
	 * @return
	 */
    public Map<String, List<Trace>> getAllCacheTrace(){
    	Map<String, List<Trace>> tmps = null; 
    	synchronized (cache) {
    		tmps = new HashMap<String,List<Trace>>(cache); 
    		cache.clear();
		}
    	return tmps;
    }
    
    /**
     * 根据指定线程所有trace
     * @param traceKey
     * @return
     */
    public List<Trace> getTraces(String traceKey){
    	return cache.get(traceKey);
    }
    
    /**
     * 获取当前活动用户
     * @return
     */
    public Map<String, ActiveUser> getActiveSessions() {
		return activeSessions;
	}

	/**
	 * 清理过期数据
	 */
	private void clearExpireTrace() {
		Iterator<List<Trace>> its = cache.values().iterator();
		while(its.hasNext()){
			Trace trace = its.next().get(0);
			if(trace.getBeginTime().before(DateUtils.addMinutes(new Date(), 0 - expireTime))){
				its.remove();
			}
		}
		
	}
	
}
