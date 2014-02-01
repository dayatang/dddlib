package com.dayatang.datasource4saas.dsregistry;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.dayatang.datasource4saas.DataSourceRegistry;
import com.dayatang.utils.Assert;
import com.dayatang.utils.Slf4jLogger;

public abstract class MapBasedDataSourceRegistry implements DataSourceRegistry {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(MapBasedDataSourceRegistry.class);
	
	private static Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
	private static Map<String, Date> lastAccess = new HashMap<String, Date>();
	public MapBasedDataSourceRegistry() {
	}

	@Override
	public DataSource getDataSourceOfTenant(String tenant) {
		Assert.notBlank(tenant, "Tenant is null or blank!");
		recordLastAccessTimeOfTenant(tenant, new Date());
		DataSource result = dataSources.get(tenant);
		if (result != null) {
			return result;
		}
		synchronized (this) {
			if (!existsDataSourceOfTenant(tenant)) {
				result = findOrCreateDataSourceForTenant(tenant);
				registerDataSourceForTenant(tenant, result);
			}
		}
		return result;
	}

    /**
     * 为租户查找或创建对应的数据源（由子类决定是查找还是创建）。
     * @param tenant
     * @return
     */
	protected abstract DataSource findOrCreateDataSourceForTenant(String tenant);

    /**
     * 记录租户对数据源的最后访问时间
     * @param tenant 租户
     * @param accessTime 访问时间
     */
	protected void recordLastAccessTimeOfTenant(String tenant, Date accessTime) {
		lastAccess.put(tenant, accessTime);
	}

    @Override
	public synchronized void registerDataSourceForTenant(String tenant, DataSource dataSource) {
		dataSources.put(tenant, dataSource);
	}

	@Override
	public synchronized void registerDataSources(Map<String, DataSource> dataSources) {
		MapBasedDataSourceRegistry.dataSources.putAll(dataSources);
		
	}

	@Override
	public synchronized void unregisterDataSourceOfTenant(String tenant) {
		DataSource dataSource = dataSources.remove(tenant);
		if (dataSource != null) {
			dataSource = null;
			LOGGER.debug("Data source of tenant '" + tenant + "' released!");
		}
		lastAccess.remove(tenant);
	}

	@Override
	public synchronized void releaseAllDataSources() {
		dataSources.clear();
		lastAccess.clear();
		LOGGER.debug("All tenant datasource have been released!");
	}

	@Override
	public int size() {
		return dataSources.size();
	}

	@Override
	public boolean existsDataSourceOfTenant(String tenant) {
		return dataSources.containsKey(tenant);
	}

	/**
	 * 获取每个租户的最后访问时间。
	 * @param tenant
	 * @return
	 */
	@Override
	public Date getLastAccessTimeOfTenant(String tenant) {
		return lastAccess.get(tenant);
	}


}
