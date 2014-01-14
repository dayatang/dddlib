package org.openkoala.opencis.jenkins.configureImpl.project;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jenkins.configureApi.ProjectCreateStrategy;
import org.openkoala.opencis.jenkins.configureApi.ScmConfigStrategy;
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
public class MavenProjectCreator implements ProjectCreateStrategy<WebDriver> {


    private String error;


    private ScmConfigStrategy scmConfig;

    public MavenProjectCreator() {
    }

    @Override
    public boolean createAndConfig(String jenkinsUrl, Project project, WebDriver context) {
        WebDriver driver;
        if (context != null) {
            driver = context;
        } else {
            driver = new HtmlUnitDriver();
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
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
        if (!driver.getCurrentUrl().contains("job/" + UrlUtil.encodeURL(project.getArtifactId())
                + "/configure")
                || driver.getCurrentUrl().contains("view/All/createItem")) {
            error = "create job failure!";
            driver.quit();
            return false;
        }

        assert driver.getCurrentUrl().contains("/job/" + UrlUtil.encodeURL(project.getArtifactId()));

        if (scmConfig != null && !scmConfig.config(driver)) {
            error = scmConfig.getErrors();
            return false;
        }

        return true;
    }

    public void setScmConfig(ScmConfigStrategy scmConfig) {
        this.scmConfig = scmConfig;
    }

    public String getErrors() {
        return error;
    }
}
