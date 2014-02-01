package com.dayatang.datasource4saas.dscreator;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 基于C3P0连接池的数据源创建器
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class C3P0DataSourceCreator extends AbstractDataSourceCreator {

	@Override
	protected DataSource createDataSource() {
		return new ComboPooledDataSource();
	}

	@Override
	protected String getDriverClassPropName() {
		return "driverClass";
	}

	@Override
	protected String getUrlPropName() {
		return "jdbcUrl";
	}

	@Override
	protected String getUsernamePropName() {
		return "user";
	}

	@Override
	protected String getPasswordPropName() {
		return "password";
	}

}
