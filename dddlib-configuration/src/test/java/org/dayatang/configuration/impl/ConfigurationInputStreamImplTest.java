package org.dayatang.configuration.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigurationInputStreamImplTest extends AbstractConfigurationTest {

	private String fileInClass = "/conf.properties";
	
	@Before
	public void setUp() throws Exception {
		InputStream in = getClass().getResourceAsStream(fileInClass);
		instance = new ConfigurationInputStreamImpl(in);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUsePrefix() {
		instance.usePrefix("org.dayatang");
		assertTrue(instance.getBoolean("finished"));
	}
}
