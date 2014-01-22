package org.openkoala.opencis;

import org.openkoala.opencis.api.AuthenticationResult;
import org.openkoala.opencis.api.AuthenticationStrategy;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jenkins.authentication.CasAuthentication;
import org.openkoala.opencis.jenkins.authentication.JenkinsOwnAuthentication;
import org.openkoala.opencis.jenkins.util.UrlUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * User: zjzhai
 * Date: 1/9/14
 * Time: 11:16 AM
 */
public abstract class CISClientAbstactIntegrationTest {

    public static final String jobName = UUID.randomUUID().toString();
    public static String jenkinsURL = "http://10.108.1.138:8080/jenkins";
    public static String jobConfigURL = jenkinsURL + "/job/" + UrlUtil.encodeURL(jobName) + "/configure";
    public static String svnUrl = "http://10.108.1.138/svn/projec2";
    public static String svnUser = "admin";
    public static String svnPassword = "admin";


    public AuthenticationStrategy casAuthenticationAndCreateWebDriver() {
        WebDriver driver = new HtmlUnitDriver(true);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        CasAuthentication cisAuthentication =
                new CasAuthentication(driver, jenkinsURL, "admin", "admin");


        return cisAuthentication;
    }


    public AuthenticationStrategy ownAuthenticationAndCreateWebDriver() {
      /*  FirefoxProfile profile = new FirefoxProfile();

        profile.setPreference("browser.link.open_newwindow.restriction", 1);
        FirefoxDriver driver  = new FirefoxDriver(profile);*/

        WebDriver driver = new HtmlUnitDriver(true);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        JenkinsOwnAuthentication cisAuthentication =
                new JenkinsOwnAuthentication(driver, jenkinsURL.toString(), "admin", "admin");

        return cisAuthentication;
    }

    public Developer getDeveloper() {
        Developer developer = new Developer();
        developer.setName("ww2");
        developer.setPassword("1111");
        developer.setEmail("admin@gmail.com");
        return developer;
    }

    public Developer getDeveloper(String name) {
        Developer developer = new Developer();
        developer.setName(name);
        developer.setPassword("1111");
        developer.setEmail("admin@gmail.com");
        return developer;
    }


    public Project getProject() {
        Project project = new Project();
        project.setArtifactId(jobName);
        return project;
    }

    public Project getProject(String name) {
        Project project = new Project();
        project.setArtifactId(name);
        return project;
    }


}
