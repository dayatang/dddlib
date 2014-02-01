package org.dayatang.dsrouter.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.dayatang.dsrouter.context.memory.ContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleDynamicRoutingDataSource extends AbstractDataSource {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDynamicRoutingDataSource.class);

	private DataSource defaultDataSource;

	private Map<String, DataSource> dataSourceMapping = new HashMap<String, DataSource>();

	public DataSource getDefaultDataSource() {
		return defaultDataSource;
	}

	public void setDefaultDataSource(DataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	public Map<String, DataSource> getDataSourceMapping() {
		return dataSourceMapping;
	}

	public void setDataSourceMapping(Map<String, DataSource> dataSourceMapping) {
		this.dataSourceMapping = dataSourceMapping;
	}

	public void afterPropertiesSet() throws Exception {
		if (defaultDataSource != null) {
			debug("设置默认数据源【{}】", defaultDataSource);
		}
	}

	protected DataSource determineTargetDataSource() {
		try {
			return getDataSource();
		} catch (Exception ex) {
			warn("获取数据源发生异常，使用默认数据源【{}】", defaultDataSource);
			LOGGER.warn("异常为：" + ex);
			return defaultDataSource;
		}

	}

	protected DataSource getDataSource() {
		String dataSourceKey = ContextHolder.getContextType();

		DataSource dataSource = dataSourceMapping.get(dataSourceKey);

		if (dataSource == null) {
			debug("系统中不存在 dataSourceKey=【{}】对应的数据源，使用默认数据源【{}】", dataSourceKey, defaultDataSource);
			return defaultDataSource;
		}
		debug("系统中已存在 dataSourceKey=【{}】对应的数据源【{}】。", dataSourceKey, dataSource);
		return dataSource;
	}

	public Connection getConnection() throws SQLException {
		DataSource ds = determineTargetDataSource();
		Connection connection = ds.getConnection();
		// 注意：当使用c3p0的时候 不能在此处调用conn.getMetaData()方法 否则读写分离失效
		// debug("获取的连接对象为【{}】", connection.getMetaData().getURL());
		return connection;
	}

	public Connection getConnection(String username, String password) throws SQLException {
		Connection connection = determineTargetDataSource().getConnection(username, password);
		return connection;
	}

	private void debug(String message, Object... params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, params);
		}
	}

	private void warn(String message, Object... params) {
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn(message, params);
		}
	}

	//For JDK 7 compatability
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

}
