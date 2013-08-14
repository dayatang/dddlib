package com.dayatang.datasource4saas.dscreator;

import javax.sql.DataSource;

import com.dayatang.utils.Slf4jLogger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 基于C3P0连接池的数据源创建器
 * @author yyang
 *
 */
public class C3P0DataSourceCreator extends AbstractDataSourceCreator {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(C3P0DataSourceCreator.class);

	@Override
	public DataSource createDataSourceForTenant(String tenant) {
		try {
			ComboPooledDataSource result = new ComboPooledDataSource();
			fillProperties(result);
			JdbcConfiguration jdbcConfiguration = getJdbcConfiguration(tenant);
			result.setDriverClass(jdbcConfiguration.getDriverClassName());
			result.setJdbcUrl(jdbcConfiguration.getUrl());
			result.setUser(jdbcConfiguration.getUsername());
			result.setPassword(jdbcConfiguration.getPassword());
			return result;
		} catch (Exception e) {
			String message = "Create C3P0 data source failure.";
			LOGGER.error(message, e);
			throw new DataSourceCreationException(message, e);
		}
	}

}
