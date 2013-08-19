package org.openkoala.koala.monitor.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.openkoala.koala.monitor.jwebap.CombineMethodTrace;
import org.openkoala.koala.monitor.jwebap.MethodTrace;
import org.openkoala.koala.monitor.jwebap.Trace;

/**
 * 
 * 功能描述：轨迹数据生命周期管理<br />
 *  
 * 创建日期：2013-6-25 上午11:29:51  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class TraceContainer implements TraceLiftcycleManager{
	
	private static ThreadLocal<String> threads = new ThreadLocal<String>();
	
	protected Map<String ,List<Analyser>> analysers=null;
	
	private abstract class DoProcess{
		void doProcess(String traceType, Trace trace){
			Collection<Analyser> as= analysers.get(traceType);
			if(as==null){
				return;
			}
			Iterator<Analyser> it=as.iterator();
			while(it.hasNext()){
				Analyser analyser=it.next();
				if(trace.isIgnore())continue;
				doEvent(analyser,trace);
			}
		}
		abstract void doEvent(Analyser analyser,Trace trace);
	};
	
	public TraceContainer(){
		analysers=new HashMap<String ,List<Analyser>>();
	}
	
	public void activateTrace(String traceType, Trace trace) {
		
		if(!RuntimeContext.componentIsActive(traceType))return;
		//设置线程标识
		if(trace.getThreadId() == null){
			trace.setThreadId(getCurrentThreadKey());
		}
		trace.setTraceType(traceType);
		
		DoProcess process=new DoProcess(){
			void doEvent(Analyser analyser,Trace trace){
				analyser.activeProcess(trace);
			}		
		};
		process.doProcess(traceType,trace);	
	}

	public void inactivateTrace(String traceType,Trace trace) {
		
		if(!RuntimeContext.componentIsActive(traceType))return;
		
		DoProcess process=new DoProcess(){
			void doEvent(Analyser analyser,Trace trace){
				analyser.inactiveProcess(trace);
			}		
		};
		process.doProcess(traceType,trace);	
		
		//跳过忽略
		if(trace.isIgnore())return;
		//放入交换数据缓存区
		
		//
		if(trace instanceof MethodTrace){
			CombineMethodTrace combineMethodTrace = new CombineMethodTrace();
			combineMethodTrace.combineTraceInfo((MethodTrace)trace);
			RuntimeContext.getContext().getDataCache().push(combineMethodTrace);
		}else{
			RuntimeContext.getContext().getDataCache().push(trace);
		}
		
		//标记为忽略
		trace.setIgnore(true);
	}


	public void destoryTrace(String traceType,Trace trace) {
		DoProcess process=new DoProcess(){
			void doEvent(Analyser analyser,Trace trace){
				analyser.destoryProcess(trace);
			}		
		};
		process.doProcess(traceType,trace);	
	}

	public void registerAnalyser(String traceType,Analyser analyser) {
		if(analysers.get(traceType)==null){
			analysers.put(traceType,new ArrayList<Analyser>());
		}
		Collection<Analyser> as= analysers.get(traceType);
		as.add(analyser);
	}

	public void unregisterAnalyser(String traceType,Analyser analyser) {
		Collection<Analyser> as= analysers.get(traceType);
		if(as!=null){
			as.remove(analyser);
		}
	}
	

	/**
	 * 获取当前线程标识
	 * @return
	 */
	public static String getCurrentThreadKey(){
		String key = threads.get();
		if(key == null){
			key = UUID.randomUUID().toString().replaceAll("\\-", "");
			threads.set(key);
		}
		return key;
	}
	
	public static void clearThreadKey(){
		threads.set(null);
	}
	
}
