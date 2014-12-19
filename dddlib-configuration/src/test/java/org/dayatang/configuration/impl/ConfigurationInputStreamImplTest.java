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
	public void testFromClasspath() {
		instance = ConfigurationFileImpl.fromClasspath(fileInClass);
		assertTrue(instance.getProperties().size() > 0);
	}

	@Test
	public void testFromPathname() {
		String pathname = getClass().getResource(fileInClass).getFile();
		instance = ConfigurationFileImpl.fromFileSystem(pathname);
		assertTrue(instance.getProperties().size() > 0);
	}

	@Test
	public void testFromDirAndFile() {
		String pathname = getClass().getResource(fileInClass).getFile();
		File file = new File(pathname);
		String dir = file.getParent();
		String fileName = file.getName();
		instance = ConfigurationFileImpl.fromFileSystem(dir, fileName);
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
		instance.usePrefix("org.dayatang");
		assertTrue(instance.getBoolean("finished"));
	}
}
