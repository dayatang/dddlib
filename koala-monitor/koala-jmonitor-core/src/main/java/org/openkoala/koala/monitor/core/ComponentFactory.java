package org.openkoala.koala.monitor.core;

import org.openkoala.koala.monitor.component.http.HttpComponent;
import org.openkoala.koala.monitor.component.jdbc.JdbcComponent;
import org.openkoala.koala.monitor.component.method.MethodComponent;


/**
 * 监控组件工厂
 * @author Administrator
 *
 */
public class ComponentFactory {
	
	public static Component getInstance(String type){
		Component component = null;
		if(HttpComponent.TRACE_TYPE.equals(type)){
			component = new HttpComponent();
		}else if(MethodComponent.TRACE_TYPE.equals(type)){
			component = new MethodComponent();
		}else if(JdbcComponent.TRACE_TYPE.equals(type)){
			component = new JdbcComponent();
		}
		return component;
	}
}
