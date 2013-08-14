package com.dayatang.datasource4saas.dscreator;

import javax.sql.DataSource;

import org.logicalcobwebs.proxool.ProxoolDataSource;

import com.dayatang.utils.Slf4jLogger;

/**
 * 基于Proxool连接池的数据源创建器
 * @author yyang
 *
 */
public class ProxoolDataSourceCreator extends AbstractDataSourceCreator {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(ProxoolDataSourceCreator.class);

	@Override
	public DataSource createDataSourceForTenant(String tenant) {
		try {
			ProxoolDataSource result = new ProxoolDataSource();
			fillProperties(result);
			JdbcConfiguration jdbcConfiguration = getJdbcConfiguration(tenant);
			result.setDriver(jdbcConfiguration.getDriverClassName());
			result.setDriverUrl(jdbcConfiguration.getUrl());
			result.setUser(jdbcConfiguration.getUsername());
			result.setPassword(jdbcConfiguration.getPassword());
			return result;
		} catch (Exception e) {
			String message = "Create Proxool data source failure.";
			LOGGER.error(message, e);
			throw new DataSourceCreationException(message, e);
		}
	}
}
