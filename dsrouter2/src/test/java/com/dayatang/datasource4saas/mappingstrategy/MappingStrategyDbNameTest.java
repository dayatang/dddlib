package com.dayatang.datasource4saas.mappingstrategy;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.dayatang.datasource4saas.dscreator.TenantDbMappingStrategy;

public class MappingStrategyDbNameTest extends AbstractTenantDbMappingStrategyTest {

	@Before
	public void setUp() throws Exception {
		instance = TenantDbMappingStrategy.DBNAME;
	}


	@Test
	@Override
	public void testGetDbName() {
		assertEquals("aaa", instance.getDbName("a", "xyz", mappings));
	}

}
