package org.openkoala.opencis.jenkins.scm;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.opencis.jenkins.util.SeleniumUtil;
import org.openkoala.opencis.jenkins.util.UrlUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 6:01 PM
 */
public class SeleniumSvnConfig implements ScmConfigStrategy {

    private WebDriver driver;

    private String jobConfigUrl;

    private String svnUrl;

    private String username;

    private String password;

    private SeleniumSvnConfig() {
    }

    public SeleniumSvnConfig(WebDriver driver, String jobConfigUrl, String svnUrl, String username, String password) {
        this.driver = driver;
        this.svnUrl = svnUrl;
        this.username = username;
        this.password = password;
        this.jobConfigUrl = jobConfigUrl;
    }

    @Override
    public void config() {

        driver.get(jobConfigUrl);

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
        repositoryUrlInput.click();

        authenticationSubversionRepository();

        //保存配置
        WebElement saveButton = driver.findElement(By.cssSelector("span[name=\"Submit\"] button"));
        saveButton.click();
        assert StringUtils.endsWith(driver.getCurrentUrl(), "job/" + jobName + "/");

        driver.quit();
    }

    private void authenticationSubversionRepository() {
        if (!SeleniumUtil.elementExist(driver, By.cssSelector("a[href*=\"hudson.scm.SubversionSCM/enterCredential?\"]"))) {
            return;
        }
        WebElement creWebElement = driver.findElement(By.cssSelector("a[href*=\"hudson.scm.SubversionSCM/enterCredential?\"]"));

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

        usernameInput.sendKeys("admin");
        passwordInput.sendKeys("admin");

        driver.findElement(By.name("postCredential")).submit();

        if (driver.getPageSource().contains("Error")) {
            System.err.println("验证失败");
            return;
        }

        driver.switchTo().window(dashboardHandle);

    }
}
