package com.dayatang.dsrouter.dscreator;

import javax.sql.DataSource;

import com.dayatang.dsrouter.Constants;
import com.dayatang.utils.Slf4jLogger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0DataSourceCreator extends AbstractDataSourceCreator {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(C3P0DataSourceCreator.class);

	@Override
	public DataSource createDataSourceForTenant(String tenant) {
		ComboPooledDataSource result = null;
		try {
			result = new ComboPooledDataSource();
			fillProperties(result);
			result.setDriverClass(getDsConfiguration().getString(Constants.JDBC_DRIVER_CLASS_NAME));
			result.setJdbcUrl(getUrl(tenant));
			result.setUser(getUsername(tenant));
			result.setPassword(getDsConfiguration().getString(Constants.JDBC_PASSWORD));
			return result;
		} catch (Exception e) {
			String message = "Create C3P0 data source failure.";
			LOGGER.error(message, e);
			throw new DataSourceCreationException(message, e);
		}
	}

}
