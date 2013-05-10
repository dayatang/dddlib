package com.dayatang.configuration;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.configuration.impl.ConfigurationDbImpl;
import com.dayatang.configuration.impl.ConfigurationFileImpl;
import com.dayatang.configuration.impl.ConfigurationUrlImpl;

public class ConfigurationFactoryTest {

	private ConfigurationFactory instance;
	
	@Before
	public void setUp() throws Exception {
		instance = new ConfigurationFactory();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFromDatabaseDataSource() {
		final DataSource dataSource = mock(DataSource.class);
		assertTrue(instance.fromDatabase(dataSource) instanceof ConfigurationDbImpl);
	}

	@Test
	public void testFromDatabaseDataSourceStringStringString() {
		final DataSource dataSource = mock(DataSource.class);
		assertTrue(instance.fromDatabase(dataSource, "table", "key", "value") instanceof ConfigurationDbImpl);
	}

	@Test
	public void testFromClasspathString() {
		assertTrue(instance.fromClasspath("/conf.properties") instanceof ConfigurationFileImpl);
	}

	@Test
	public void testFromFileSystemString() {
		String pathname = getClass().getResource("/conf.properties").getFile();
		assertTrue(instance.fromFileSystem(pathname) instanceof ConfigurationFileImpl);
	}

	@Test
	public void testFromFileSystemStringString() {
		String pathname = getClass().getResource("/conf.properties").getFile();
		File file = new File(pathname);
		String dir = file.getParent();
		String fileName = file.getName();
		assertTrue(instance.fromFileSystem(dir, fileName) instanceof ConfigurationFileImpl);
	}

	@Test
	public void testFromFileSystemFile() {
		String pathname = getClass().getResource("/conf.properties").getFile();
		File file = new File(pathname);
		assertTrue(instance.fromFileSystem(file) instanceof ConfigurationFileImpl);
	}

	@Test
	public void testFromUrlString() {
		String url = "http://www.dayatang.com/conf.properties";
		assertTrue(instance.fromUrl(url) instanceof ConfigurationUrlImpl);
	}

	@Test
	public void testFromUrlURL() throws MalformedURLException {
		URL url = new URL("http://www.dayatang.com/conf.properties");
		assertTrue(instance.fromUrl(url) instanceof ConfigurationUrlImpl);
	}

}
