package com.dayatang.datasource4saas.dscreator;

import com.dayatang.configuration.Configuration;
import com.dayatang.datasource4saas.Constants;

/**
 * 从配置文件中读入的数据库信息。
 * 
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 * 
 */
public class DbInfo {
	private String host;
	private String port;
	private String dbname;
	private String instance;
	private String username;
	private String password;
	private String extraUrlString;

	
	public DbInfo() {
	}

	public DbInfo(Configuration dsConfiguration) {
		host = dsConfiguration.getString(Constants.JDBC_HOST);
		port = dsConfiguration.getString(Constants.JDBC_PORT);
		dbname = dsConfiguration.getString(Constants.JDBC_DB_NAME);
		instance = dsConfiguration.getString(Constants.JDBC_INSTANCE);
		username = dsConfiguration.getString(Constants.JDBC_USERNAME);
		password = dsConfiguration.getString(Constants.JDBC_PASSWORD);
		extraUrlString = dsConfiguration.getString(Constants.JDBC_EXTRA_URL_STRING);
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
