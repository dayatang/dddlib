package org.openkoala.opencis.jenkins;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.CISClientAbstactIntegrationTest;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.authorize.CISAuthorization;
import org.openkoala.opencis.jenkins.authentication.JenkinsOwnAuthen;
import org.openkoala.opencis.jenkins.configureApi.ProjectCreateStrategy;
import org.openkoala.opencis.jenkins.configureApi.ScmConfigStrategy;
import org.openkoala.opencis.jenkins.configureImpl.authorize.GlobalProjectAuthorization;
import org.openkoala.opencis.jenkins.configureImpl.authorize.ProjectAuthorization;
import org.openkoala.opencis.jenkins.configureImpl.project.MavenProjectCreator;
import org.openkoala.opencis.jenkins.configureImpl.scm.SvnConfig;
import org.openkoala.opencis.jenkins.configureImpl.user.UserCreator;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.UUID;

public class JenkinsCISClientIntegrationTest extends CISClientAbstactIntegrationTest {


    @Test
    public void test() throws MalformedURLException {
        System.out.println(new Date());

        Project project = getProject("123");
        Developer developer = getDeveloper("1233");

        WebDriver driver = ownAuthenticationAndCreateWebDriver();
        ProjectCreateStrategy projectCreateStrategy = new MavenProjectCreator(jenkinsUrl.toString());
        projectCreateStrategy.create(project, driver);
        System.out.println(projectCreateStrategy.getError());


        driver = ownAuthenticationAndCreateWebDriver();
        ProjectAuthorization projectAuthorization = new ProjectAuthorization(jenkinsUrl.toString());
        projectAuthorization.authorize(project, developer, driver);
        System.out.println(projectAuthorization.getError());

        driver = ownAuthenticationAndCreateWebDriver();
        GlobalProjectAuthorization globalProjectAuthorization = new GlobalProjectAuthorization(jenkinsUrl.toString());
        globalProjectAuthorization.authorize(developer, driver);
        System.out.println(globalProjectAuthorization.getError());

        driver = ownAuthenticationAndCreateWebDriver();
        UserCreator userCreator = new UserCreator(jenkinsUrl.toString());
        userCreator.createUser(developer, driver);
        System.out.println(userCreator.getError());

        driver = ownAuthenticationAndCreateWebDriver();
        ScmConfigStrategy scmConfig = new SvnConfig(jenkinsUrl.toString() + "/job/" + project.getArtifactId() + "/configure", "http://10.108.1.138/svn/projec1", "admin", "admin");
        scmConfig.config(driver);
        System.out.println(scmConfig.getError());

        driver = ownAuthenticationAndCreateWebDriver();
        Developer developer1 = getDeveloper("12334");
        globalProjectAuthorization.authorize(developer1, driver);
        System.out.println(globalProjectAuthorization.getError());

        driver = ownAuthenticationAndCreateWebDriver();
        projectAuthorization.authorize(project, developer1, driver);
        System.out.println(projectAuthorization.getError());

        driver = ownAuthenticationAndCreateWebDriver();
        userCreator.createUser(developer1, driver);
        System.out.println(userCreator.getError());
        System.out.println(new Date());


    }


}
