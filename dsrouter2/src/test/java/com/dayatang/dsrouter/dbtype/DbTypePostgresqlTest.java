package com.dayatang.dsrouter.dbtype;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.Constants;
import com.dayatang.dsrouter.dscreator.DbType;
import com.dayatang.dsrouter.dscreator.JdbcConfiguration;

public class DbTypePostgresqlTest extends AbstractDbTypeTest {
	
	@Before
	public void setUp() throws Exception {
		instance = DbType.POSTGRESQL;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getUrl() {
		assertEquals("jdbc:postgresql://localhost:3306/ABC", instance.getUrl(jdbcConfiguration));
	}

	@Test
	public void getUrlWithExtraUrlString() {
		dsConfiguration.setString(Constants.JDBC_EXTRA_URL_STRING, "useUnicode=true&encoding=UTF-8");
		jdbcConfiguration = new JdbcConfiguration(tenant, dsConfiguration, dbTenantMappings);
		assertEquals("jdbc:postgresql://localhost:3306/ABC?useUnicode=true&encoding=UTF-8", instance.getUrl(jdbcConfiguration));
	}
}
