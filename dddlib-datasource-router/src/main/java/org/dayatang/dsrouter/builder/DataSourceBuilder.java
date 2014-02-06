package org.dayatang.dsrouter.builder;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.Properties;

public interface DataSourceBuilder extends Serializable {

	public DataSource buildDataSource(Properties prop);

	public DataSource buildDataSource(String url, String username,
			String password, Properties prop);

}
