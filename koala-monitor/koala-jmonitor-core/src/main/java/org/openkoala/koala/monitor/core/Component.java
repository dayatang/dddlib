package org.openkoala.koala.monitor.core;


/**
 * 监控组件接口，实现它就可以集成于Jwebap框架之中
 * @author leadyu
 * @since Jwebap 0.5
 * @date  Aug 7, 2007
 */
public interface Component {
	
	/**
	 * plug-in启动方法，实现者可以在该方法中初始组件需要的资源
	 * 该方法由Jwebap启动时调用
	 * @param context
	 */
	public void startup(ComponentContext context);
	
	/**
	 * 该方法在Jwebap移除组件时调用，实现者可以在这里进行资源的回收
	 *
	 */
	public void destory();
	
	/**
	 * 该方法通常由用户在界面触发调用，用以清理一些组件内部使用的临时数据，以及统计数据，
	 * 清理完成，组件的状态应该归为刚刚启动时的状态
	 *
	 */
	public void clear();
	
	/**
	 * 获得组件上下文
	 *
	 */
	public ComponentContext getComponentContext();
	
	public void setTraceType(String traceType);
}
