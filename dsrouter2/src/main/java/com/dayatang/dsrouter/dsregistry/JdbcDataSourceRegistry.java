package com.dayatang.dsrouter.dsregistry;

import java.util.Date;

import javax.sql.DataSource;

import com.dayatang.utils.Slf4jLogger;

/**
 * 当还没有对应于指定的租户的数据源时，即时创建一个。
 * @author yyang
 *
 */
public class JdbcDataSourceRegistry extends AbstractDataSourceRegistry {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(JdbcDataSourceRegistry.class);
	
	private DataSourceCreator dataSourceCreator;

	public JdbcDataSourceRegistry() {
	}

	public JdbcDataSourceRegistry(DataSourceCreator dataSourceCreator) {
		this.dataSourceCreator = dataSourceCreator;
	}

	@Override
	protected DataSource findOrCreateDataSourceForTenant(String tenant) {
		DataSource result = dataSourceCreator.createDataSourceForTenant(tenant);
		Date now = new Date();
		recordLastAccessTimeOfTenant(tenant, now);
		LOGGER.debug("Create data source for tenant '{}' at {}", tenant, now);
		return result;
	}
}
