package org.openkoala.koala.monitor.core;

import java.util.HashMap;
import java.util.Map;

import org.openkoala.koala.monitor.def.PropertyMap;

/**
 * 抽象上下文
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date  2007-12-8
 */
public abstract class AbstractContext implements Context{
	
	private Context _parent=null;
	
	protected Map<String, String> properties=new HashMap<String, String>();
	
	public AbstractContext(){
	}
	
	public AbstractContext(Context parent){
		_parent=parent;
	}
	
	public AbstractContext(Context parent,PropertyMap map){	
		_parent=parent;

		String[] names=map.propNames();
		for(int i=0;i<names.length;i++){
			properties.put(names[i],map.getProperty(names[i]));
		}
	
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}

	public void initProperties(Map<String, String> properties) {
		if(properties != null){
			this.properties.putAll(properties);
		}
	}
	
	public void addProperty(String propName,String propValue){
		properties.put(propName, propValue);
	}
	
	public String getProperty(String propName){
		return properties.get(propName);
	}

	public Context getParent(){
		return _parent;
	}
}
