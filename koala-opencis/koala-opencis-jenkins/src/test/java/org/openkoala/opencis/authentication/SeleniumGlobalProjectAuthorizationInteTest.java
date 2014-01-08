package org.openkoala.opencis.authentication;

import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.authorize.CISAuthorization;
import org.openkoala.opencis.jenkins.authentication.SeleniumCasAuthen;
import org.openkoala.opencis.jenkins.authorize.SeleniumGlobalProjectAuthorization;
import org.openkoala.opencis.jenkins.authorize.SeleniumProjectAuthorize;
import org.openkoala.opencis.jenkins.util.UrlUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * User: zjzhai
 * Date: 1/8/14
 * Time: 11:52 PM
 */
public class SeleniumGlobalProjectAuthorizationInteTest {

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
    public void testName() throws Exception {
        WebDriver driver = authenticationAndCreateWebDriver();
        SeleniumGlobalProjectAuthorization cisAuthorization = new SeleniumGlobalProjectAuthorization(jenkins_url.toString());
        cisAuthorization.authorize(getProject(), getDeveloper(), driver);
    }


    private WebDriver authenticationAndCreateWebDriver() {
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        SeleniumCasAuthen cisAuthentication =
                new SeleniumCasAuthen(driver, jenkins_url, "admin", "admin");

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
