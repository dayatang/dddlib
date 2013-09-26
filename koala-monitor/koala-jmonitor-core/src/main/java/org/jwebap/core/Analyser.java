package org.jwebap.core;

import org.openkoala.koala.monitor.jwebap.Trace;


/**
 * 轨迹分析器，轨迹收集器TraceContainer在轨迹的产生的生命过程中
 * 会不断向各个分析器广播事件，由分析器完成轨迹的统计，保存统计结果
 * @author leadyu
 * @since Jwebap 0.5
 * @date  Aug 7, 2007
 */
public interface Analyser {
	
	public void activeProcess(Trace trace);

	public void inactiveProcess(Trace trace);

	public void destoryProcess(Trace trace);

	public void clear();
}
