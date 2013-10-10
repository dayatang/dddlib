package org.openkoala.koala.monitor.core;


/**
 * Component Plug-in初始化上下文
 * <p>
 * 可以获取Component启动所需的属性，这些属性由Component的扩展者定义，比如HttpComponent,包含属性：
 * 1)trace-max-size
 * 2)trace-filter-active-time
 * </p>
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date 2007-12-4
 * @see org.openkoala.koala.monitor.config.PropertyMap
 * @see org.openkoala.koala.monitor.core.TraceLiftcycleManager
 */
public interface ComponentContext extends Context{
	
	/**
	 * 获取轨迹容器
	 * 
	 * @return
	 */
	public TraceLiftcycleManager getContainer();
	
	public Component getComponent();
	
	public void setComponent(Component component);
	
	
}
