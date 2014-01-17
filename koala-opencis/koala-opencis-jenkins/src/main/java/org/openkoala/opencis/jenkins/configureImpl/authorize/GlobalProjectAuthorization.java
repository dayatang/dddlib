package org.openkoala.opencis.jenkins.configureImpl.authorize;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.jenkins.util.SeleniumUtil;
import org.openkoala.opencis.jenkins.util.UrlUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.concurrent.TimeUnit;

/**
 * User: zjzhai
 * Date: 1/8/14
 * Time: 11:36 PM
 */
public class GlobalProjectAuthorization {


    public GlobalProjectAuthorization() {
    }

    public void authorize(String jenkinsUrl, Object context, Developer... developers) {
        jenkinsUrl = UrlUtil.removeEndIfExists(jenkinsUrl, "/");
        WebDriver driver;
        if (context != null) {
            driver = (WebDriver) context;
        } else {
            driver = new HtmlUnitDriver();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        }

        driver.get(jenkinsUrl + "/configureSecurity/");

        for (Developer developer : developers) {
            //用户已经存在
            if (SeleniumUtil.elementExist(driver, By.cssSelector("table#hudson-security-ProjectMatrixAuthorizationStrategy tr.permission-row[name=\"[" + developer.getName() + "]\"]"))) {
                continue;
            }

            WebElement usernameInput =
                    driver.findElement(By.cssSelector("table[id=\"hudson-security-ProjectMatrixAuthorizationStrategy\"] +table input[type=\"text\"]"));

            usernameInput.sendKeys(developer.getName());

            WebElement addUserSubmitButton = driver.findElement(By.cssSelector("table[id=\"hudson-security-ProjectMatrixAuthorizationStrategy\"] +table button[type=\"button\"]"));
            addUserSubmitButton.click();

            //设置read权限
            WebElement readPermissionCheckbox = driver.findElement(
                    By.cssSelector("tr.permission-row[name=\"[" + developer.getName() + "]\"] input[name=\"[hudson.model.Hudson.Read]\"][type=\"checkbox\"]"));
            readPermissionCheckbox.click();


        }


        //保存配置
        WebElement saveButton = driver.findElement(By.cssSelector("span[name=\"Submit\"] button"));
        saveButton.click();

        assert driver.getCurrentUrl().equals(jenkinsUrl + "/manage");

    }
}
