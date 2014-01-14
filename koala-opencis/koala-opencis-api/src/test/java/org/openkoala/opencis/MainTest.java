package org.openkoala.opencis;

import org.junit.Test;
import org.openkoala.opencis.api.AuthenticationStrategy;
import org.openkoala.opencis.api.CISClient;
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

        CISClient cisClient = new JenkinsCISClient(authenticationStrategy);

        if (!cisClient.authenticate()) {
            error = cisClient.getErrors();
            return;
        }

        if (!cisClient.createProject(project)) {
            error = cisClient.getErrors();
            return;
        }

        cisClient.close();


    }
}
