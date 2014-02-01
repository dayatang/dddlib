package com.dayatang.configuration.impl;

import static org.junit.Assert.*;
import java.util.Date;
import java.util.Hashtable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.configuration.impl.ConfigurationUrlImpl;
import com.dayatang.utils.DateUtils;

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
