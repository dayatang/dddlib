package org.openkoala.koala.action.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.koala.pojo.Dependency;

/**
 * 
 * @description 读取POM的测试
 *  
 * @date：      2013-8-27   
 * 
 * @version    Copyright (c) 2013 Koala All Rights Reserved
 * 
 * @author     lingen.liu  <a href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a>
 */
public class PomXmlReaderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	private Document document;

	@Before
	public void setUp() throws Exception {
		copyFile();
		initDoucment();
	}
	
	private void copyFile(){
		String tmpDir = System.getProperty("java.io.tmpdir");
		try {
			FileUtils.copyFile(new File(DocumentUtilTest.class.getClassLoader().getResource("pom.xml").getPath()),new File(tmpDir+File.separator+"pom.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initDoucment(){
		String xmlPath = DocumentUtilTest.class.getClassLoader().getResource("pom.xml").getPath();
		document = DocumentUtil.readDocument(xmlPath);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testQueryProperties() {
		Map<String,String> propertiesMap = PomXmlReader.queryProperties(document);
		Assert.assertEquals(propertiesMap.get("dayatang.commons.version"), "3.5");
	}

	@Test
	public void testQueryModules() {
		List<String> modules = PomXmlReader.queryModules(document);
		Assert.assertEquals(modules.size(), 6);
	}

	@Test
	public void testQueryDependency() {
		List<Dependency> dependencys = PomXmlReader.queryDependency(document);
		Assert.assertNotNull(dependencys);
	}

	@Test
	public void testQueryText() {
		String name = PomXmlReader.queryText("/xmlns:project/xmlns:name", document);
		Assert.assertEquals(name,"koala project");
	}

	@Test
	public void testQueryListText() {
		List<String> developers = PomXmlReader.queryListText("/xmlns:project/xmlns:developers/xmlns:developer/xmlns:name", document);
		Assert.assertEquals(developers.size(), 7);
	}


	@Test
	public void testIsDependencyExists() {
		boolean exists = PomXmlReader.isDependencyExists("com.puppycrawl.tools", "checkstyle", document);
		Assert.assertTrue(exists);
		
		exists = PomXmlReader.isDependencyExists("abc", "checkstyle", document);
		Assert.assertTrue(!exists);
	}

	@Ignore
	@Test
	public void testIsBizModel() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testIsImpl() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testIsEar() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testIsInterface() {
		fail("Not yet implemented");
	}

}
