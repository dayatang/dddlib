package com.dayatang.dsrouter.context;

import java.io.Serializable;
import java.util.Properties;

public interface DataSourceContext extends Serializable {

	public String getUrl();

	public String getSchema();

	public String getUsername();

	public String getPassword();

	public Properties getProperties();

}
