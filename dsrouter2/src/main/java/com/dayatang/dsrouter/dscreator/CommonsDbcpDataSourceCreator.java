package com.dayatang.dsrouter.dscreator;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.dayatang.dsrouter.Constants;
import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.configuration.Configuration;
import com.dayatang.utils.Slf4jLogger;

public class CommonsDbcpDataSourceCreator extends AbstractDataSourceCreator {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(CommonsDbcpDataSourceCreator.class);

	public CommonsDbcpDataSourceCreator(JdbcUrlTranslator urlTranslator, Configuration configuration) {
		super(urlTranslator, configuration);
	}

	public CommonsDbcpDataSourceCreator(JdbcUrlTranslator urlTranslator) {
		super(urlTranslator);
	}

	@Override
	protected DataSource createDataSource() {
		try {
			return BasicDataSource.class.newInstance();
		} catch (Exception e) {
			String message = "Create Commons DBCP data source failure.";
			LOGGER.error(message, e);
			throw new DataSourceCreationException(message, e);
		}
	}

	@Override
	protected Map<String, String> getStandardPropMappings() {
		Map<String, String> results = new HashMap<String, String>();
		results.put(Constants.JDBC_DRIVER_CLASS_NAME, "driverClassName");
		results.put(Constants.JDBC_URL, "url");
		results.put(Constants.JDBC_USERNAME, "username");
		results.put(Constants.JDBC_PASSWORD, "password");
		return results;
	}

}
