package org.openkoala.opencis.jenkins.configureImpl.scm;

import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.jenkins.configureApi.ScmConfigStrategy;
import org.openkoala.opencis.jenkins.configureImpl.ProjectConfigUtil;
import org.openkoala.opencis.jenkins.util.SeleniumUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 6:01 PM
 */
public class SvnConfig implements ScmConfigStrategy<WebDriver> {


    private String svnUrl;

    private String username;

    private String password;

    private SvnConfig() {
    }

    public SvnConfig(String svnUrl, String username, String password) {
        this.svnUrl = svnUrl;
        this.username = username;
        this.password = password;
    }

    public void config(WebDriver context) {

        String jobName = context.findElement(By.name("name")).getAttribute("value");

        String scm = "Subversion";
        WebElement selectedSCM = context.findElement(By.xpath("//label[contains(., '" + scm + "')]//input[@name=\"scm\"]"));

        selectedSCM.click();

        WebElement repositoryUrlInput = context.findElement(By.id("svn.remote.loc"));
        repositoryUrlInput.sendKeys(svnUrl);
        SeleniumUtil.clickBlankArea(context);

        authenticationSubversionRepository(context);

        ProjectConfigUtil.submitForm(context, jobName);

    }


    private void authenticationSubversionRepository(WebDriver driver) {
        if (SeleniumUtil.elementExist(driver, By.cssSelector("a#svnerrorlink"))) {
            throw new CISClientBaseRuntimeException(driver.findElement(By.xpath("//a[@id=\"svnerrorlink\"]/..")).getText());
        }


        if (!SeleniumUtil.elementExist(driver,
                By.cssSelector("a[href*=\"hudson.scm.SubversionSCM/enterCredential?\"]"))) {
            return;
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
            driver.quit();
            throw new CISClientBaseRuntimeException("svn authentication failure");
        }

        driver.switchTo().window(dashboardHandle);
    }
}
