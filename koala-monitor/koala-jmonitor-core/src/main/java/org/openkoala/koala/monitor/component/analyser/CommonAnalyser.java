package org.openkoala.koala.monitor.component.analyser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openkoala.koala.monitor.component.tracer.http.HttpComponent;
import org.openkoala.koala.monitor.component.tracer.method.MethodComponent;
import org.openkoala.koala.monitor.core.Analyser;
import org.openkoala.koala.monitor.core.RuntimeContext;
import org.openkoala.koala.monitor.core.TraceContainer;
import org.openkoala.koala.monitor.def.MethodTrace;
import org.openkoala.koala.monitor.def.Trace;
/**
 * 
 * 功能描述：通用分析器（耗时、超时等）<br />
 *  
 * 创建日期：2013-7-1 上午11:07:20  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class CommonAnalyser implements Analyser{
	
	protected String traceType;

	private List<Trace> traces;
	
	//每个主线程调用的第一个方法集合
	private static Map<String, String> methodEnpoints = new ConcurrentHashMap<String, String>();

	public CommonAnalyser(String traceType) {
		this.traceType = traceType;
		this.traces = new ArrayList<Trace>();
	}
	
	public void activeProcess(Trace trace) {
		synchronized (traces) {
			//处理超时trace
			for (Trace _trace : traces) {
				if(System.currentTimeMillis() - _trace.getCreatedTime()>getTraceTimeout()){
					_trace.setTimeout(true);
					_trace.inActive();
					methodEnpoints.remove(trace.getThreadId());
					RuntimeContext.getContext().getContainer().inactivateTrace(traceType, _trace);
				}else{
					break;
				}
			}
			
			traces.add(trace);
			//记录入口方法
			if(MethodComponent.TRACE_TYPE.equals(trace.getTraceType())){
				if(!methodEnpoints.containsKey(trace.getTraceKey())){
					methodEnpoints.put(trace.getTraceKey(), ((MethodTrace)trace).getMethod());
				}
			}
		}
	}

	public void inactiveProcess(Trace trace) {
		long activeTime = System.currentTimeMillis() - trace.getCreatedTime();
		long tracefilterActivetime = getTracefilterActivetime();
		if (tracefilterActivetime >= activeTime){
			//发生异常的方法也需要继续处理
			if(MethodComponent.TRACE_TYPE.equals(trace.getTraceType()) && ((MethodTrace)trace).isSuccessed()==false){
				trace.inActive();
			}else{
				trace.destroy();
			}
		}else{
			trace.inActive();
		}
		synchronized (traces) {
			traces.remove(trace);
		}
		
		//http请求结束 释放
		if(HttpComponent.TRACE_TYPE.equals(trace.getTraceType())){
			methodEnpoints.remove(trace.getThreadId());
			TraceContainer.clearThreadKey();
		}
	}
	
	public void destoryProcess(Trace trace) {
				
	}
	
	/**
	 * 清空统计数据
	 *
	 */
	public void clear() {
		traces.clear();	
	}

	
	private long getTracefilterActivetime() {
		String value = RuntimeContext.getComponentProps(traceType, "trace-filter-active-time");
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			return -1;
		}
	}
	
	private long getTraceTimeout() {
		String value = RuntimeContext.getComponentProps(traceType, "trace-timeout");
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			return 60*1000;
		}
	}
	
	public static String getTraceEndpointMethod(String traceKey){
		return methodEnpoints.get(traceKey);
	}

	public static void removeTraceEndpointMethod(String traceKey){
		methodEnpoints.remove(traceKey);
	}
}