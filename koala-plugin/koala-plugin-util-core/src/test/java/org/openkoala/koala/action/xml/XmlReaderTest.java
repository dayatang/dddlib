package org.openkoala.koala.action.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class XmlReaderTest {

	XmlReader xmlReader;
	
	public XmlReaderTest() {
		xmlReader = XmlReader.getReader("", "src/test/resources/test.xml");
	}
	
	@Test
	public void queryStringTest() {
		assertEquals("test-name", xmlReader.queryString("/xmlns:person/xmlns:name"));
	}
	
	@Test
	public void queryStringListTest() {
		List<String> telephones = new ArrayList<String>();
		telephones.add("123456");
		telephones.add("234567");
		assertEquals(telephones, xmlReader.queryListString("/xmlns:person/xmlns:telephones/xmlns:telephone"));
	}

}
