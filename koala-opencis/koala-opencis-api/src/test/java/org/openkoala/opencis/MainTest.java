package org.openkoala.opencis;

import org.junit.Test;
import org.openkoala.opencis.api.AuthenticationStrategy;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jenkins.impl.CASAuthentication;
import org.openkoala.opencis.jenkins.impl.JenkinsCISClient;

/**
 * User: zjzhai
 * Date: 1/14/14
 * Time: 9:17 AM
 */
public class MainTest {

    @Test
    public void testName() throws Exception {
        String error;

        AuthenticationStrategy authenticationStrategy = new CASAuthentication();

        String jenkinsUrl = "http://126.0.0.1";
        CISClient cisClient = new JenkinsCISClient(jenkinsUrl);
        Project project = new Project();
        project.setArtifactId("josn");
        project.setGroupId("group");

        if (!cisClient.createProject(project)) {
            error = cisClient.getErrors();
            return;
        }

        cisClient.close();


    }
}
