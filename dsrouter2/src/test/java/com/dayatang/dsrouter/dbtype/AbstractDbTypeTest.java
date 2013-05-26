package com.dayatang.dsrouter.dbtype;

import com.dayatang.dsrouter.dscreator.DbType;
import com.dayatang.dsrouter.dscreator.JdbcConfiguration;

public abstract class AbstractDbTypeTest {
	
	protected DbType instance;
	protected JdbcConfiguration jdbcConfiguration;

	public AbstractDbTypeTest() {
		jdbcConfiguration = new JdbcConfiguration();
		jdbcConfiguration.setHost("localhost");
		jdbcConfiguration.setDbname("test_db");
		jdbcConfiguration.setPort("3306");
		jdbcConfiguration.setInstance("XE");
		jdbcConfiguration.setUsername("root");
		
	}

}
