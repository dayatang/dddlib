package com.dayatang.dsrouter.builder;

import java.io.Serializable;
import java.util.Properties;

import javax.sql.DataSource;

public interface DataSourceBuilder extends Serializable {

	public DataSource buildDataSource(Properties prop);

	public DataSource buildDataSource(String url, String username,
			String password, Properties prop);

}
