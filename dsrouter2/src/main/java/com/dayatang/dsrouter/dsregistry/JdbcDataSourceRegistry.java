package com.dayatang.dsrouter.dsregistry;

import java.util.Date;

import javax.sql.DataSource;

import com.dayatang.utils.Assert;
import com.dayatang.utils.Slf4jLogger;

/**
 * 当还没有对应于指定的租户的数据源时，即时创建一个。
 * @author yyang
 *
 */
public class JdbcDataSourceRegistry extends MapBasedDataSourceRegistry {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(JdbcDataSourceRegistry.class);
	
	private DataSourceCreator dataSourceCreator;

	public JdbcDataSourceRegistry(DataSourceCreator dataSourceCreator) {
		Assert.notNull(dataSourceCreator, "Data Source Creator is null!");
		this.dataSourceCreator = dataSourceCreator;
	}

	@Override
	protected DataSource findOrCreateDataSourceForTenant(String tenant) {
		Assert.notNull(tenant, "Tenant is null!");
		DataSource result = dataSourceCreator.createDataSourceForTenant(tenant);
		Date now = new Date();
		recordLastAccessTimeOfTenant(tenant, now);
		LOGGER.debug("Create data source for tenant '{}' at {}", tenant, now);
		return result;
	}
}
