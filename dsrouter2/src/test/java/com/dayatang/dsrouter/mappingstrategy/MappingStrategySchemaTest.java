package com.dayatang.dsrouter.mappingstrategy;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.dscreator.TenantDbMappingStrategy;

public class MappingStrategySchemaTest extends AbstractTenantDbMappingStrategyTest {

	@Before
	public void setUp() throws Exception {
		instance = TenantDbMappingStrategy.SCHEMA;
	}


	@Override
	@Test
	public void testGetSchema() {
		assertEquals("aaa", instance.getSchema("a", "xyz", mappings));
	}

}
