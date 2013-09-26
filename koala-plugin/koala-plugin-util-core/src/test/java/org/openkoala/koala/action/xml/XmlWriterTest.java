package org.openkoala.koala.action.xml;

import static org.junit.Assert.*;

import org.junit.Test;

public class XmlWriterTest {

	private XmlWriter xmlWriter = XmlWriter.getInstance("", "src/test/resources/test.xml");
	
	@Test
	public void updateTest() {
		xmlWriter.update("/xmlns:person/xmlns:address", "china-gd-gz");
		xmlWriter.saveUpdate();
		
		XmlReader xmlReader = XmlReader.getReader("", "src/test/resources/test.xml");
		assertEquals("china-gd-gz", xmlReader.queryString("/xmlns:person/xmlns:address"));
		
		xmlWriter.update("/xmlns:person/xmlns:address", "china");
		xmlWriter.saveUpdate();
		
		xmlReader = XmlReader.getReader("", "src/test/resources/test.xml");
		assertEquals("china", xmlReader.queryString("/xmlns:person/xmlns:address"));
	}

}
