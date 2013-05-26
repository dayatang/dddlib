package com.dayatang.dsrouter.dscreator;

import javax.sql.DataSource;

import org.logicalcobwebs.proxool.ProxoolDataSource;

import com.dayatang.dsrouter.Constants;
import com.dayatang.utils.Slf4jLogger;

public class ProxoolDataSourceCreator extends AbstractDataSourceCreator {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(ProxoolDataSourceCreator.class);

	@Override
	public DataSource createDataSourceForTenant(String tenant) {
		try {
			ProxoolDataSource result = new ProxoolDataSource();
			fillProperties(result);
			result.setDriver(getDsConfiguration().getString(Constants.JDBC_DRIVER_CLASS_NAME));
			result.setDriverUrl(getUrl(tenant));
			result.setUser(getUsername(tenant));
			result.setPassword(getDsConfiguration().getString(Constants.JDBC_PASSWORD));
			return result;
		} catch (Exception e) {
			String message = "Create Proxool data source failure.";
			LOGGER.error(message, e);
			throw new DataSourceCreationException(message, e);
		}
	}
}
