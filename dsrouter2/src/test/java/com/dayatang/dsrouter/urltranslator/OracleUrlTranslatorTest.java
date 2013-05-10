package com.dayatang.dsrouter.urltranslator;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class OracleUrlTranslatorTest {
	
	private OracleUrlTranslator instance;
	private DbMappingStrategy strategy;
	private Properties properties = new Properties();
	private String tenant = "abc";

	@Before
	public void setUp() throws Exception {
		strategy = mock(DbMappingStrategy.class);
		instance = new OracleUrlTranslator(strategy);
		when(strategy.getHost(tenant, properties)).thenReturn("localhost");
		when(strategy.getPort(tenant, properties)).thenReturn("1521");
		when(strategy.getInstanceName(tenant, properties)).thenReturn("XE");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void withoutExtraUrlString() {
		assertEquals("jdbc:oracle:thin:@localhost:1521:XE", instance.translateUrl(tenant, properties));
	}

	/*
	@Test
	public void withExtraUrlString() {
		properties.put(Constants.JDBC_EXTRA_URL_STRING, "useUnicode=true&encoding=UTF-8");
		assertEquals("jdbc:oracle:thin:@localhost:1521:XE?useUnicode=true&encoding=UTF-8", instance.translateUrl(tenant, properties));
	}

	@Test
	public void withExtraUrlStringWithLeadingChar() {
		properties.put(Constants.JDBC_EXTRA_URL_STRING, "?useUnicode=true&encoding=UTF-8");
		assertEquals("jdbc:oracle:thin:@localhost:1521:XE?useUnicode=true&encoding=UTF-8", instance.translateUrl(tenant, properties));
	}
	*/

}
