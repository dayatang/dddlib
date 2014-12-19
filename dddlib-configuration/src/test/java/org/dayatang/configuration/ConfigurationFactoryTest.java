package org.dayatang.configuration;

import org.dayatang.configuration.impl.ConfigurationDbImpl;
import org.dayatang.configuration.impl.ConfigurationFileImpl;
import org.dayatang.configuration.impl.ConfigurationInputStreamImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ConfigurationFactoryTest {

	private ConfigurationFactory instance;
	
	@Before
	public void setUp() throws Exception {
		instance = ConfigurationFactory.singleton();
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
		assertTrue(instance.fromClasspath("/conf.properties") instanceof ConfigurationInputStreamImpl);
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
		assertTrue(file.canRead());
		assertTrue(instance.fromFileSystem(file) instanceof ConfigurationFileImpl);
	}

	@Test
    public void testFromUrlString() throws IOException {
        String pathname = getClass().getResource("/conf.properties").getFile();
        final File file = new File(pathname);
        EmbeddedHttpServer httpServer = new EmbeddedHttpServer(1528);
        httpServer.mapping("/", file);
        httpServer.start();
        String url = "http://localhost:1528/";
        assertTrue(instance.fromUrl(url) instanceof ConfigurationInputStreamImpl);
        httpServer.shutdown();
    }

	@Test
    public void testFromUrlURL() throws Exception {
        String pathname = getClass().getResource("/conf.properties").getFile();
        final File file = new File(pathname);
        EmbeddedHttpServer httpServer = new EmbeddedHttpServer(1528);
        httpServer.mapping("/", file);
        httpServer.start();
        URL url = new URL("http://localhost:1528/");
        assertTrue(instance.fromUrl(url) instanceof ConfigurationInputStreamImpl);
        httpServer.shutdown();
    }

}
