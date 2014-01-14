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
public class CISClientCasIntegrationTest extends CISClientAbstactIntegrationTest {

    @Test
    public void test001CreateProject() throws Exception {
        WebDriver driver = casAuthenticationAndCreateWebDriver();
        ProjectCreateStrategy projectCreateStrategy
                = new MavenProjectCreator();

        projectCreateStrategy.createAndConfig(jenkinsUrl, getProject(), driver);

        String svnUrl = "http://10.108.1.138/svn/project1";
        String svnUser = "admin";
        String svnPassword = "admin";
        SvnConfig svnConfig =
                new SvnConfig(jobConfigUrl, svnUrl, svnUser, svnPassword);
        svnConfig.config(driver);


        CISAuthorization cisAuthorization = new ProjectAuthorization();
        cisAuthorization.authorize(jenkinsUrl, getProject(), driver, getDeveloper());
    }


}
