package com.dayatang.dsrouter.mappingstrategy;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.dscreator.TenantDbMappingStrategy;

public class MappingStrategyPortTest extends AbstractTenantDbMappingStrategyTest {

	@Before
	public void setUp() throws Exception {
		instance = TenantDbMappingStrategy.PORT;
	}


	@Override
	@Test
	public void testGetPort() {
		assertEquals("aaa", instance.getPort("a", "xyz", mappings));
	}

}
