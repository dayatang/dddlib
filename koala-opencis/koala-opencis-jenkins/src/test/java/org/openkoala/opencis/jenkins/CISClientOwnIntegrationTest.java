package org.openkoala.opencis.jenkins;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openkoala.opencis.CISClientAbstactIntegrationTest;
import org.openkoala.opencis.authorize.CISAuthorization;
import org.openkoala.opencis.jenkins.configureImpl.authorize.ProjectAuthorization;
import org.openkoala.opencis.jenkins.configureImpl.project.MavenProjectCreator;
import org.openkoala.opencis.jenkins.configureApi.ProjectCreateStrategy;
import org.openkoala.opencis.jenkins.configureImpl.scm.GitConfig;
import org.openkoala.opencis.jenkins.configureImpl.scm.SvnConfig;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 9:46 PM
 */
@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CISClientOwnIntegrationTest extends CISClientAbstactIntegrationTest {

    @Test
    public void test001CreateProject() throws Exception {

        WebDriver driver = ownAuthenticationAndCreateWebDriver();
        ProjectCreateStrategy projectCreateStrategy
                = new MavenProjectCreator();
        projectCreateStrategy.createAndConfig(jenkinsUrl, getProject(), driver);
    }


    @Test
    public void test006SCM() throws MalformedURLException {
        WebDriver driver = ownAuthenticationAndCreateWebDriver();
        String svnUrl = "http://10.108.1.138/svn/projec2";
        String svnUser = "admin";
        String svnPassword = "admin";
        SvnConfig svnConfig =
                new SvnConfig(jobConfigUrl, svnUrl, svnUser, svnPassword);
        assert !svnConfig.config(driver);
    }

    @Test
    public void test009GIT() {
        WebDriver driver = ownAuthenticationAndCreateWebDriver();
        String gitUrl = "git@github.com:mitchellh/vagrant.git";
        GitConfig config =
                new GitConfig(jobConfigUrl, gitUrl);
        config.config(driver);
    }

    @Test
    public void test010addUserToProject() {
        WebDriver driver = ownAuthenticationAndCreateWebDriver();
        CISAuthorization cisAuthorization = new ProjectAuthorization();
        cisAuthorization.authorize(jenkinsUrl, getProject(), driver);
    }


}
