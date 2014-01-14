package org.openkoala.opencis.jenkins;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.CISClientAbstactIntegrationTest;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jenkins.configureImpl.user.GlobalProjectAuthorization;
import org.openkoala.opencis.jenkins.configureImpl.authorize.ProjectAuthorization;
import org.openkoala.opencis.jenkins.configureImpl.project.MavenProjectCreator;
import org.openkoala.opencis.jenkins.configureImpl.scm.SvnConfig;
import org.openkoala.opencis.jenkins.configureImpl.user.UserCreator;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.util.Date;


@Ignore
public class JenkinsCISClientIntegrationTest extends CISClientAbstactIntegrationTest {


    @Test
    public void test() throws MalformedURLException {

        Project project = getProject("11111");
        Developer developer = getDeveloper("sdgdfa111");

        WebDriver driver = ownAuthenticationAndCreateWebDriver();
        MavenProjectCreator projectCreateStrategy = new MavenProjectCreator(jenkinsUrl.toString());
        projectCreateStrategy.setScmConfig(
                new SvnConfig(jenkinsUrl.toString() + "/job/" + project.getArtifactId()
                        + "/configure", "http://10.108.1.138/svn/projec1", "admin", "admin"));
        projectCreateStrategy.createAndConfig(project, driver);
        System.out.println(projectCreateStrategy.getErrors());


        driver = ownAuthenticationAndCreateWebDriver();
        ProjectAuthorization projectAuthorization = new ProjectAuthorization(jenkinsUrl.toString());
        projectAuthorization.authorize(project, driver, developer);
        System.out.println(projectAuthorization.getErrors());

        driver = ownAuthenticationAndCreateWebDriver();
        GlobalProjectAuthorization globalProjectAuthorization = new GlobalProjectAuthorization(jenkinsUrl.toString());
        globalProjectAuthorization.authorize(developer, driver);
        System.out.println(globalProjectAuthorization.getErrors());

        driver = ownAuthenticationAndCreateWebDriver();
        UserCreator userCreator = new UserCreator(jenkinsUrl.toString());
        userCreator.createUser(developer, driver);
        System.out.println(userCreator.getError());


        driver = ownAuthenticationAndCreateWebDriver();
        Developer developer1 = getDeveloper("asfsd22f");
        globalProjectAuthorization.authorize(developer1, driver);
        System.out.println(globalProjectAuthorization.getErrors());

        driver = ownAuthenticationAndCreateWebDriver();
        projectAuthorization.authorize(project, driver, developer1);
        System.out.println(projectAuthorization.getErrors());

        driver = ownAuthenticationAndCreateWebDriver();
        userCreator.createUser(developer1, driver);
        System.out.println(userCreator.getError());
        System.out.println(new Date());
    }


}
