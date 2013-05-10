package com.dayatang.dsrouter.dsregistry;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.dayatang.dsrouter.DataSourceRegistry;
import com.dayatang.utils.Assert;
import com.dayatang.utils.Slf4jLogger;

public abstract class AbstractDataSourceRegistry implements DataSourceRegistry {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(AbstractDataSourceRegistry.class);
	
	private static Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
	private static Map<String, Date> lastAccess = new HashMap<String, Date>();

	public AbstractDataSourceRegistry() {
	}

	@Override
	public DataSource getDataSourceOfTenant(String tenant) {
		Assert.notNull(tenant, "Tenant is null!");
		recordLastAccessTimeOfTenant(tenant, new Date());
		DataSource result = dataSources.get(tenant);
		if (result != null) {
			return result;
		}
		synchronized (this) {
			if (!exists(tenant)) {
				result = findOrCreateDataSourceForTenant(tenant);
				registerDataSourceForTenant(tenant, result);
			}
		}
		return result;
	}

	protected abstract DataSource findOrCreateDataSourceForTenant(String tenant);

	protected void recordLastAccessTimeOfTenant(String tenant, Date accessTime) {
		lastAccess.put(tenant, accessTime);
	}

	public synchronized void registerDataSourceForTenant(String tenant, DataSource dataSource) {
		dataSources.put(tenant, dataSource);
	}

	@Override
	public synchronized void registerDataSources(Map<String, DataSource> dataSources) {
		AbstractDataSourceRegistry.dataSources.putAll(dataSources);
		
	}

	public synchronized void releaseDataSourceOfTenant(String tenant) {
		DataSource dataSource = dataSources.remove(tenant);
		if (dataSource != null) {
			dataSource = null;
			LOGGER.debug("Data source of tenant '" + tenant + "' released!");
		}
		lastAccess.remove(tenant);
	}

	//Clear/release all cached DataSource.
	public synchronized void releaseAllDataSources() {
		dataSources.clear();
		lastAccess.clear();
		LOGGER.debug("All tenant datasource have been released!");
	}

	public int size() {
		return dataSources.size();
	}

	public boolean exists(String tenant) {
		return dataSources.containsKey(tenant);
	}

	/**
	 * 获取每个租户的最后访问时间。
	 * @param tenant
	 * @return
	 */
	public Date getLastAccessTimeOfTenant(String tenant) {
		return lastAccess.get(tenant);
	}


}
