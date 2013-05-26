package com.dayatang.dsrouter.dbtype;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.impl.SimpleConfiguration;
import com.dayatang.dsrouter.dscreator.DbType;
import com.dayatang.dsrouter.dscreator.JdbcConfiguration;

public abstract class AbstractDbTypeTest {
	
	protected DbType instance;
	protected JdbcConfiguration jdbcConfiguration;

	public AbstractDbTypeTest() {
		Configuration configuration = new SimpleConfiguration();
		configuration.setString("jdbc.host", "localhost");
		configuration.setString("jdbc.port", "3306");
		configuration.setString("jdbc.dbname", "test_db");
		configuration.setString("jdbc.instance", "XE");
		//configuration.setString("jdbc.extraUrlString", "useUnicode=true&characterEncoding=utf-8");
		configuration.setString("jdbc.username", "root");
		jdbcConfiguration = new JdbcConfiguration(configuration);
	}

}
