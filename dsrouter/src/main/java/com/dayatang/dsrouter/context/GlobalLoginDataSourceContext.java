package com.dayatang.dsrouter.context;

import java.util.Properties;

public abstract class GlobalLoginDataSourceContext implements DataSourceContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3147626591195093707L;

	protected String username;

	protected String password;

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
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
	public abstract Properties getProperties();

	@Override
	public abstract String getSchema();

	@Override
	public abstract String getUrl();

	public String toString() {
		return "url = " + getUrl() + " schema = " + getSchema();
	}
}
