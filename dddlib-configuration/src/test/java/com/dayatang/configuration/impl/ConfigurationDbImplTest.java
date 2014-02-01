package com.dayatang.configuration.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConfigurationDbImplTest extends AbstractConfigurationTest {
	
	private static DataSource dataSource;

	@BeforeClass
	public static void classSetUp() throws Exception {
		dataSource = createDataSource();
	}

	private static DataSource createDataSource() throws IOException {
		Properties properties = new Properties();
		properties.load(ConfigurationDbImplTest.class.getResourceAsStream("/jdbc.properties"));
		ComboPooledDataSource result = new ComboPooledDataSource();
		try {
			result.setDriverClass(properties.getProperty("jdbc.driverClassName"));
		} catch (PropertyVetoException e) {
			throw new RuntimeException("Cannot access JDBC Driver: org.h2.Driver", e);
		}
		result.setJdbcUrl(properties.getProperty("jdbc.url"));
		result.setUser(properties.getProperty("jdbc.username"));
		result.setPassword(properties.getProperty("jdbc.password"));
		result.setMinPoolSize(10);
		result.setMaxPoolSize(300);
		result.setInitialPoolSize(10);
		result.setAcquireIncrement(5);
		result.setMaxStatements(0);
		result.setIdleConnectionTestPeriod(60);
		result.setAcquireRetryAttempts(30);
		result.setBreakAfterAcquireFailure(false);
		result.setTestConnectionOnCheckout(false);
		return result;
	}


	@Before
	public void setUp() throws Exception {
		readConfigFromFile();
		instance = new ConfigurationDbImpl(dataSource);
	}

	private static void readConfigFromFile() throws IOException {
		Properties props = new Properties();
		props.load(new InputStreamReader(ConfigurationDbImplTest.class.getResourceAsStream("/conf.properties"), "UTF-8") );
		ConfigurationDbImpl dbImpl = new ConfigurationDbImpl(dataSource);
		for (Object key : props.keySet()) {
			dbImpl.setString((String) key, props.getProperty((String) key));
		}
		//dbImpl.setString("name", "张三");
		dbImpl.save();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUsePrefix() {
		((ConfigurationDbImpl)instance).usePrefix("org.dayatang");
		assertTrue(instance.getBoolean("finished"));
	}

	@Test
	public void testSave() {
		instance.setString("xyz", "yyyy-MM-dd");
		((ConfigurationDbImpl)instance).save();
		ConfigurationDbImpl instance2 = new ConfigurationDbImpl(dataSource);
		assertEquals("yyyy-MM-dd", instance2.getString("xyz"));
	}

}
