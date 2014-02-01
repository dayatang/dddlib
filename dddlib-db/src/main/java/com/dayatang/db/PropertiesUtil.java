/**
 * 
 */
package com.dayatang.db;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;

/**
 * 
 * @author chencao <a href="mailto:chencao0524@gmail.com">陈操</a>
 * 
 * @version $LastChangedRevision$ $LastChangedBy$ $LastChangedDate$
 * 
 */
public class PropertiesUtil {

	private static final String JDBC_CONFIG = "jdbc.properties";
	private static String JDBC_DRIVER_KEY = "jdbc.driverClassName";
	private static String JDBC_URL_KEY = "jdbc.url";
	private static String JDBC_USERNAME_KEY = "jdbc.username";
	private static String JDBC_PASSWD_KEY = "jdbc.password";
	private static String JDBC_HOST_NAME_KEY = "jdbc.hostName";
	private static String JDBC_DATABSE_NAME_KEY = "jdbc.databaseName";
	private static String INIT_SQL_FILE_KEY = "init.sql.file";

	private static Configuration configuration = new ConfigurationFactory().fromClasspath(JDBC_CONFIG);

	public static final String JDBC_DRIVER = configuration.getString(JDBC_DRIVER_KEY);
	public static final String JDBC_URL = configuration.getString(JDBC_URL_KEY);
	public static final String JDBC_USERNAME = configuration.getString(JDBC_USERNAME_KEY);
	public static final String JDBC_PASSWD = configuration.getString(JDBC_PASSWD_KEY);
	
	public static final String JDBC_HOST_NAME = configuration.getString(JDBC_HOST_NAME_KEY);
	public static final String JDBC_DATABSE_NAME = configuration.getString(JDBC_DATABSE_NAME_KEY);
	public static final String INIT_SQL_FILE = configuration.getString(INIT_SQL_FILE_KEY);


	private PropertiesUtil() {
		super();
	}
}
