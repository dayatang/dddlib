package org.openkoala.koala.action.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openkoala.koala.pojo.Dependency;

/**
 * 
 * 
 * @description PomXmlWriter的测试
 *  
 * @date：       2013-8-28
 * 
 * @version      Copyright (c) 2013 Koala All Rights Reserved
 *  
 * @author       lingen.liu  <a href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a>
 */
public class PomXmlWriterTest {

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
	
	private void copyFile(){
		String tmpDir = System.getProperty("java.io.tmpdir");
		try {
			FileUtils.copyFile(new File(DocumentUtilTest.class.getClassLoader().getResource("pom2.xml").getPath()),new File(tmpDir+File.separator+"pom2.xml"));
			xmlPath = tmpDir +File.separator+"pom2.xml";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initDoucment(){
		document = DocumentUtil.readDocument(xmlPath);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdatePomArtifactId() {
		PomXmlWriter.updatePomArtifactId(document, "koala-commons-codechecker-2");
		DocumentUtil.document2Xml(xmlPath, document);
		Document document2 = DocumentUtil.readDocument(xmlPath);
		String artifactId = PomXmlReader.queryText("/xmlns:project/xmlns:artifactId", document2);
		Assert.assertEquals(artifactId, "koala-commons-codechecker-2");
	}

	@Test
	public void testUpdatePomParentArtifactId() {
		PomXmlWriter.updatePomParentArtifactId(document, "koala-commons-2");
		DocumentUtil.document2Xml(xmlPath, document);
		Document document2 = DocumentUtil.readDocument(xmlPath);
		String artifactId = PomXmlReader.queryText("/xmlns:project/xmlns:parent/xmlns:artifactId", document2);
		Assert.assertEquals(artifactId, "koala-commons-2");
	}

	@Test
	public void testUpdateDependency() {
		Dependency dependency = new Dependency("com.puppycrawl.tools","checkstyle","5.6");
		
		Dependency newdependency = new Dependency("com.puppycrawl.tools","checkstyle-core","5.7");
		
		PomXmlWriter.updateDependency(document, dependency, newdependency);
		List<String> artifactIds = PomXmlReader.queryListText("/xmlns:project/xmlns:dependencies/xmlns:dependency/xmlns:artifactId", document);
		boolean artifactIdChanged = false;
		
		for(String artifactId:artifactIds){
			if(artifactId.equals("checkstyle-core")){
				artifactIdChanged = true;
				break;
			}
		}
		
		Assert.assertTrue(artifactIdChanged);
	}

	

	@Test
	public void testUpdateModules() {
		List<String> modules = new ArrayList<String>();
		modules.add("bbb");
		PomXmlWriter.updateModules(document, modules);
		List<String> newModules = PomXmlReader.queryModules(document);
		Assert.assertTrue(modules.equals(newModules));
	}

}
