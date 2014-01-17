package org.openkoala.opencis.jenkins.configureImpl.authorize;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.exception.CISClientBaseRuntimeException;
import org.openkoala.opencis.jenkins.configureImpl.ProjectConfigUtil;
import org.openkoala.opencis.jenkins.configureImpl.user.UserCreator;
import org.openkoala.opencis.jenkins.util.SeleniumUtil;
import org.openkoala.opencis.jenkins.util.UrlUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目授权
 * User: zjzhai
 * Date: 1/7/14
 * Time: 6:17 PM
 */
public class ProjectAuthorization {


    private String errors;

    public ProjectAuthorization() {
    }

    public void authorize(String jenkinsBaseUrl, Project project, WebDriver driver, Developer... developers) {
        String jobConfigureUrl =
                UrlUtil.removeEndIfExists(jenkinsBaseUrl, "/") + "/job/" + project.getArtifactId() + "/configure";

        ProjectConfigUtil.openJobConfigurePage(driver, jobConfigureUrl);


        if (!SeleniumUtil.elementExist(driver, By.cssSelector("input[name=\"useProjectSecurity\"][type=\"checkbox\"]"))) {
            //jenkins没有开启权限控制
            throw new CISClientBaseRuntimeException("jenkins.not.openSecurity");

        }

        WebElement userProjectSecurityCheckbox = driver.findElement(By.cssSelector("input[name=\"useProjectSecurity\"]"));
        if (!"true".equals(userProjectSecurityCheckbox.getAttribute("checked"))) {
            userProjectSecurityCheckbox.click();
        }


        WebElement userProjectSecurityMartix =
                driver.findElement(
                        By.xpath("//input[@name=\"useProjectSecurity\"]/../../following-sibling::*[2]")
                );


        List<Developer> notExistUsers = new ArrayList<Developer>();
        for (Developer developer : developers) {
            //用户已经存在
            if (SeleniumUtil.elementExist(driver,
                    By.cssSelector("tr.permission-row[name=\"[" + developer.getName() + "]\"]"))) {
                continue;
            }

            WebElement userInput = userProjectSecurityMartix.findElement(By.cssSelector("input[type=\"text\"]"));
            //添加用户名
            userInput.sendKeys(developer.getName());

            WebElement addUserSubmitButton = userProjectSecurityMartix.findElement(By.cssSelector("button[type=\"button\"]"));
            addUserSubmitButton.click();

            //设置read权限
            WebElement readPermissionCheckbox = driver.findElement(
                    By.cssSelector(
                            "tr.permission-row[name=\"[" + developer.getName() + "]\"] input[name=\"[hudson.model.Item.Read]\"]"));
            readPermissionCheckbox.click();

            //jenkins数据库没有该用户
            if (SeleniumUtil.elementExist(driver,
                    By.cssSelector("tr.permission-row[name=\"[" + developer.getName() + "]\"] img[src*=\"error.png\"]"))) {
                notExistUsers.add(developer);
            }


        }


        ProjectConfigUtil.submitForm(driver, project.getArtifactId());



        //创建用户
        for (Developer developer : notExistUsers) {
            UserCreator userCreator = new UserCreator();
            userCreator.createUser(jenkinsBaseUrl, developer, driver);
        }
    }


}
