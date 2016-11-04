package org.dayatang.datasource4saas.dscreator;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.beanutils.BeanUtils;
import org.dayatang.configuration.Configuration;
import org.dayatang.configuration.impl.SimpleConfiguration;
import org.dayatang.datasource4saas.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class DruidDataSourceCreatorTest {

	private DruidDataSourceCreator instance;
	private String tenant = "abc";
	
	@Before
	public void setUp() throws Exception {
		instance = new DruidDataSourceCreator();
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
		result.setInt("maxActive", 30);
		result.setInt("initialSize", 10);
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
		assertThat(result, instanceOf(DruidDataSource.class));
		assertEquals("com.mysql.jdbc.Driver", BeanUtils.getProperty(result, "driverClassName"));
		assertEquals(url, BeanUtils.getProperty(result, "url"));
		assertEquals("root", BeanUtils.getProperty(result, "username"));
		assertEquals("1234", BeanUtils.getProperty(result, "password"));
		assertEquals("30", BeanUtils.getProperty(result, "maxActive"));
		assertEquals("10", BeanUtils.getProperty(result, "initialSize"));
	}

	@Test
	public void createDataSourceForTenantByPostgresAndPort() throws Exception {
		instance.setDbType(DbType.POSTGRESQL);
		instance.setMappingStrategy(TenantDbMappingStrategy.PORT);
		String url = "jdbc:postgresql://localhost:DB_ABC/test_db?useUnicode=true&characterEncoding=utf-8";
		DataSource result = instance.createDataSourceForTenant(tenant);
		assertThat(result, instanceOf(DruidDataSource.class));
		assertEquals("org.postgresql.Driver", BeanUtils.getProperty(result, "driverClassName"));
		assertEquals(url, BeanUtils.getProperty(result, "url"));
		assertEquals("root", BeanUtils.getProperty(result, "username"));
		assertEquals("1234", BeanUtils.getProperty(result, "password"));
		assertEquals("30", BeanUtils.getProperty(result, "maxActive"));
		assertEquals("10", BeanUtils.getProperty(result, "initialSize"));
	}
}
