package org.openkoala.koala.action.properties;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class PropertiesUtilTest {

	private PropertiesUtil propertiesUtil;
	private String tempFile = "src/test/resources/test.properties";
	private String key = "test-key";
	private String value = "test-value";
	
	public PropertiesUtilTest() {
		File file = new File(tempFile);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		propertiesUtil = PropertiesUtil.getInstance(tempFile);
	}
	
	@Test
	public void propertyCrudTest() {
		//create property test
		propertiesUtil.addProperty(key, value);
		assertEquals(value, propertiesUtil.getProperty(key));
		
		//update property test
		propertiesUtil.setProperty(key, "test-value2");
		assertEquals("test-value2", propertiesUtil.getProperty(key));
		
		//remove property test
		propertiesUtil.removeProperty(key);
		assertNull(propertiesUtil.getProperty(key));
	}

	@Test
	public void restoreTest() {
		PropertiesUtil thePropertiesUtil = PropertiesUtil.getInstance(tempFile);
		
		thePropertiesUtil.addProperty(key, value);
		thePropertiesUtil.restore();
		PropertiesUtil newPropertiesUtilInstance = PropertiesUtil.getInstance(tempFile);
		assertEquals(value, newPropertiesUtilInstance.getProperty(key));
		
		thePropertiesUtil.removeProperty(key);
		thePropertiesUtil.restore();
		newPropertiesUtilInstance = PropertiesUtil.getInstance(tempFile);
		assertNull(newPropertiesUtilInstance.getProperty(key));
	}
	
}
