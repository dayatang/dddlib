package org.openkoala.opencis;

import static org.mockito.Mockito.*;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.openkoala.opencis.api.*;
import org.openkoala.opencis.jenkins.impl.MockCISClient;

/**
 * User: zjzhai
 * Date: 1/14/14
 * Time: 9:17 AM
 */
public class MainTest {

    @Test
    public void testName() throws Exception {
        String jenkinsUrl = "http://126.0.0.1/jenkinsUrl";
        Project project = new Project();
        project.setArtifactId("josn");
        project.setGroupId("group");

        Developer developer = new Developer();
        developer.setName("xx");
        developer.setPassword("password");
        developer.setEmail("email");
        developer.setFullName("fullname");

        CloseableHttpClient client = mock(CloseableHttpClient.class);

        AuthenticationResult authenticationResult = new AuthenticationResult();
        authenticationResult.setContext(client);

        AuthenticationStrategy authenticationStrategy = mock(AuthenticationStrategy.class);
        when(authenticationStrategy.authenticate()).thenReturn(authenticationResult);

        CISClient cisClient = new MockCISClient(jenkinsUrl, authenticationStrategy);

        try {
            cisClient.authenticate();
            verify(authenticationStrategy).authenticate();
            cisClient.createProject(project);

            for (Developer eachDeveloper : project.getDevelopers()) {
                cisClient.createUserIfNecessary(project, eachDeveloper);
                cisClient.assignUsersToRole(project, "role", eachDeveloper);
            }
        } catch (Exception e) {

        } finally {
            cisClient.close();
            verify(client).close();
        }

    }
}
