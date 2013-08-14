package com.dayatang.datasource4saas.dscreator;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.impl.SimpleConfiguration;
import com.dayatang.datasource4saas.Constants;
import com.dayatang.datasource4saas.dscreator.DbType;
import com.dayatang.datasource4saas.dscreator.JdbcConfiguration;
import com.dayatang.datasource4saas.dscreator.TenantDbMappingStrategy;

public class JdbcConfigurationTest {
	
	protected JdbcConfiguration instance;
	protected String tenant = "abc";
	protected Configuration dsConfiguration;
	protected Configuration dbTenantMappings;

	@Before
	public void setUp() throws Exception {
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
		
		instance = new JdbcConfiguration(tenant, dsConfiguration, dbTenantMappings);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void mappingByHost() {
		instance.setMappingStrategy(TenantDbMappingStrategy.HOST);
		assertEquals("jdbc:mysql://ABC:3306/test_db", instance.getUrl());
		assertEquals("root", instance.getUsername());
	}

	@Test
	public void mappingByPort() {
		instance.setMappingStrategy(TenantDbMappingStrategy.PORT);
		assertEquals("jdbc:mysql://localhost:ABC/test_db", instance.getUrl());
		assertEquals("root", instance.getUsername());
	}

	@Test
	public void mappingByDbName() {
		instance.setMappingStrategy(TenantDbMappingStrategy.DBNAME);
		assertEquals("jdbc:mysql://localhost:3306/ABC", instance.getUrl());
		assertEquals("root", instance.getUsername());
	}

	@Test
	public void mappingByInstance() {
		instance.setDbType(DbType.ORACLE);
		instance.setMappingStrategy(TenantDbMappingStrategy.INSTANCE);
		assertEquals("jdbc:oracle:thin:@localhost:3306:ABC", instance.getUrl());
		assertEquals("root", instance.getUsername());
	}

	@Test
	public void mappingBySchema() {
		instance.setDbType(DbType.ORACLE);
		instance.setMappingStrategy(TenantDbMappingStrategy.SCHEMA);
		assertEquals("jdbc:oracle:thin:@localhost:3306:XE", instance.getUrl());
		assertEquals("ABC", instance.getUsername());
	}

}
