package com.dayatang.dsrouter.mappingstrategy;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.Constants;
import com.dayatang.configuration.ConfigurationFactory;
import com.dayatang.configuration.WritableConfiguration;

public class InstanceBasedMappingStrategyTest {
	
	private InstanceBasedMappingStrategy instance;
	private WritableConfiguration configuration;
	private String tenant = "abc";
	private Properties properties = new Properties();
	

	@Before
	public void setUp() throws Exception {
		configuration = new ConfigurationFactory().fromClasspath(Constants.DB_MAPPING_FILE);
		configuration.setString(tenant, "XE");
		instance = new InstanceBasedMappingStrategy(configuration);
		properties.put(Constants.JDBC_HOST, "host");
		properties.put(Constants.JDBC_PORT, "port");
		properties.put(Constants.JDBC_DB_NAME, "dbname");
		properties.put(Constants.JDBC_INSTANCE, "instance");
		properties.put(Constants.JDBC_SCHEMA, "schema");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetDbName() {
		assertEquals("dbname", instance.getDbName(tenant, properties));
	}

	@Test
	public void testGetPort() {
		assertEquals("port", instance.getPort(tenant, properties));
	}

	@Test
	public void testGetHost() {
		assertEquals("host", instance.getHost(tenant, properties));
	}

	@Test
	public void testGetSchema() {
		assertEquals("schema", instance.getSchema(tenant, properties));
	}

	@Test
	public void testGetInstanceName() {
		assertEquals("XE", instance.getInstanceName(tenant, properties));
	}

}
