package com.dayatang.dsrouter.dscreator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;

import com.dayatang.dsrouter.Constants;
import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.utils.Slf4jLogger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0DataSourceCreator extends AbstractDataSourceCreator {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(C3P0DataSourceCreator.class);

	@Override
	protected DataSource createDataSource() {
		ComboPooledDataSource result = null;
		try {
			result = new ComboPooledDataSource();
			fillProperties(result);
			return result;
		} catch (Exception e) {
			String message = "Create C3P0 data source failure.";
			LOGGER.error(message, e);
			throw new DataSourceCreationException(message, e);
		}
	}

	protected void fillProperties(DataSource dataSource) throws IllegalAccessException, InvocationTargetException {
		Properties dsProperties = getDsConfiguration().getProperties();
		for (Entry<Object, Object> entry : dsProperties.entrySet()) {
			BeanUtils.setProperty(dataSource, entry.getKey().toString(), entry.getValue());
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
