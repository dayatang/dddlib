package org.dayatang.configuration.impl;

import org.dayatang.configuration.EmbeddedHttpServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertTrue;

public class ConfigurationUrlImplTest extends AbstractConfigurationTest {

    private static EmbeddedHttpServer httpServer;
    private InputStream in;

    @BeforeClass
    public static void classSetUp() throws Exception {
        String pathname = ConfigurationUrlImplTest.class.getResource("/conf.properties").getFile();
        final File file = new File(pathname);
        httpServer = new EmbeddedHttpServer(1528);
        httpServer.mapping("/", file);
        httpServer.start();
    }

    @AfterClass
    public static void classTearDown() throws Exception {
        httpServer.shutdown();
    }

	@Before
	public void setUp() throws Exception {
        in = new URL("http://localhost:1528/").openStream();
        instance = new ConfigurationInputStreamImpl(in);
    }

    public void tearDown() throws Exception {
        in.close();
    }

	@Test
	public void testUsePrefix() {
		instance.usePrefix("org.dayatang");
		assertTrue(instance.getBoolean("finished"));
	}

}
