package com.dayatang.datasource4saas.dscreator;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.impl.SimpleConfiguration;
import com.dayatang.datasource4saas.Constants;
import com.dayatang.datasource4saas.dscreator.C3P0DataSourceCreator;
import com.mchange.v2.c3p0.ComboPooledDataSource;


public class C3P0DataSourceCreatorTest {

	private C3P0DataSourceCreator instance;
	private String tenant = "abc";
	
	@Before
	public void setUp() throws Exception {
		instance = new C3P0DataSourceCreator();
		Configuration dsConfiguration = createDsConfiguration();
		Configuration tenantDbMappings = createDbMappings();
		instance.setDsConfiguration(dsConfiguration);
		instance.setTenantDbMapping(tenantDbMappings);
		
	}

	private Configuration createDsConfiguration() {
		Configuration result = new SimpleConfiguration();
		result.setString(Constants.JDBC_HOST, "localhost");
		result.setString(Constants.JDBC_PORT, "3306");
		result.setString(Constants.JDBC_DB_NAME, "test_db");
		result.setString(Constants.JDBC_INSTANCE, "XE");
		result.setString(Constants.JDBC_EXTRA_URL_STRING, "useUnicode=true&characterEncoding=utf-8");
		result.setString(Constants.JDBC_USERNAME, "root");
		result.setString(Constants.JDBC_PASSWORD, "1234");
		result.setInt("minPoolSize", 5);
		result.setInt("maxPoolSize", 30);
		result.setInt("initialPoolSize", 10);
		return result;
	}

	private Configuration createDbMappings() {
		Configuration result = new SimpleConfiguration();
		result.setString("abc", "DB_ABC");
		result.setString("xyz", "DB_XYZ");
		return result;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createDataSourceForTenantByMysqlAndDbname() throws Exception {
		instance.setDbType(DbType.MYSQL);
		instance.setMappingStrategy(TenantDbMappingStrategy.DBNAME);
		String url = "jdbc:mysql://localhost:3306/DB_ABC?useUnicode=true&characterEncoding=utf-8";
		DataSource result = instance.createDataSourceForTenant(tenant);
		assertThat(result, instanceOf(ComboPooledDataSource.class));
		assertEquals("com.mysql.jdbc.Driver", BeanUtils.getProperty(result, "driverClass"));
		assertEquals(url, BeanUtils.getProperty(result, "jdbcUrl"));
		assertEquals("root", BeanUtils.getProperty(result, "user"));
		assertEquals("1234", BeanUtils.getProperty(result, "password"));
		assertEquals("5", BeanUtils.getProperty(result, "minPoolSize"));
		assertEquals("30", BeanUtils.getProperty(result, "maxPoolSize"));
		assertEquals("10", BeanUtils.getProperty(result, "initialPoolSize"));
	}

	@Test
	public void createDataSourceForTenantByPostgresAndPort() throws Exception {
		instance.setDbType(DbType.POSTGRESQL);
		instance.setMappingStrategy(TenantDbMappingStrategy.PORT);
		String url = "jdbc:postgresql://localhost:DB_ABC/test_db?useUnicode=true&characterEncoding=utf-8";
		DataSource result = instance.createDataSourceForTenant(tenant);
		assertThat(result, instanceOf(ComboPooledDataSource.class));
		assertEquals("org.postgresql.Driver", BeanUtils.getProperty(result, "driverClass"));
		assertEquals(url, BeanUtils.getProperty(result, "jdbcUrl"));
		assertEquals("root", BeanUtils.getProperty(result, "user"));
		assertEquals("1234", BeanUtils.getProperty(result, "password"));
		assertEquals("5", BeanUtils.getProperty(result, "minPoolSize"));
		assertEquals("30", BeanUtils.getProperty(result, "maxPoolSize"));
		assertEquals("10", BeanUtils.getProperty(result, "initialPoolSize"));
	}
}
