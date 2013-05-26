package com.dayatang.dsrouter.urltranslator;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.Constants;
import com.dayatang.dsrouter.dscreator.TenantDbMappingStrategy;

public class MySqlUrlTranslatorTest {
	
	private MySqlUrlTranslator instance;
	private TenantDbMappingStrategy strategy;
	private Properties properties = new Properties();
	private String tenant = "abc";

	@Before
	public void setUp() throws Exception {
		strategy = mock(TenantDbMappingStrategy.class);
		instance = new MySqlUrlTranslator(strategy);
		when(strategy.getHost(tenant, properties)).thenReturn("localhost");
		when(strategy.getDbName(tenant, properties)).thenReturn("testdb_abc");
		when(strategy.getPort(tenant, properties)).thenReturn("3306");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void withoutExtraUrlString() {
		assertEquals("jdbc:mysql://localhost:3306/testdb_abc", instance.translateUrl(tenant, properties));
	}

	@Test
	public void withExtraUrlString() {
		properties.put(Constants.JDBC_EXTRA_URL_STRING, "useUnicode=true&encoding=UTF-8");
		assertEquals("jdbc:mysql://localhost:3306/testdb_abc?useUnicode=true&encoding=UTF-8", instance.translateUrl(tenant, properties));
	}

	@Test
	public void withExtraUrlStringWithLeadingChar() {
		properties.put(Constants.JDBC_EXTRA_URL_STRING, "?useUnicode=true&encoding=UTF-8");
		assertEquals("jdbc:mysql://localhost:3306/testdb_abc?useUnicode=true&encoding=UTF-8", instance.translateUrl(tenant, properties));
	}

}
