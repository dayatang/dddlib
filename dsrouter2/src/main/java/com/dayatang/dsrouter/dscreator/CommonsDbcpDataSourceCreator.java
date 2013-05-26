package com.dayatang.dsrouter.dscreator;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.dayatang.dsrouter.Constants;
import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.utils.Slf4jLogger;

public class CommonsDbcpDataSourceCreator extends AbstractDataSourceCreator {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(CommonsDbcpDataSourceCreator.class);

	@Override
	public DataSource createDataSourceForTenant(String tenant) {
		try {
			BasicDataSource result = new BasicDataSource();
			fillProperties(result);
			result.setDriverClassName(getDsConfiguration().getString(Constants.JDBC_DRIVER_CLASS_NAME));
			result.setUrl(getUrl(tenant));
			result.setUsername(getUsername(tenant));
			result.setPassword(getDsConfiguration().getString(Constants.JDBC_PASSWORD));
			return result;
		} catch (Exception e) {
			String message = "Create Commons DBCP data source failure.";
			LOGGER.error(message, e);
			throw new DataSourceCreationException(message, e);
		}
	}
}
