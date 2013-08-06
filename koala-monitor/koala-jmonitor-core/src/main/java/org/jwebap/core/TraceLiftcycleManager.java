package org.jwebap.core;

import org.openkoala.koala.monitor.jwebap.Trace;


/**
 * 轨迹收集器，这个类的设计关乎整个组件的性能
 * @author leadyu
 * @since Jwebap 0.5
 * @date  2007-8-8
 */
public interface TraceLiftcycleManager {
	public void activateTrace(String traceType,Trace trace);
	public void inactivateTrace(String traceType,Trace trace);
	public void destoryTrace(String traceType,Trace trace);
	public void registerAnalyser(String traceType,Analyser analyser);
	public void unregisterAnalyser(String traceType,Analyser analyser);

}
