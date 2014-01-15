package org.openkoala.opencis.jenkins;

import org.junit.Test;
import org.openkoala.opencis.CISClientAbstactIntegrationTest;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jenkins.configureImpl.authorize.ProjectAuthorization;
import org.openkoala.opencis.jenkins.configureImpl.project.MavenProjectCreator;
import org.openkoala.opencis.jenkins.configureImpl.scm.SvnConfig;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;


public class JenkinsCISClientIntegrationTest extends CISClientAbstactIntegrationTest {


    @Test
    public void test() throws MalformedURLException {

        Project project = getProject("20");

        WebDriver driver = ownAuthenticationAndCreateWebDriver();

        JenkinsCISClient client = new JenkinsCISClient(jenkinsURL, driver);

        MavenProjectCreator creator = new MavenProjectCreator();
        creator.setScmConfig(new SvnConfig(svnUrl, svnUser, svnPassword));
        client.setProjectCreateStrategy(creator);
        client.createProject(project);
        client.close();

        // TODO 添加自动验证
        Developer developer = getDeveloper("20");
        WebDriver driver1 = ownAuthenticationAndCreateWebDriver();
        JenkinsCISClient client1 = new JenkinsCISClient(jenkinsURL, driver1);
        client1.setAuthorizationStrategy(new ProjectAuthorization());
        client1.createUserIfNecessary(project, developer);
        client1.close();

        // TODO 添加自动验证

    }


}
