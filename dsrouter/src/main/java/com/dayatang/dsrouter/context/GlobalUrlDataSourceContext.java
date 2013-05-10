package com.dayatang.dsrouter.context;

import java.util.Properties;

public abstract class GlobalUrlDataSourceContext implements DataSourceContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3147626591195093707L;

	protected String username;

	protected String password;

	protected Properties properties;

	protected String url;

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public abstract String getSchema();

}
