package com.dayatang.dsrouter.dscreator;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.dayatang.utils.Slf4jLogger;

public class CommonsDbcpDataSourceCreator extends AbstractDataSourceCreator {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(CommonsDbcpDataSourceCreator.class);

	@Override
	public DataSource createDataSourceForTenant(String tenant) {
		try {
			BasicDataSource result = new BasicDataSource();
			fillProperties(result);
			JdbcConfiguration jdbcConfiguration = getJdbcConfiguration(tenant);
			result.setDriverClassName(jdbcConfiguration.getDriverClassName());
			result.setUrl(jdbcConfiguration.getUrl());
			result.setUsername(jdbcConfiguration.getUsername());
			result.setPassword(jdbcConfiguration.getPassword());
			return result;
		} catch (Exception e) {
			String message = "Create Commons DBCP data source failure.";
			LOGGER.error(message, e);
			throw new DataSourceCreationException(message, e);
		}
	}
}
