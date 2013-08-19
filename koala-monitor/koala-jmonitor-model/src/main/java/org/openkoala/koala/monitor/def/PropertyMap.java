package org.openkoala.koala.monitor.def;

import java.util.Map;

/**
 * 参数集合对象，通常由字符串组成的name和value键值对组成
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date 2007-12-4
 */
public interface PropertyMap{

	public String getProperty(String key);

	public String[] propNames();
	
	public void putProperty(String key, String value);
	
	public void putProperties(Map<String, String> props);

}
