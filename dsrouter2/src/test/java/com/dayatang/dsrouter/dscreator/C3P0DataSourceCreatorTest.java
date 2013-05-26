package com.dayatang.dsrouter.dscreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.Constants;
import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;


public class C3P0DataSourceCreatorTest {

	private AbstractDataSourceCreator instance;
	private DbType urlTranslator; 
	private Configuration configuration = new ConfigurationFactory().fromClasspath(Constants.DB_CONF_FILE);
	private ComboPooledDataSource dataSource;
	
	@Before
	public void setUp() throws Exception {
		urlTranslator = mock(DbType.class);
		instance = new C3P0DataSourceCreator();
		dataSource = new ComboPooledDataSource();
		instance.setDataSource(dataSource);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createDataSourceForTenant() throws Exception {
		String tenant = "abcd";
		String url = "jdbc:mysql://localhost:3306/testdb_abc";
		when(urlTranslator.translateUrl(tenant, configuration.getProperties())).thenReturn(url);
		DataSource result = instance.createDataSourceForTenant(tenant);
		assertSame(dataSource, result);
		assertEquals("com.mysql.jdbc.Driver", BeanUtils.getProperty(result, "driverClass"));
		assertEquals(url, BeanUtils.getProperty(result, "jdbcUrl"));
		//assertEquals("root", BeanUtils.getProperty(result, "user"));
		//assertEquals("", BeanUtils.getProperty(result, "password"));
		assertEquals("5", BeanUtils.getProperty(result, "minPoolSize"));
	}
}
