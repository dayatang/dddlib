package com.dayatang.dsrouter.dscreator;

import com.dayatang.configuration.Configuration;
import com.dayatang.dsrouter.Constants;
import com.dayatang.utils.Assert;


public class JdbcConfiguration {

	private String driverClassName;
	private String host;
	private String port;
	private String dbname;
	private String instance;
	private String schema;
	private String username;
	private String password;
	private String extraUrlString;


	public JdbcConfiguration() {
	}

	public JdbcConfiguration(Configuration configuration) {
		Assert.notNull(configuration);
		driverClassName = configuration.getString(Constants.JDBC_DRIVER_CLASS_NAME);
		host = configuration.getString(Constants.JDBC_HOST);
		port = configuration.getString(Constants.JDBC_PORT);
		dbname = configuration.getString(Constants.JDBC_DB_NAME);
		instance = configuration.getString(Constants.JDBC_INSTANCE);
		schema = configuration.getString(Constants.JDBC_SCHEMA);
		username = configuration.getString(Constants.JDBC_USERNAME);
		password = configuration.getString(Constants.JDBC_PASSWORD);
		extraUrlString = configuration.getString(Constants.JDBC_EXTRA_URL_STRING);
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
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

	public String getExtraUrlString() {
		return extraUrlString;
	}

	public void setExtraUrlString(String extraUrlString) {
		this.extraUrlString = extraUrlString;
	}

}
