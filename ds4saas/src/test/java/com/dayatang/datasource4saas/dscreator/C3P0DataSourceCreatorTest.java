package com.dayatang.datasource4saas.dscreator;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.datasource4saas.dscreator.C3P0DataSourceCreator;


public class C3P0DataSourceCreatorTest {

	private C3P0DataSourceCreator instance;
	
	@Before
	public void setUp() throws Exception {
		instance = new C3P0DataSourceCreator();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createDataSourceForTenant() throws Exception {
		String tenant = "abc";
		String url = "jdbc:mysql://localhost:3306/db_abc?useUnicode=true&characterEncoding=utf-8";
		DataSource result = instance.createDataSourceForTenant(tenant);
		assertEquals("com.mysql.jdbc.Driver", BeanUtils.getProperty(result, "driverClass"));
		assertEquals(url, BeanUtils.getProperty(result, "jdbcUrl"));
		assertEquals("5", BeanUtils.getProperty(result, "minPoolSize"));
	}
}
