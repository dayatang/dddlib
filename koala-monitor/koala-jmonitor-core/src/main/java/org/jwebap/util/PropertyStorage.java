package org.jwebap.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 参数集合实现类
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.6
 * @date  2008-10-6
 */
public class PropertyStorage implements PropertyMap{
	/**
	 *参数Map 
	 */
	protected Map<String, String> properties=new HashMap<String, String>();

	public PropertyStorage() {}

	public PropertyStorage(Map<String, String> map){
		properties=map;
	}
	
	public PropertyStorage(PropertyMap map) {
		String[] names=map.propNames();
		for(int i=0;i<names.length;i++){
			putProperty(names[i],map.getProperty(names[i]));
		}
	}
	
	
	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	/**
	 * 返回参数值，找不到时返回空串
	 */
	public String getProperty(String key) {
		String value=(String)properties.get(key);
		return value==null?"":value;
	}

	/**
	 * 设置参数
	 */
	public void putProperty(String key, String value) {
		properties.put(key,value);
	}
	
	/**
	 * 返回所有参数名
	 */
	public String[] propNames(){
		String[] keys=new String[properties.size()];
		Set<String> keySet=properties.keySet();
		keySet.toArray(keys);
		return keys;
	}

	@Override
	public void putProperties(Map<String, String> props) {
		properties.putAll(props);
	}

	@Override
	public void refreshProps() {
		// TODO Auto-generated method stub
		
	}

}