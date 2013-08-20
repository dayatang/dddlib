package org.openkoala.koala.monitor.component.tracer.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;

import javax.sql.DataSource;

import org.openkoala.koala.monitor.core.TraceLiftcycleManager;
import org.openkoala.koala.monitor.toolkit.asm.MethodInjectHandler;

/**
 * 数据库监控注入handle
 * 
 * @author leadyu
 * @since Jwebap 0.5
 * @date 2007-8-14
 */
public class JdbcDriverInjectHandle implements MethodInjectHandler {

	/**
	 * 运行轨迹容器
	 */
	private transient TraceLiftcycleManager _container = null;

	public JdbcDriverInjectHandle(TraceLiftcycleManager container) {
		_container = container;
	}

	public Object invoke(Object target, Method method, Method methodProxy,
			Object[] args) throws Throwable {
		Object o;
		try {
			o = methodProxy.invoke(target, args);
		} catch (InvocationTargetException e) {
			// 抛出原有异常
			throw e.getCause();
		}
		if (!Modifier.isPrivate(method.getModifiers())
				&& o instanceof Connection
				&& !(o instanceof ProxyConnection)) {
			return new ProxyConnection(_container, (Connection) o,(DataSource)target);
		} else if (!Modifier.isPrivate(method.getModifiers())
				&& o instanceof DataSource
				&& !(o instanceof ProxyDataSource)) {
			return new ProxyDataSource(_container, (DataSource) o,(DataSource)target);
		} else {
			return o;
		}

	}

}
