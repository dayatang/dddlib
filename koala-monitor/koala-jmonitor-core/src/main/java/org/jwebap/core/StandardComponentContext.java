package org.jwebap.core;

import org.openkoala.koala.monitor.jwebap.ComponentDef;

/**
 * ComponentContext标准实现
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date  2007-12-8
 */
public class StandardComponentContext extends AbstractContext implements ComponentContext{

	/**
	 * 轨迹容器
	 */
	private TraceLiftcycleManager _container=null;
	
	private Component _component=null;
	
	public Component getComponent() {
		return _component;
	}

	public void setComponent(Component component) {
		this._component = component;
	}

	public StandardComponentContext(TraceLiftcycleManager container,Context parent,ComponentDef def){
		super(parent,def);
		_container=container;
	}
	
	
	
	public TraceLiftcycleManager getContainer() {
		return _container;
	}

	


}
