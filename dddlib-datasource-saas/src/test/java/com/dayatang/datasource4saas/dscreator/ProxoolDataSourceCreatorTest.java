package com.dayatang.datasource4saas.dscreator;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.logicalcobwebs.proxool.ProxoolDataSource;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.impl.SimpleConfiguration;
import com.dayatang.datasource4saas.Constants;


public class ProxoolDataSourceCreatorTest {

	private ProxoolDataSourceCreator instance;
	private String tenant = "abc";
	
	@Before
	public void setUp() throws Exception {
		instance = new ProxoolDataSourceCreator();
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
		result.setInt("maximumConnectionCount", 15);
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
		instance.setDbType(DbType.SQLSERVER);
		instance.setMappingStrategy(TenantDbMappingStrategy.SCHEMA);
		String url = "jdbc:jtds:sqlserver://localhost:3306/test_db?useUnicode=true&characterEncoding=utf-8";
		DataSource result = instance.createDataSourceForTenant(tenant);
		assertThat(result, instanceOf(ProxoolDataSource.class));
		assertEquals("net.sourceforge.jtds.jdbc.Driver", BeanUtils.getProperty(result, "driver"));
		assertEquals(url, BeanUtils.getProperty(result, "driverUrl"));
		assertEquals("DB_ABC", BeanUtils.getProperty(result, "user"));
		assertEquals("1234", BeanUtils.getProperty(result, "password"));
		assertEquals("15", BeanUtils.getProperty(result, "maximumConnectionCount"));		
	}

}
