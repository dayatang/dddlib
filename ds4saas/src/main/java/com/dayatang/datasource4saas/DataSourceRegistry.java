package com.dayatang.datasource4saas;

import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

/**
 * 租户数据源注册表，为每个租户注册专用的数据源
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
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
	 * 给租户指定数据源
	 * @param tenant 数据源所属的租户
	 * @param dataSource 要注册的数据源
	 */
	void registerDataSourceForTenant(String tenant, DataSource dataSource);

	/**
	 * 批量注册租户数据源
	 * @param dataSources 租户数据源映射。以租户标识为key，对应数据源为value
	 */
	void registerDataSources(Map<String, DataSource> dataSources);
	
	/**
	 * 从注册表中去除指定租户对应的数据源
	 * @param tenant
	 */
	void unregisterDataSourceOfTenant(String tenant);
	
	/**
	 * Clear/release all cached DataSource.
	 */
	void releaseAllDataSources();
	
	/**
	 * 租户最后访问数据源的时间
	 * @param tenant
	 * @return
	 */
	Date getLastAccessTimeOfTenant(String tenant);
	
	/**
	 * 获得注册表中注册的数据源的数量
	 * @return
	 */
	int size();
	
	/**
	 * 判断是否已经为指定租户注册数据源
	 * @param tenant
	 * @return
	 */
	boolean existsDataSourceOfTenant(String tenant);
	
}
