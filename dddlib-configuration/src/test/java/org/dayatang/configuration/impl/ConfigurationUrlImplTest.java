package org.dayatang.configuration.impl;

import org.dayatang.configuration.EmbeddedHttpServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class ConfigurationUrlImplTest extends AbstractConfigurationTest {

    private static EmbeddedHttpServer httpServer;

    @BeforeClass
    public static void classSetUp() throws Exception {
        String pathname = ConfigurationUrlImplTest.class.getResource("/conf.properties").getFile();
        final File file = new File(pathname);
        httpServer = new EmbeddedHttpServer(1528);
        httpServer.mapping("/", file);
        httpServer.start();
    }

    @AfterClass
    public static void classTearDown() {
        httpServer.shutdown();
    }

	@Before
	public void setUp() throws Exception {
        instance = ConfigurationUrlImpl.fromUrl("http://localhost:1528/");
    }

	@Test
	public void testUsePrefix() {
		((ConfigurationUrlImpl)instance).usePrefix("org.dayatang");
		assertTrue(instance.getBoolean("finished"));
	}

}
