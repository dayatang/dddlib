package org.openkoala.opencis.jenkins.authorize;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.authorize.CISAuthorization;
import org.openkoala.opencis.jenkins.util.SeleniumUtil;
import org.openkoala.opencis.jenkins.util.UrlUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 6:17 PM
 */
public class SeleniumProjectAuthorization implements CISAuthorization {

    private WebDriver driver;

    private String jenkinsUrl;


    public SeleniumProjectAuthorization(WebDriver driver, String jenkinsUrl) {
        this.driver = driver;
        this.jenkinsUrl = jenkinsUrl;
    }

    @Override
    public void authorize(Project project, Developer developer) {

        String jobConfigureUrl =
                UrlUtil.removeEndIfExists(jenkinsUrl, "/") + "/job/" + project.getArtifactId() + "/configure";

        driver.get(jobConfigureUrl);

        if (!SeleniumUtil.elementExist(driver, By.cssSelector("input[name=\"useProjectSecurity\"][type=\"checkbox\"]"))) {
            return;
        }

        WebElement userProjectSecurityCheckbox = driver.findElement(By.cssSelector("input[name=\"useProjectSecurity\"][type=\"checkbox\"]"));
        userProjectSecurityCheckbox.click();

        WebElement userProjectSecurityMartix =
                userProjectSecurityCheckbox.findElement(
                        By.xpath("../../following-sibling::tr/following-sibling::tr/td")
                );

        WebElement userInput = userProjectSecurityMartix.findElement(By.cssSelector("input[type=\"text\"]"));

        //添加用户名
        userInput.sendKeys(developer.getName());

        WebElement addUserSubmitButton = userProjectSecurityMartix.findElement(By.cssSelector("button[type=\"button\"]"));
        addUserSubmitButton.click();


        //设置read权限
        WebElement readPermissionCheckbox = driver.findElement(
                By.cssSelector("tr.permission-row[name=\"[" + developer.getName() + "]\"] input[name=\"[hudson.model.Item.Read]\"]"));
        readPermissionCheckbox.click();

        //保存配置
        WebElement saveButton = driver.findElement(By.cssSelector("span[name=\"Submit\"] button"));
        saveButton.click();

        assert driver.getCurrentUrl().contains("/job/" + UrlUtil.encodeURL(project.getArtifactId()));

        driver.quit();

    }


}
