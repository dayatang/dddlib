package org.openkoala.koala.monitor.component.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.openkoala.koala.monitor.core.TraceLiftcycleManager;

/**
 * 代理DataSource
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date Mar 11, 2008
 */
public class ProxyDataSource implements DataSource {

	private DataSource outterDataSource;//最外层的
	private DataSource delegate;

	/**
	 * 运行轨迹容器
	 */
	private transient TraceLiftcycleManager container = null;

	public ProxyDataSource(TraceLiftcycleManager container,
			DataSource delegate,DataSource outterDataSource) {
		this.container = container;
		this.delegate = delegate;
		this.outterDataSource = outterDataSource;
	}

	public Connection getConnection() throws SQLException {
		Connection conn = delegate.getConnection();
		return getProxy(conn);
	}

	public Connection getConnection(String arg0, String arg1)
			throws SQLException {
		Connection conn = delegate.getConnection(arg0, arg1);
		return getProxy(conn);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return delegate.getLogWriter();
	}

	public int getLoginTimeout() throws SQLException {
		return delegate.getLoginTimeout();
	}

	public void setLogWriter(PrintWriter arg0) throws SQLException {
		delegate.setLogWriter(arg0);
	}

	public void setLoginTimeout(int arg0) throws SQLException {
		delegate.setLoginTimeout(arg0);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return delegate.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return delegate.isWrapperFor(iface);
	}

	//For JDK 7 compatability
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	/**
	 * 得到连接代理
	 * 
	 * @param conn
	 * @return
	 */
	private Connection getProxy(Connection conn) {
		if (!(conn instanceof ProxyConnection)) {
			return new ProxyConnection(container, conn,outterDataSource);
		}
		return conn;
	}
}
