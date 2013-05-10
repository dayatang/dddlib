package com.dayatang.dsrouter.dscreator;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.dayatang.dsrouter.Constants;
import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.configuration.Configuration;
import com.dayatang.utils.Slf4jLogger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0DataSourceCreator extends AbstractDataSourceCreator {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(C3P0DataSourceCreator.class);

	public C3P0DataSourceCreator(JdbcUrlTranslator urlTranslator, Configuration configuration) {
		super(urlTranslator, configuration);
	}

	public C3P0DataSourceCreator(JdbcUrlTranslator urlTranslator) {
		super(urlTranslator);
	}

	@Override
	protected DataSource createDataSource() {
		try {
			return new ComboPooledDataSource();
		} catch (Exception e) {
			String message = "Create C3P0 data source failure.";
			LOGGER.error(message, e);
			throw new DataSourceCreationException(message, e);
		}
	}

	@Override
	protected Map<String, String> getStandardPropMappings() {
		Map<String, String> results = new HashMap<String, String>();
		results.put(Constants.JDBC_DRIVER_CLASS_NAME, "driverClass");
		results.put(Constants.JDBC_URL, "jdbcUrl");
		results.put(Constants.JDBC_USERNAME, "user");
		results.put(Constants.JDBC_PASSWORD, "password");
		return results;
	}

}
