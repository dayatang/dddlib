package org.openkoala.opencis.jenkins.configureImpl.project;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jenkins.configureApi.ProjectCreateStrategy;
import org.openkoala.opencis.jenkins.util.SeleniumUtil;
import org.openkoala.opencis.jenkins.util.UrlUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.concurrent.TimeUnit;

/**
 * 目前只版本只支持maven项目的创建
 * User: zjzhai
 * Date: 1/7/14
 * Time: 5:37 PM
 */
public class CreateMavenProject implements ProjectCreateStrategy {


    private String error;

    private String jenkinsUrl;

    private CreateMavenProject() {
    }

    public CreateMavenProject(String jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
    }


    @Override
    public boolean create(Project project, Object context) {
        WebDriver driver;
        if (context != null) {
            driver = (WebDriver) context;
        } else {
            driver = new HtmlUnitDriver();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        }

        driver.get(jenkinsUrl + "/view/All/newJob");
        WebElement jobNameInput = driver.findElement(By.id("name"));
        jobNameInput.sendKeys(project.getArtifactId());
        SeleniumUtil.clickBlankArea(driver);
        if (SeleniumUtil.elementExist(driver, By.cssSelector("div.error"))) {
            error = driver.findElement(By.cssSelector("div.error")).getText();
            driver.quit();
            return false;
        }


        WebElement jobTypeRadio = driver.findElement(By.cssSelector("input[value=\"hudson.maven.MavenModuleSet\"]"));
        jobTypeRadio.click();
        jobNameInput.submit();
        if (!driver.getCurrentUrl().contains("job/" + UrlUtil.encodeURL(project.getArtifactId()) + "/configure")
                || driver.getCurrentUrl().contains("view/All/createItem")) {
            error = "create job failure!";
            return false;
        }

        assert driver.getCurrentUrl().contains("/job/" + UrlUtil.encodeURL(project.getArtifactId()));

        driver.quit();
        return true;
    }

    public String getError() {
        return error;
    }
}
