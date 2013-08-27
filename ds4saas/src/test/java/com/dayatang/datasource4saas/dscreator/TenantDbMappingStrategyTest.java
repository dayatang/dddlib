package com.dayatang.datasource4saas.dscreator;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.impl.SimpleConfiguration;

public class TenantDbMappingStrategyTest {
	
	private DbInfo dbInfo;
	private String tenant = "abc";
	private Configuration dbTenantMappings;

	@Before
	public void setUp() throws Exception {
		dbInfo = createDbInfo();
		dbTenantMappings = createDbMappings();
	}

	private DbInfo createDbInfo() {
		DbInfo dbInfo = new DbInfo();
		dbInfo.setHost("localhost");
		dbInfo.setPort("3306");
		dbInfo.setDbname("test_db");
		dbInfo.setInstance("XE");
		dbInfo.setUsername("root");
		return dbInfo;
	}

	private Configuration createDbMappings() {
		Configuration result = new SimpleConfiguration();
		result.setString("abc", "ABC123");
		result.setString("xyz", "XYZ123");
		return result;
	}

	@Test
	public void testOf() {
		assertThat(TenantDbMappingStrategy.of("dbname"), is(TenantDbMappingStrategy.DBNAME));
		assertThat(TenantDbMappingStrategy.of("Host"), is(TenantDbMappingStrategy.HOST));
		assertThat(TenantDbMappingStrategy.of("PORT"), is(TenantDbMappingStrategy.PORT));
		assertThat(TenantDbMappingStrategy.of("schEma"), is(TenantDbMappingStrategy.SCHEMA));
		assertThat(TenantDbMappingStrategy.of("InstancE"), is(TenantDbMappingStrategy.INSTANCE));
	}

	@Test
	public void testProcessByDbname() {
		TenantDbMappingStrategy.DBNAME.process(dbInfo, tenant, dbTenantMappings);
		assertThat(dbInfo.getDbname(), is("ABC123"));
	}

	@Test
	public void testProcessByHost() {
		TenantDbMappingStrategy.HOST.process(dbInfo, tenant, dbTenantMappings);
		assertThat(dbInfo.getHost(), is("ABC123"));
	}

	@Test
	public void testProcessByInstance() {
		TenantDbMappingStrategy.INSTANCE.process(dbInfo, tenant, dbTenantMappings);
		assertThat(dbInfo.getInstance(), is("ABC123"));
	}

	@Test
	public void testProcessByPort() {
		TenantDbMappingStrategy.PORT.process(dbInfo, tenant, dbTenantMappings);
		assertThat(dbInfo.getPort(), is("ABC123"));
	}

	@Test
	public void testProcessBySchema() {
		TenantDbMappingStrategy.SCHEMA.process(dbInfo, tenant, dbTenantMappings);
		assertThat(dbInfo.getUsername(), is("ABC123"));
	}

}
