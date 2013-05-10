package com.dayatang.dsrouter.dsregistry;

import javax.sql.DataSource;

/**
 * 数据源创建器接口。用于为特定租户生成唯一的数据源。
 * @author yyang
 *
 */
public interface DataSourceCreator {

	DataSource createDataSourceForTenant(String tenant);

}
