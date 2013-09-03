package org.openkoala.koala.monitor.core;

import java.util.List;
import java.util.Map;

import org.openkoala.koala.monitor.def.HttpRequestTrace;
import org.openkoala.koala.monitor.def.Trace;
import org.openkoala.koala.monitor.def.HttpRequestTrace.ActiveUser;


/**
 * 功能描述：缓存数据池接口<br />
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
public interface CacheDataPool {
	
	
	public void push(Trace trace);
	
	public void pushAll(Map<String, List<Trace>> traces);
	
	public Map<String, List<Trace>> getAllCacheTrace();
	
	public List<Trace> getTraces(String traceKey);
	/**
	 * 刷新当前激活会话列表
	 * @param sessionId
	 */
	public void refreshActiveSessions(HttpRequestTrace trace);
	
	public Map<String, ActiveUser> getActiveSessions();

}
