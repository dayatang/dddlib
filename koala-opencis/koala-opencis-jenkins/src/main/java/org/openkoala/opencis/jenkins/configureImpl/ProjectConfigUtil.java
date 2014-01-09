package org.openkoala.opencis.jenkins.configureImpl;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.opencis.jenkins.configureApi.ScmConfigStrategy;
import org.openkoala.opencis.jenkins.util.SeleniumUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.concurrent.TimeUnit;

/**
 * User: zjzhai
 * Date: 1/9/14
 * Time: 2:21 PM
 */

public class ProjectConfigUtil {

    private static final int waitingTime = 2;

    public static String openJobConfigurePage(Object context, String jenkinsJobConfigUrl) {
        WebDriver driver;
        if (context != null) {
            driver = (WebDriver) context;
        } else {
            driver = new HtmlUnitDriver();
            driver.manage().timeouts().implicitlyWait(waitingTime, TimeUnit.SECONDS);
        }
        driver.get(jenkinsJobConfigUrl);

        if (SeleniumUtil.elementExist(driver, By.cssSelector("input[type=\"text\"][name=\"name\"]"))) {
            return null;
        }
        return "job is not exist.";
    }


    public static String submitForm(WebDriver driver, String jobName) {
        //保存配置
        WebElement saveButton = driver.findElement(By.cssSelector("span[name=\"Submit\"] button"));
        saveButton.click();

        if (StringUtils.endsWith(driver.getCurrentUrl(), "job/" + jobName + "/")) {
            return null;
        }
        return "project configure form submit failure";
    }


}
