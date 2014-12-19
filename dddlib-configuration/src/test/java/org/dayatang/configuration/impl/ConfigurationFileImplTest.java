package org.dayatang.configuration.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigurationFileImplTest extends AbstractConfigurationTest {

	private String fileInClass = "/conf.properties";
	
	@Before
	public void setUp() throws Exception {
		String fileName = getClass().getResource(fileInClass).getFile();
		instance = ConfigurationFileImpl.fromFile(new File(fileName));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFromPathname() {
		String pathname = getClass().getResource(fileInClass).getFile();
		instance = new ConfigurationFileImpl(pathname);
		assertTrue(instance.getProperties().size() > 0);
	}

	@Test
	public void testFromDirAndFile() {
		String pathname = getClass().getResource(fileInClass).getFile();
		File file = new File(pathname);
		String dir = file.getParent();
		String fileName = file.getName();
		instance = new ConfigurationFileImpl(dir, fileName);
		assertTrue(instance.getProperties().size() > 0);
	}

	@Test
	public void testFromFile() {
		String pathname = getClass().getResource(fileInClass).getFile();
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
		String fileName = getClass().getResource(fileInClass).getFile();
		instance = new ConfigurationFileImpl(new File(fileName));
		assertEquals("yyyy-MM-dd", instance.getString("xyz"));
	}

}
