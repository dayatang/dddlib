package org.jwebap.plugin.tracer.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;

import javax.sql.DataSource;

import org.jwebap.core.TraceLiftcycleManager;
import org.jwebap.toolkit.bytecode.asm.MethodInjectHandler;

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

	private ConnectionEventListener[] _listeners = null;

	public JdbcDriverInjectHandle(TraceLiftcycleManager container,
			ConnectionEventListener[] listeners) {
		_container = container;
		
		_listeners =new ConnectionEventListener[listeners.length];
		System.arraycopy(listeners, 0, _listeners, 0, listeners.length);
		
	}

	public Object invoke(Object target, Method method, Method methodProxy,
			Object[] args) throws Throwable {
		Object o;
		try {
			o = methodProxy.invoke(target, args);
		} catch (InvocationTargetException e) {
			// 抛出原有异常
			throw e.getCause();
		} finally {

		}
		if (!Modifier.isPrivate(method.getModifiers())
				&& o instanceof Connection
				&& !(o instanceof ProxyConnection)) {
			return new ProxyConnection(_container, (Connection) o,(DataSource)target,_listeners);
		} else if (!Modifier.isPrivate(method.getModifiers())
				&& o instanceof DataSource
				&& !(o instanceof ProxyDataSource)) {
			return new ProxyDataSource(_container, (DataSource) o,(DataSource)target,_listeners);
		} else {
			return o;
		}

	}

}
