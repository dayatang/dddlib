package org.openkoala.koala.parse;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openkoala.koala.actionvo.XmlAdd;

public class XML2ObjectUtilTest {

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
	public void testProcessParse() throws Exception {
		XmlAdd xmlAdd = (XmlAdd)XML2ObjectUtil.getInstance().processParse("xmladd.xml");
	    assertEquals(xmlAdd.getExpress(), "$Project.dbProtocol=='JPA'");
	}

}
