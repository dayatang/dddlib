package org.openkoala.koala.monitor.def;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 轨迹基类
 * 
 * 在Jwebap中轨迹类是一个重要的核心模型。
 * 
 * <p>
 * 轨迹的定义
 * 
 * 轨迹用于封装对性能分析有用的信息（比如上下文的信息，线程堆栈的信息，执 行时间的信息等等）。
 * 
 * 在系统中，任何程序的执行都有可能留下轨迹（数据库的调用会留下SQL轨迹， 事务的轨迹，http请求会留下访问的轨迹，程序的方法执行也可以留下轨迹）。
 * 
 * 轨迹之间存在关系，比如数据库事务的轨迹和SQL的轨迹，http请求和类方法 执行的轨迹之间都有一定的关系。Jwebap中通过字节码注入，收集运行时的信
 * 息，封装成轨迹，供分析器(Analyser)进行统计。
 * </p>
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date 2007-04-11
 */
public class Trace implements Serializable, Comparable<Trace>,Cloneable{
	
	private static final long serialVersionUID = 1L;

	//trace 标识
	protected String innerKey;
	//同一个线程该值相同）
	protected String threadKey;//线程ID
	
	protected String traceType;

	/**
	 * 创建时间
	 */
	protected long createdTime;

	/**
	 * 不活动时间
	 */
	protected long inActiveTime;

	/**
	 * 轨迹内容
	 */
	protected String content;

	/**
	 * 父轨迹
	 */
	protected Trace parent;
	
	protected boolean timeout;//标记为超时
	
	protected boolean unrecovered = false;//标记为未回收

	/**
	 * 子轨迹
	 */
	protected List<Trace> traces;

	public Trace() {
		init(parent);
	}

	public Trace(Trace parent) {
		init(parent);
	}

	private void init(Trace parent) {
		if (parent != null)
			parent.addChild(this);
		this.parent = parent;
		createdTime = System.currentTimeMillis();
		innerKey = UUID.randomUUID().toString().replaceAll("\\-", "");
	}
	

	/**
	 * 增加子轨迹
	 * 
	 * @param trace
	 */
	public void addChild(Trace trace) {
		if(this.traces == null)this.traces = new ArrayList<Trace>();
		synchronized (traces) {
			this.traces.add(trace);
		}
	}


	/**
	 * 得到子轨迹
	 * 
	 * 返回定义为数组有2个目的:
	 * 
	 * 1)返回复本，保证外在的操作不会侵入轨迹的内部运行
	 * 
	 * 2)返回类型为数组，更不易产生歧义，误以为得到的是轨迹内部的集合结构
	 * 
	 * @return
	 */
	public Trace[] getChildTraces() {
		Trace[] ts = null;
		if(traces != null){
			ts = new Trace[traces.size()];
			traces.toArray(ts);
		}else{
			ts = new Trace[0];
		}
		return ts;
	}

	/**
	 * 轨迹存活时间
	 * 
	 * @return
	 */
	public long getActiveTime() {
		return getInactiveTime() <= 0L ? System.currentTimeMillis()
				- getCreatedTime() : getInactiveTime() - getCreatedTime();
	}
	
	/**
	 * 耗时
	 * @return
	 */
	public long getTimeConsume() {
		return getActiveTime();
	}

	/**
	 * 清空子轨迹
	 */
	public synchronized void clearChildTrace() {
		if (traces != null)
			traces.clear();
	}

	/**
	 * 删除子轨迹
	 * 
	 * @param trace
	 */
	protected synchronized void removeChildTrace(Trace trace) {
		if (this.traces != null) {
			this.traces.remove(trace);
			trace.parent = null;
		}
	}

	/**
	 * 销毁轨迹
	 */
	public void destroy() {
		if (parent != null) {
			parent.removeChildTrace(this);
			parent = null;
		}
		Trace[] children = getChildTraces();
		clearChildTrace();
		for (int i = 0; i < children.length; i++) {
			Trace trace = children[i];
			trace.destroy();
		}
	}

	/**
	 * 不活动
	 */
	public void inActive() {
		if (!(inActiveTime > 0L))
			inActiveTime = System.currentTimeMillis();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getInactiveTime() {
		return inActiveTime;
	}

	public long getCreatedTime() {
		return createdTime;
	}
	
	public String getInnerKey() {
		return innerKey;
	}

	public void setInnerKey(String innerKey) {
		this.innerKey = innerKey;
	}

	public String getThreadKey() {
		return threadKey;
	}

	public void setThreadKey(String threadKey) {
		this.threadKey = threadKey;
	}

	public String getTraceType() {
		return traceType;
	}

	public void setTraceType(String traceType) {
		this.traceType = traceType;
	}

	
	public Date getBeginTime() {
		return new Date(getCreatedTime());
	}
	
	
	public Date getEndTime() {
		return new Date(getInactiveTime());
	}
	

	public boolean isTimeout() {
		return timeout;
	}

	public void setTimeout(boolean timeout) {
		this.timeout = timeout;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((innerKey == null) ? 0 : innerKey.hashCode());
		return result;
	}
	

	public boolean isUnrecovered() {
		return unrecovered;
	}

	public void setUnrecovered(boolean unrecovered) {
		this.unrecovered = unrecovered;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trace other = (Trace) obj;
		if (innerKey == null) {
			if (other.innerKey != null)
				return false;
		} else if (!innerKey.equals(other.innerKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int compareTo(Trace o) {
		return (int)(this.createdTime - o.getCreatedTime());
	}
	
	public Trace clone(){
		try {
			return (Trace) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
}
