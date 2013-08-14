package com.dayatang.dsrouter.dbtype;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.impl.SimpleConfiguration;
import com.dayatang.dsrouter.Constants;
import com.dayatang.dsrouter.dscreator.DbType;
import com.dayatang.dsrouter.dscreator.JdbcConfiguration;

public abstract class AbstractDbTypeTest {
	
	protected DbType instance;
	protected JdbcConfiguration jdbcConfiguration;
	protected String tenant = "abc";
	protected Configuration dsConfiguration;
	protected Configuration dbTenantMappings;

	public AbstractDbTypeTest() {
		dsConfiguration = new SimpleConfiguration();
		dsConfiguration.setString(Constants.DB_TYPE, "mysql");
		dsConfiguration.setString(Constants.TENANT_MAPPING_STRATEGY, "dbname");
		dsConfiguration.setString(Constants.JDBC_HOST, "localhost");
		dsConfiguration.setString(Constants.JDBC_DB_NAME, "test_db");
		dsConfiguration.setString(Constants.JDBC_PORT, "3306");
		dsConfiguration.setString(Constants.JDBC_INSTANCE, "XE");
		dsConfiguration.setString(Constants.JDBC_USERNAME, "root");
		
		dbTenantMappings = new SimpleConfiguration();
		dbTenantMappings.setString("abc", "ABC");
		dbTenantMappings.setString("xyz", "XYZ");
		
		jdbcConfiguration = new JdbcConfiguration(tenant, dsConfiguration, dbTenantMappings);
		
	}

}
