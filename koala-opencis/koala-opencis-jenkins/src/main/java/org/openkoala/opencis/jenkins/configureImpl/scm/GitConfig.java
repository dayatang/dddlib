package org.openkoala.opencis.jenkins.configureImpl.scm;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.opencis.jenkins.configureApi.ScmConfigStrategy;
import org.openkoala.opencis.jenkins.configureImpl.ProjectConfigUtil;
import org.openkoala.opencis.jenkins.util.SeleniumUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * User: zjzhai
 * Date: 1/9/14
 * Time: 10:44 AM
 */
public class GitConfig implements ScmConfigStrategy {
    private String jobConfigUrl;

    private String scmUrl;

    private String error;

    public GitConfig(String jobConfigUrl, String scmUrl) {
        this.jobConfigUrl = jobConfigUrl;
        this.scmUrl = scmUrl;
    }

    @Override
    public boolean config(Object context) {
        String directConfigurePageResult = ProjectConfigUtil.openJobConfigurePage(context, jobConfigUrl);
        if (directConfigurePageResult != null) {
            error = directConfigurePageResult;
            return false;
        }
        WebDriver driver = (WebDriver) context;


        String jobName = driver.findElement(By.name("name")).getAttribute("value");

        List<WebElement> scmRadios = driver.findElements(By.cssSelector("input[name=\"scm\"]"));
        String scm = "Git";
        WebElement selectedSCM = null;
        for (WebElement scmRadio : scmRadios) {
            WebElement label = scmRadio.findElement(By.xpath(".."));
            if (label.getText().contains(scm)) {
                selectedSCM = scmRadio;
                break;
            }
        }

        selectedSCM.click();

        WebElement repositoryUrlInput =
                driver.findElement(
                        By.cssSelector("input[checkurl*=\"hudson.plugins.git.UserRemoteConfig\"]"));
        repositoryUrlInput.sendKeys(scmUrl);
        SeleniumUtil.clickBlankArea(driver);


        repositoryUrlInput.click();

        String submitResult = ProjectConfigUtil.submitForm(driver, jobName);
        if (submitResult != null) {
            error = submitResult;
            return false;
        }

        return true;


    }

    @Override
    public String getError() {
        return error;
    }
}
