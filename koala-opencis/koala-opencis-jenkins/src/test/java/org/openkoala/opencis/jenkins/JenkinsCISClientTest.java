package org.openkoala.opencis.jenkins;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.authentication.CISAuthentication;
import org.openkoala.opencis.authorize.CISAuthorization;
import org.openkoala.opencis.jenkins.configureApi.ProjectCreateStrategy;

import java.net.MalformedURLException;

import java.net.URL;
import java.util.HashMap;

import static org.mockito.Mockito.*;

/**
 * /createItem?name=
 *
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:56:28 AM
 */
public class JenkinsCISClientTest {


    @Test
    public void test() throws MalformedURLException {
        URL JENKINS_URL = new URL("http", "localhost", 8080, "/jenkins");

        URL CAS_URL = new URL("http", "localhost", 8080, "/cas/v1/tickets/");

        JenkinsCISClient jenkinsCISClient;

        Project project = new Project();
        project.setArtifactId("Artifactdddfee3323");
        Developer developer = new Developer();
        developer.setName("www");
        developer.setEmail("admin@gmail.com");


        //认证
        jenkinsCISClient = new JenkinsCISClient(JENKINS_URL);
        CISAuthentication authentication = mock(CISAuthentication.class);
        if (!jenkinsCISClient.authenticateBy(authentication)) {
            return;
        }
        verify(authentication).authenticate();

        Object context = new HashMap<String, Object>();
        when(authentication.getContext()).thenReturn(context);

        //创建项目
        ProjectCreateStrategy projectCreateStrategy = mock(ProjectCreateStrategy.class);
        jenkinsCISClient.setProjectCreateStrategy(projectCreateStrategy);
        jenkinsCISClient.createProject(project);
        verify(projectCreateStrategy).create(project, context);

        //授权
        CISAuthorization cisAuthorization = mock(CISAuthorization.class);
        jenkinsCISClient.setAuthorization(cisAuthorization);
        jenkinsCISClient.createUserIfNecessary(project, developer);
        verify(cisAuthorization).authorize(project, developer, context);

    }


}
