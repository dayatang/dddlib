package org.openkoala.opencis.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.dom4j.Document;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.PropertyIllegalException;
import org.openkoala.opencis.utils.JenkinsConfUtil;

@Ignore
public class ProjectPermissionTest extends PermissionTest {

    private static final String PARENT_XPATH = "//maven2-moduleset/properties/hudson.security.AuthorizationMatrixProperty";
    private static final String XPATH = "//maven2-moduleset/properties/hudson.security.AuthorizationMatrixProperty/permission";

    @Test
    public void testSave() {
        ProjectPermission permission = new ProjectPermission(PERMISSION_TEXT, "tenpay");
        Document document = permission.save();
        assertTrue(xmlConfigContainNewPermission(document, XPATH));
        removePermission(document, PARENT_XPATH, JenkinsConfUtil.getJobConfigXMLPath(permission.getArtifactId()));
        assertFalse(xmlConfigContainNewPermission(document, XPATH));
    }

    @Test(expected = PropertyIllegalException.class)
    public void testSaveIfArtifactIdNull() {
        ProjectPermission permission = new ProjectPermission(PERMISSION_TEXT, null);
        permission.save();
    }


}
