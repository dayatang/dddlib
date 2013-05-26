package com.dayatang.dsrouter.mappingstrategy;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dayatang.dsrouter.dscreator.TenantDbMappingStrategy;
import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.impl.SimpleConfiguration;

public abstract class AbstractTenantDbMappingStrategyTest {
	
	protected TenantDbMappingStrategy instance;
	protected Configuration mappings;

	public AbstractTenantDbMappingStrategyTest() {
		mappings = new SimpleConfiguration();
		mappings.setString("a", "aaa");
		mappings.setString("b", "bbb");
	}

	@Test
	public void testGetDbName() {
		assertEquals("xyz", instance.getDbName("a", "xyz", mappings));
	}

	@Test
	public void testGetPort() {
		assertEquals("123", instance.getPort("a", "123", mappings));
	}

	@Test
	public void testGetHost() {
		assertEquals("xyz", instance.getHost("a", "xyz", mappings));
	}

	@Test
	public void testGetSchema() {
		assertEquals("xyz", instance.getSchema("a", "xyz", mappings));
	}

	@Test
	public void testGetInstanceName() {
		assertEquals("xyz", instance.getInstanceName("a", "xyz", mappings));
	}


}
