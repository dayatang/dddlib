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
 * Date: 1/7/14
 * Time: 6:01 PM
 */
public class SvnConfig implements ScmConfigStrategy {

    private String jobConfigUrl;

    private String svnUrl;

    private String username;

    private String password;
    private String error;

    private SvnConfig() {
    }

    public SvnConfig(String jobConfigUrl, String svnUrl, String username, String password) {
        this.svnUrl = svnUrl;
        this.username = username;
        this.password = password;
        this.jobConfigUrl = jobConfigUrl;
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
        String scm = "Subversion";
        WebElement selectedSCM = null;
        for (WebElement scmRadio : scmRadios) {
            WebElement label = scmRadio.findElement(By.xpath(".."));
            if (label.getText().contains(scm)) {
                selectedSCM = scmRadio;
                break;
            }
        }

        selectedSCM.click();

        WebElement repositoryUrlInput = driver.findElement(By.id("svn.remote.loc"));
        repositoryUrlInput.sendKeys(svnUrl);
        SeleniumUtil.clickBlankArea(driver);

        if(!authenticationSubversionRepository(driver)){
            return false;
        }

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

    private boolean authenticationSubversionRepository(WebDriver driver) {
        if (SeleniumUtil.elementExist(driver, By.cssSelector("a#svnerrorlink"))) {
            error = driver.findElement(By.xpath("//a[@id=\"svnerrorlink\"]/..")).getText();
            return false;
        }


        if (!SeleniumUtil.elementExist(driver,
                By.cssSelector("a[href*=\"hudson.scm.SubversionSCM/enterCredential?\"]"))) {
            return true;
        }
        WebElement creWebElement = driver.findElement(
                By.cssSelector("a[href*=\"hudson.scm.SubversionSCM/enterCredential?\"]"));

        creWebElement.click();

        String dashboardHandle = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (dashboardHandle.equals(handle)) {
                continue;
            }
            driver.switchTo().window(handle);
        }

        WebElement authenticationRadio = driver.findElement(By.cssSelector("input[name=\"kind\"][value=\"password\"]"));

        authenticationRadio.click();

        WebElement usernameInput = driver.findElement(By.cssSelector("input[name=\"username1\"]"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[name=\"password1\"]"));

        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);

        driver.findElement(By.name("postCredential")).submit();

        if (driver.getPageSource().contains("Error")) {
            error = "svn authentication failure";
            driver.quit();
            return false;
        }

        driver.switchTo().window(dashboardHandle);

        return true;
    }
}
