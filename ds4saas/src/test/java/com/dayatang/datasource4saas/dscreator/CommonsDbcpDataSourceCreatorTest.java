package com.dayatang.datasource4saas.dscreator;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.impl.SimpleConfiguration;
import com.dayatang.datasource4saas.Constants;


public class CommonsDbcpDataSourceCreatorTest {

	private CommonsDbcpDataSourceCreator instance;
	private String tenant = "abc";
	
	@Before
	public void setUp() throws Exception {
		instance = new CommonsDbcpDataSourceCreator();
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
		result.setInt("initialSize", 100);
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
		instance.setDbType(DbType.ORACLE);
		instance.setMappingStrategy(TenantDbMappingStrategy.INSTANCE);
		String url = "jdbc:oracle:thin:@localhost:3306:DB_ABC?useUnicode=true&characterEncoding=utf-8";
		DataSource result = instance.createDataSourceForTenant(tenant);
		assertThat(result, instanceOf(BasicDataSource.class));
		assertEquals("oracle.jdbc.OracleDriver", BeanUtils.getProperty(result, "driverClassName"));
		assertEquals(url, BeanUtils.getProperty(result, "url"));
		assertEquals("root", BeanUtils.getProperty(result, "username"));
		assertEquals("1234", BeanUtils.getProperty(result, "password"));
		assertEquals("100", BeanUtils.getProperty(result, "initialSize"));
	}

	@Test
	public void createDataSourceForTenantByPostgresAndPort() throws Exception {
		instance.setDbType(DbType.DB2);
		instance.setMappingStrategy(TenantDbMappingStrategy.HOST);
		String url = "jdbc:db2://DB_ABC:3306/test_db?useUnicode=true&characterEncoding=utf-8";
		DataSource result = instance.createDataSourceForTenant(tenant);
		assertThat(result, instanceOf(BasicDataSource.class));
		assertEquals("com.ibm.db2.jcc.DB2Driver", BeanUtils.getProperty(result, "driverClassName"));
		assertEquals(url, BeanUtils.getProperty(result, "url"));
		assertEquals("root", BeanUtils.getProperty(result, "username"));
		assertEquals("1234", BeanUtils.getProperty(result, "password"));
		assertEquals("100", BeanUtils.getProperty(result, "initialSize"));
	}
}
