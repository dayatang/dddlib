package com.dayatang.db;

import java.sql.Driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 此抽象类适用于所有数据库连接信息从classpath下的jdbc.properties文件中读取
 * 
 * @author chencao
 *
 */
public abstract class AbstractDBManager implements DBManager {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractDBManager.class);


	@SuppressWarnings("unchecked")
	public AbstractDBManager() {
		this.jdbcUrl = PropertiesUtil.JDBC_URL;
		this.username = PropertiesUtil.JDBC_USERNAME;
		this.password = PropertiesUtil.JDBC_PASSWD;
		
		this.hostName = PropertiesUtil.JDBC_HOST_NAME;
		this.databaseName = PropertiesUtil.JDBC_DATABSE_NAME;
		
		
		try {
			this.driverClass = (Class<Driver>) Class.forName(PropertiesUtil.JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			error("initial driver class error!!");
			System.exit(1);
		}
	}

	protected String jdbcUrl;
	protected String username;
	protected String password;
	protected Class<Driver> driverClass;
	
	protected String hostName;
	protected String databaseName;

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Class<Driver> getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(Class<Driver> driverClass) {
		this.driverClass = driverClass;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	private void error(String message, Object... params) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(message, params);
		}
	}

}
