package org.openkoala.koala.parse;

import static org.junit.Assert.*;

import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PackageScanUtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testScan() {
		Map<String,String> maps = PackageScanUtil.scan();
		Assert.assertEquals(maps.get("PackageScanUtilTest"), "org.openkoala.koala.parse.PackageScanUtilTest");
	}

}
