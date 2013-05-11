package com.dayatang.dsrouter;

import java.util.Map;

import javax.sql.DataSource;

/**
 * 租户数据源注册表
 * @author yyang
 *
 */
public interface DataSourceRegistry {

	/**
	 * 获得与租户对应数据源
	 * @param tenant 租户标识
	 * @return 对应租户tenant的数据源
	 */
	DataSource getDataSourceOfTenant(String tenant);

	/**
	 * 注册租户数据源
	 * @param dataSources 租户数据源映射。以租户标识为key，对应数据源为value
	 */
	void registerDataSources(Map<String, DataSource> dataSources);
	
	/**
	 * 注册租户数据源
	 * @param tenant 数据源所属的租户
	 * @param dataSource 要注册的数据源
	 */
	void registerDataSource(String tenant, DataSource dataSource);
}
