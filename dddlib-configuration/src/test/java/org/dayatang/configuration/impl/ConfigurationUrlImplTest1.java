package org.dayatang.configuration.impl;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Hashtable;

import org.dayatang.configuration.impl.ConfigurationUrlImpl;
import org.dayatang.utils.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationUrlImplTest1 extends AbstractConfigurationTest {
	
	@Before
	public void setUp() throws Exception {
		instance = ConfigurationUrlImpl.fromUrl("http://www.dayatang.com/conf.properties");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUsePrefix() {
		((ConfigurationUrlImpl)instance).usePrefix("org.dayatang");
		assertTrue(instance.getBoolean("finished"));
	}

}
