package org.openkoala.opencis.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.dom4j.Document;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.JenkinsSecurityNotOpenException;
import org.openkoala.opencis.PermissionElementNotExistException;
import org.openkoala.opencis.PropertyIllegalException;

@Ignore
public class GlobalPermissionTest extends PermissionTest {

	private static final String PARENT_XPATH = "//hudson/authorizationStrategy";
	private static final String XPATH = "//hudson/authorizationStrategy/permission";
	
	@Test
	public void testSave() {
		GlobalPermission permission = new GlobalPermission(PERMISSION_TEXT);
		Document document = permission.save();
		assertNotNull(document);
		assertTrue(xmlConfigContainNewPermission(document, XPATH));
		removePermission(document, PARENT_XPATH, GlobalPermission.JENKINS_CONFIG_XML_PATH);
		assertFalse(xmlConfigContainNewPermission(document, XPATH));
	}
	
	@Test(expected = PropertyIllegalException.class)
	public void testSaveIfPermissionTextNull() {
		Permission permission = new GlobalPermission(null);
		permission.save();
	}
	
	@Ignore
	@Test(expected = JenkinsSecurityNotOpenException.class)
	public void testSaveIfSecurityNotOpen() {
		GlobalPermission permission = new GlobalPermission(PERMISSION_TEXT);
		permission.save();
	}
	
	@Ignore
	@Test(expected = PermissionElementNotExistException.class)
	public void testSaveIfNotPermissionElement() {
		GlobalPermission permission = new GlobalPermission(PERMISSION_TEXT);
		permission.save();
	}
	
}
