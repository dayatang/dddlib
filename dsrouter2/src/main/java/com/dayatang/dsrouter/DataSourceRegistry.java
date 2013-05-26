package com.dayatang.dsrouter;

import java.util.Date;
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
	 * 给租户指定数据源
	 * @param tenant 数据源所属的租户
	 * @param dataSource 要注册的数据源
	 */
	void registerDataSource(String tenant, DataSource dataSource);

	/**
	 * 给租户指定数据源
	 * @param dataSources 租户数据源映射。以租户标识为key，对应数据源为value
	 */
	void registerDataSources(Map<String, DataSource> dataSources);
	
	/**
	 * 从注册表中去除指定租户对应的数据源
	 * @param tenant
	 */
	void unregisterDataSource(String tenant);
	
	/**
	 * Clear/release all cached DataSource.
	 */
	void releaseAllDataSources();
	
	/**
	 * 租户最后访问时间
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
	 * 判断指定租户对应的数据源是否已经存在
	 * @param tenant
	 * @return
	 */
	boolean exists(String tenant);
	
}
