package com.dayatang.dsmonitor.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.dayatang.utils.Assert;

/**
 * 来自spring
 * 
 * @author chencao
 * 
 */
public class DelegatingDataSource implements DataSource {

	private DataSource targetDataSource;

	/**
	 * Create a new DelegatingDataSource.
	 * 
	 * @param targetDataSource
	 *            the target DataSource
	 */
	public DelegatingDataSource(DataSource targetDataSource) {
		Assert.notNull(targetDataSource, "'targetDataSource' must not be null");
		this.targetDataSource = targetDataSource;
	}

	/**
	 * Return the target DataSource that this DataSource should delegate to.
	 */
	public DataSource getTargetDataSource() {
		return this.targetDataSource;
	}

	public Connection getConnection() throws SQLException {
		return getTargetDataSource().getConnection();
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		return getTargetDataSource().getConnection(username, password);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return getTargetDataSource().getLogWriter();
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		getTargetDataSource().setLogWriter(out);
	}

	public int getLoginTimeout() throws SQLException {
		return getTargetDataSource().getLoginTimeout();
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		getTargetDataSource().setLoginTimeout(seconds);
	}

	// ---------------------------------------------------------------------
	// Implementation of JDBC 4.0's Wrapper interface
	// ---------------------------------------------------------------------

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return getTargetDataSource().unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return getTargetDataSource().isWrapperFor(iface);
	}

	//For JDK 7 compatability
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

}