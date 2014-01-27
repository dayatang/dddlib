package org.openkoala.koala.action.xml;


import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *  
 * 
 * @description DocumentUtil的测试类
 *  
 * @date：      2013-8-27   
 * 
 * @version    Copyright (c) 2013 Koala All Rights Reserved
 * 
 * @author     lingen.liu  <a href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a>
 */
public class DocumentUtilTest {
	
	@Before
	public void setUp(){
		String tmpDir = System.getProperty("java.io.tmpdir");
		try {
			FileUtils.copyFile(new File(DocumentUtilTest.class.getClassLoader().getResource("pom.xml").getPath()),new File(tmpDir+File.separator+"pom.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 测试从一个Document存储为XML
	 */
	@Test
	public void testDocument2Xml() {
		String tmpDir = System.getProperty("java.io.tmpdir");
		
		Document document = DocumentUtil.readDocument(tmpDir+File.separator+"pom.xml");
		DocumentUtil.document2Xml(tmpDir+File.separator+"pom2.xml", document);
		Assert.assertNotNull(DocumentUtil.readDocument(tmpDir+File.separator+"pom2.xml"));
	}

	@Test
	public void testReadDocumentString() throws IOException {
		String pomString = FileUtils.readFileToString(new File(DocumentUtilTest.class.getClassLoader().getResource("pom.txt").getPath()));
		Document document = DocumentUtil.readDocumentFromString(pomString);
		Assert.assertNotNull(document!=null);
	}

	@Test
	public void testReadDocumentInputStream() {
		Document document = DocumentUtil.readDocument(DocumentUtilTest.class.getClassLoader().getResource("pom.xml").getPath());
		Assert.assertNotNull(document!=null);
	}

	@Test
	public void testReadDocumentFromString() {
		String xmlPath = DocumentUtilTest.class.getClassLoader().getResource("pom.xml").getPath();
		Document document = DocumentUtil.readDocument(xmlPath);
		Assert.assertNotNull(document!=null);
	}

}
