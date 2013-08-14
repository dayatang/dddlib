package com.dayatang.datasource4saas.dsregistry;

import javax.sql.DataSource;

/**
 * 数据源创建器接口。用于为特定租户创建专有的数据源。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public interface DataSourceCreator {

	/**
	 * 为租户创建专有的数据源
	 * @param tenant 要创建数据源的租户
	 * @return 分配给租户tenant的数据源
	 */
	DataSource createDataSourceForTenant(String tenant);

}
