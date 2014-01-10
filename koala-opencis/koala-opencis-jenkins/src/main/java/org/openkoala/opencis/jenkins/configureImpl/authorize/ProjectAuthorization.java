package org.openkoala.opencis.jenkins.configureImpl.authorize;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.authorize.CISAuthorization;
import org.openkoala.opencis.jenkins.configureImpl.ProjectConfigUtil;
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
public class ProjectAuthorization implements CISAuthorization {


    private String jenkinsUrl;
    private String error;


    public ProjectAuthorization(String jenkinsUrl) {
        this.jenkinsUrl = UrlUtil.removeEndIfExists(jenkinsUrl, "/");
    }

    @Override
    public boolean authorize(Project project, Object context, Developer... developers) {
        String jobConfigureUrl =
                UrlUtil.removeEndIfExists(jenkinsUrl, "/") + "/job/" + project.getArtifactId() + "/configure";
        String directConfigurePageResult = ProjectConfigUtil.openJobConfigurePage(context, jobConfigureUrl);
        if (directConfigurePageResult != null) {
            error = directConfigurePageResult;
            return false;
        }
        WebDriver driver = (WebDriver) context;

        if (!SeleniumUtil.elementExist(driver, By.cssSelector("input[name=\"useProjectSecurity\"][type=\"checkbox\"]"))) {
            error = "not found useProjectSecurity checkbox";
            return false;
        }

        WebElement userProjectSecurityCheckbox = driver.findElement(By.cssSelector("input[name=\"useProjectSecurity\"]"));
        if (!"true".equals(userProjectSecurityCheckbox.getAttribute("checked"))) {
            userProjectSecurityCheckbox.click();
        }


        WebElement userProjectSecurityMartix =
                driver.findElement(
                        By.xpath("//input[@name=\"useProjectSecurity\"]/../../following-sibling::*[2]")
                );


        for (Developer developer : developers) {
            //用户已经存在
            if (SeleniumUtil.elementExist(driver,
                    By.cssSelector("tr.permission-row[name=\"[" + developer.getName() + "]\"]"))) {
                driver.quit();
                return true;
            }

            WebElement userInput = userProjectSecurityMartix.findElement(By.cssSelector("input[type=\"text\"]"));
            //添加用户名
            userInput.sendKeys(developer.getName());

            WebElement addUserSubmitButton = userProjectSecurityMartix.findElement(By.cssSelector("button[type=\"button\"]"));
            addUserSubmitButton.click();


            //设置read权限
            WebElement readPermissionCheckbox = driver.findElement(
                    By.cssSelector("tr.permission-row[name=\"[" + developer.getName() + "]\"] input[name=\"[hudson.model.Item.Read]\"]"));
            readPermissionCheckbox.click();
        }


        String submitResult = ProjectConfigUtil.submitForm(driver, project.getArtifactId());
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
