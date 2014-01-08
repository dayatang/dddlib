package org.openkoala.opencis.jenkins;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jenkins.authentication.jenkinsOwnUserDatabaseAuthen.SeleniumAuthen;
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
@Ignore
public class JenkinsCISClientIntegrationTest {

    private static final String jobName = UUID.randomUUID().toString();
    private static URL jenkins_url = null;

    static {
        try {
            jenkins_url = new URL("http", "localhost", 8080, "/jenkins");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testName() throws Exception {

        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        SeleniumAuthen cisAuthentication = new SeleniumAuthen(driver, jenkins_url.toString(), "admin", "admin");

        if (!cisAuthentication.authenticate()) {
            System.out.println("authentication error");
            return;
        }


        ProjectCreateStrategy projectCreateStrategy
                = new SeleniumCreateProject(driver, jenkins_url.toString());

        projectCreateStrategy.create(getProject());


    }


    @Test
    public void setSCM() throws MalformedURLException {

        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);


        SeleniumAuthen cisAuthentication = new SeleniumAuthen(driver, jenkins_url.toString(), "admin", "admin");

        if (!cisAuthentication.authenticate()) {
            System.out.println("authentication error");
            return;
        }
        String svnUrl = "http://10.108.1.138/svn/project1";
        String svnUser = "admin";
        String svnPassword = "admin";
        String jobConfigUrl = jenkins_url.toString() + "/job/" + UrlUtil.encodeURL(jobName) + "/configure";
        SeleniumSvnConfig svnConfig = new SeleniumSvnConfig(driver, jobConfigUrl, svnUrl, svnUser, svnPassword);
        svnConfig.config();

    }

    private Project getProject() {
        Project project = new Project();
        project.setArtifactId(jobName);
        return project;

    }
}
