package org.openkoala.opencis;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jenkins.authentication.CasAuthen;
import org.openkoala.opencis.jenkins.authentication.JenkinsOwnAuthen;
import org.openkoala.opencis.jenkins.util.UrlUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * User: zjzhai
 * Date: 1/9/14
 * Time: 11:16 AM
 */
public abstract class CISClientAbstactIntegrationTest {

    public static final String jobName = UUID.randomUUID().toString();
    public static URL jenkinsUrl = null;
    public static String jobConfigUrl;


    static {
        try {
            jenkinsUrl = new URL("http", "localhost", 8080, "/jenkins");
            jobConfigUrl = jenkinsUrl.toString() + "/job/" + UrlUtil.encodeURL(jobName) + "/configure";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public WebDriver casAuthenticationAndCreateWebDriver() {
        WebDriver driver = new HtmlUnitDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        CasAuthen cisAuthentication =
                new CasAuthen(driver, jenkinsUrl, "admin", "admin");

        if (!cisAuthentication.authenticate()) {
            System.out.println("authentication error");
            return null;
        }
        return driver;
    }


    public WebDriver ownAuthenticationAndCreateWebDriver() {
      /*  FirefoxProfile profile = new FirefoxProfile();

        profile.setPreference("browser.link.open_newwindow.restriction", 1);
        FirefoxDriver driver  = new FirefoxDriver(profile);*/

        WebDriver driver = new HtmlUnitDriver(true);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        JenkinsOwnAuthen cisAuthentication =
                new JenkinsOwnAuthen(driver, jenkinsUrl.toString(), "admin", "admin");

        if (!cisAuthentication.authenticate()) {
            System.out.println("authentication error");
            return null;
        }
        return driver;
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
