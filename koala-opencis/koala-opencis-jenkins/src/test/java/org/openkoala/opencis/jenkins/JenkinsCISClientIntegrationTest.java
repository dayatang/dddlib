package org.openkoala.opencis.jenkins;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.authorize.CISAuthorization;
import org.openkoala.opencis.jenkins.authentication.SeleniumJenkinsOwnAuthen;
import org.openkoala.opencis.jenkins.authorize.SeleniumProjectAuthorize;
import org.openkoala.opencis.jenkins.project.ProjectCreateStrategy;
import org.openkoala.opencis.jenkins.project.SeleniumCreateProject;
import org.openkoala.opencis.jenkins.scm.SeleniumSvnConfig;
import org.openkoala.opencis.jenkins.util.UrlUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 9:46 PM
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JenkinsCISClientIntegrationTest {

    private static final String jobName = UUID.randomUUID().toString();
    private static URL jenkins_url = null;
    private static String jobConfigUrl;


    static {
        try {
            jenkins_url = new URL("http", "localhost", 8080, "/jenkins");
            jobConfigUrl = jenkins_url.toString() + "/job/" + UrlUtil.encodeURL(jobName) + "/configure";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test001CreateProject() throws Exception {
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        SeleniumJenkinsOwnAuthen cisAuthentication = new SeleniumJenkinsOwnAuthen(driver, jenkins_url.toString(), "admin", "admin");

        if (!cisAuthentication.authenticate()) {
            System.out.println("authentication error");
            return;
        }


        ProjectCreateStrategy projectCreateStrategy
                = new SeleniumCreateProject(jenkins_url.toString());

        projectCreateStrategy.create(getProject(), driver);


    }


    @Test
    public void test002SCM() throws MalformedURLException {

        WebDriver driver = authenticationAndCreateWebDriver();

        String svnUrl = "http://10.108.1.138/svn/project1";
        String svnUser = "admin";
        String svnPassword = "admin";
        SeleniumSvnConfig svnConfig =
                new SeleniumSvnConfig(jobConfigUrl, svnUrl, svnUser, svnPassword);
        svnConfig.config(driver);

    }

    @Test
    public void test003addUserToProject() {
        WebDriver driver = authenticationAndCreateWebDriver();
        CISAuthorization cisAuthorization = new SeleniumProjectAuthorize(jenkins_url.toString());
        cisAuthorization.authorize(getProject(), getDeveloper(), driver);

    }

    private WebDriver authenticationAndCreateWebDriver() {
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        SeleniumJenkinsOwnAuthen cisAuthentication =
                new SeleniumJenkinsOwnAuthen(driver, jenkins_url.toString(), "admin", "admin");

        if (!cisAuthentication.authenticate()) {
            System.out.println("authentication error");
            return null;
        }
        return driver;
    }

    private Developer getDeveloper() {
        Developer developer = new Developer();
        developer.setName("www");
        developer.setEmail("admin@gmail.com");
        return developer;
    }


    private Project getProject() {
        Project project = new Project();
        project.setArtifactId(jobName);
        return project;

    }
}
