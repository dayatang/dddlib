/**
 * 
 */
package com.dayatang.db;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author chencao <a href="mailto:chencao0524@gmail.com">陈操</a>
 * 
 * @version $LastChangedRevision$ $LastChangedBy$ $LastChangedDate$
 * 
 */
public class PropertiesUtil {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PropertiesUtil.class);

	private static Configuration config = null;

	public static String JDBC_CONFIG = "jdbc.properties";

	public static String JDBC_URL;
	public static String JDBC_USERNAME;
	public static String JDBC_PASSWD;
	public static String JDBC_DRIVER;
	
	public static String JDBC_HOST_NAME;
	public static String JDBC_DATABSE_NAME;
	public static String INIT_SQL_FILE;

	static {
		try {
			config = new PropertiesConfiguration(JDBC_CONFIG);

			JDBC_URL = getProperty("jdbc.url");
			JDBC_USERNAME = getProperty("jdbc.username");
			JDBC_PASSWD = getProperty("jdbc.password");
			
			JDBC_DRIVER = getProperty("jdbc.driverClassName");
			JDBC_HOST_NAME = getProperty("jdbc.hostName");
			JDBC_DATABSE_NAME = getProperty("jdbc.databaseName");
			INIT_SQL_FILE = getProperty("init.sql.file");

				info("JDBC_DRIVER = {}", PropertiesUtil.JDBC_DRIVER);
				info("JDBC_URL = {}", PropertiesUtil.JDBC_URL);
				info("JDBC_USERNAME = {}", PropertiesUtil.JDBC_USERNAME);
				info("JDBC_PASSWD = {}", PropertiesUtil.JDBC_PASSWD);
				info("JDBC_HOST_NAME = {}",PropertiesUtil.JDBC_HOST_NAME);
				info("JDBC_DATABSE_NAME = {}",PropertiesUtil.JDBC_DATABSE_NAME);
				info("INIT_SQL_FILE = {}", PropertiesUtil.INIT_SQL_FILE);

		} catch (ConfigurationException e) {
			error("initial properties error!! ",e);	
			System.exit(1);
		}
	}

	private static String getProperty(String key) {
		return config.getString(key);
	}

	public static String getProperty(String file, String key) throws Exception {
		Configuration config = new PropertiesConfiguration(file);
		return config.getString(key);
	}

	private static void info(String message, Object... params) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(message, params);
		}
	}

	private static void error(String message, Object... params) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(message, params);
		}
	}

}
