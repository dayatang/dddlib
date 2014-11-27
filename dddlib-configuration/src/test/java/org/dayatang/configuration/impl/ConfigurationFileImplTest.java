package org.dayatang.configuration.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigurationFileImplTest extends AbstractConfigurationTest {
	
	@Before
	public void setUp() throws Exception {
		instance = ConfigurationFileImpl.fromClasspath("/conf.properties");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFromClasspath() {
		instance = ConfigurationFileImpl.fromClasspath("/conf.properties");
		assertTrue(instance.getProperties().size() > 0);
	}

	@Test
	public void testFromPathname() {
		String pathname = getClass().getResource("/conf.properties").getFile();
		instance = ConfigurationFileImpl.fromFileSystem(pathname);
		assertTrue(instance.getProperties().size() > 0);
	}

	@Test
	public void testFromDirAndFile() {
		String pathname = getClass().getResource("/conf.properties").getFile();
		File file = new File(pathname);
		String dir = file.getParent();
		String fileName = file.getName();
		instance = ConfigurationFileImpl.fromFileSystem(dir, fileName);
		assertTrue(instance.getProperties().size() > 0);
	}

	@Test
	public void testFromFile() {
		String pathname = getClass().getResource("/conf.properties").getFile();
		File file = new File(pathname);
		instance = ConfigurationFileImpl.fromFile(file);
		assertTrue(instance.getProperties().size() > 0);
	}

	@Test
	public void testUsePrefix() {
		((ConfigurationFileImpl)instance).usePrefix("org.dayatang");
		assertTrue(instance.getBoolean("finished"));
	}

	@Test
	public void testSave() {
		instance.setString("xyz", "yyyy-MM-dd");
		((ConfigurationFileImpl)instance).save();
		instance = ConfigurationFileImpl.fromClasspath("/conf.properties");
		assertEquals("yyyy-MM-dd", instance.getString("xyz"));
	}

}
