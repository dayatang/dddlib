package org.openkoala.koala.monitor.core;

import org.openkoala.koala.monitor.component.http.HttpComponent;
import org.openkoala.koala.monitor.component.jdbc.JdbcComponent;
import org.openkoala.koala.monitor.component.method.MethodComponent;
import org.openkoala.koala.monitor.constant.E_TraceType;


/**
 * 监控组件工厂
 * @author Administrator
 *
 */
public class ComponentFactory {
	
	public static Component getInstance(String type){
		Component component = null;
		if(E_TraceType.HTTP.name().equals(type)){
			component = new HttpComponent();
		}else if(E_TraceType.METHOD.name().equals(type)){
			component = new MethodComponent();
		}else if(E_TraceType.JDBC.name().equals(type)){
			component = new JdbcComponent();
		}
		return component;
	}
}
