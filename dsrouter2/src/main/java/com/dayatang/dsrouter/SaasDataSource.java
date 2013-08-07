package com.dayatang.dsrouter;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.dayatang.utils.Assert;

/**
 * 用于SaaS的数据源实现。是本项目的中心类。它作为代理，自动为不同的租户分配不同的实际数据源。
 * @author yyang
 *
 */
public class SaasDataSource implements DataSource {

	private TenantService tenantService;
	private DataSourceRegistry dataSourceRegistry;

	public SaasDataSource(TenantService tenantService, DataSourceRegistry dataSourceRegistry) {
		Assert.notNull(dataSourceRegistry, "data source registry is null!");
		Assert.notNull(tenantService, "Tenant service is null!");
		this.tenantService = tenantService;
		this.dataSourceRegistry = dataSourceRegistry;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return getActualDataSource().getLogWriter();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return getActualDataSource().getLoginTimeout();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		getActualDataSource().setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		getActualDataSource().setLoginTimeout(seconds);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return getActualDataSource().isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return getActualDataSource().unwrap(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return getActualDataSource().getConnection();
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		return getActualDataSource().getConnection(username, password);
	}

	private DataSource getActualDataSource() {
		return dataSourceRegistry.getDataSourceOfTenant(tenantService.getTenant());
	}

	//For JDK 7 compatability
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
	
}
