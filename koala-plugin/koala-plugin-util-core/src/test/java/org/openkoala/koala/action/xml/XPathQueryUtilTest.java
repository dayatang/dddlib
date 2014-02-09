package org.openkoala.koala.action.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * 
 * @description XPathQuery的实现类
 * 
 * @date： 2013-8-28
 * 
 * @version Copyright (c) 2013 Koala All Rights Reserved
 * 
 * @author lingen.liu <a
 *         href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a>
 */
public class XPathQueryUtilTest {
	
	public static final String POM_XMLS = "http://maven.apache.org/POM/4.0.0";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private Document document;

	private String xmlPath;

	@Before
	public void setUp() throws Exception {
		copyFile();
		initDoucment();
	}

	private void copyFile() {
		String tmpDir = System.getProperty("java.io.tmpdir");
		try {
			FileUtils.copyFile(new File(DocumentUtilTest.class.getClassLoader()
					.getResource("pom2.xml").getPath()), new File(tmpDir
					+ File.separator + "pom2.xml"));
			xmlPath = tmpDir + File.separator + "pom2.xml";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initDoucment() {
		document = DocumentUtil.readDocument(xmlPath);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testQueryStringFromStringDocument() {
		List<Element> elements = XPathQueryUtil.query(POM_XMLS,"/xmlns:project/xmlns:dependencies/xmlns:dependency", document);
		Assert.assertTrue(elements.size()==3);
	}

	@Test
	public void testQuerySingle() {
		Element nameElement = XPathQueryUtil.querySingle(POM_XMLS, "/xmlns:project/xmlns:name", document);
		Assert.assertTrue(nameElement.getText().equals("koala-commons-codechecker"));
	}
	

}
