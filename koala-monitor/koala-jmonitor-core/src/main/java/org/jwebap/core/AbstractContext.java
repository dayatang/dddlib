package org.jwebap.core;

import org.openkoala.koala.monitor.jwebap.PropertyMap;
import org.openkoala.koala.monitor.jwebap.PropertyStorage;

/**
 * 抽象上下文
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date  2007-12-8
 */
public abstract class AbstractContext extends PropertyStorage implements Context{
	
	private Context _parent=null;
	
	public AbstractContext(){
	}
	
	public AbstractContext(Context parent){
		_parent=parent;
	}
	
	public AbstractContext(Context parent,PropertyMap map){	
		super(map);
		_parent=parent;
	}
	
	
	public Context getParent(){
		return _parent;
	}
}
