package com.dayatang.dsrouter.builder.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 本版本，暂不支持Proxool实现
 * 
 * @author chencao
 * 
 */
public class ProxoolMySQLDataSourceBuilder extends
		AbstractMySQLDataSourceBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 701803034236511465L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProxoolMySQLDataSourceBuilder.class);

	private static final String PROP_FILE = "datasource-default-properties-c3p0.properties";

	private static Properties defaultProp = new Properties();

	private static InputStream defaultPropIns = C3P0MySQLDataSourceBuilder.class
			.getResourceAsStream("/" + PROP_FILE);

	static {
		try {
			defaultProp.load(defaultPropIns);
		} catch (IOException e) {
			error("initial properties error!!");
			System.err.println("initial properties error!!");
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	protected Properties getDefaultProp() {
		return defaultProp;
	}

	@Override
	protected Class<? extends DataSource> getDataSourceClass() {
		return ProxoolDataSource.class;
	}

	@Override
	protected String getDriverProperty() {
		return "driver";
	}

	@Override
	protected String getJdbcUrlProperty() {
		return "driverUrl";
	}

	@Override
	protected String getPasswordProperty() {
		return "password";
	}

	@Override
	protected String getUserProperty() {
		return "user";
	}

	private static void error(String message, Object... params) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(message, params);
		}
	}

}
