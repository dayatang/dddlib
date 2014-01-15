package org.openkoala.opencis.jenkins;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.AuthenticationResult;
import org.openkoala.opencis.api.AuthenticationStrategy;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.authorize.CISAuthorization;
import org.openkoala.opencis.jenkins.configureApi.ProjectCreateStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

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
        String JENKINS_URL = "http://127.0.0.1:8080/jenkins";

        String CAS_URL = "http://127.0.0.1:8080/cas/v1/tickets/";

        JenkinsCISClient jenkinsCISClient;

        Project project = new Project();
        project.setArtifactId("Artifactdddfee3323");
        Developer developer = new Developer();
        developer.setName("www");
        developer.setEmail("admin@gmail.com");


        AuthenticationResult<WebDriver> authenticationResult = new AuthenticationResult<WebDriver>();
        authenticationResult.setContext(new HtmlUnitDriver(true));

        //认证
        AuthenticationStrategy<WebDriver> authentication = mock(AuthenticationStrategy.class);
        when(authentication.authenticate()).thenReturn(authenticationResult);

        jenkinsCISClient = new JenkinsCISClient(JENKINS_URL, authenticationResult.getContext());

        //创建项目
        ProjectCreateStrategy projectCreateStrategy = mock(ProjectCreateStrategy.class);
        jenkinsCISClient.setProjectCreateStrategy(projectCreateStrategy);
        jenkinsCISClient.createProject(project);

        verify(projectCreateStrategy).createAndConfig(JENKINS_URL, project, authenticationResult.getContext());

        //授权
        CISAuthorization cisAuthorization = mock(CISAuthorization.class);
        jenkinsCISClient.setAuthorizationStrategy(cisAuthorization);
        jenkinsCISClient.createUserIfNecessary(project, developer);
        verify(cisAuthorization).authorize(JENKINS_URL, project, authenticationResult.getContext(), developer);

    }


}
