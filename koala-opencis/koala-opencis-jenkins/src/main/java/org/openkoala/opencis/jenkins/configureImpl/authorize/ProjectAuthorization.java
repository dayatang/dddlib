package org.openkoala.opencis.jenkins.configureImpl.authorize;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.authorize.CISAuthorization;
import org.openkoala.opencis.jenkins.configureImpl.ProjectConfigUtil;
import org.openkoala.opencis.jenkins.configureImpl.user.GlobalProjectAuthorization;
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
public class ProjectAuthorization implements CISAuthorization<WebDriver> {


    private String errors;

    public ProjectAuthorization() {
    }

    @Override
    public boolean authorize(String jenkinsBaseUrl, Project project, WebDriver context, Developer... developers) {
        String jobConfigureUrl =
                UrlUtil.removeEndIfExists(jenkinsBaseUrl, "/") + "/job/" + project.getArtifactId() + "/configure";

        String directConfigurePageResult = ProjectConfigUtil.openJobConfigurePage(context, jobConfigureUrl);
        if (directConfigurePageResult != null) {
            errors = directConfigurePageResult;
            return false;
        }
        WebDriver driver = context;

        if (!SeleniumUtil.elementExist(driver, By.cssSelector("input[name=\"useProjectSecurity\"][type=\"checkbox\"]"))) {
            errors = "not found useProjectSecurity checkbox";
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


        String submitResult = ProjectConfigUtil.submitForm(driver, project.getArtifactId());
        if (submitResult != null) {
            errors = submitResult;
            return false;
        }

        // 需要设置全局的权限
        GlobalProjectAuthorization globalProjectAuthorization = new GlobalProjectAuthorization();
        if (!globalProjectAuthorization.authorize(jenkinsBaseUrl, driver, developers)) {
            errors = globalProjectAuthorization.getErrors();
            return false;
        }

        System.out.println(notExistUsers);

        //创建用户
        for (Developer developer : notExistUsers) {
            UserCreator userCreator = new UserCreator();
            userCreator.createUser(jenkinsBaseUrl, developer, driver);
        }


        return true;
    }

    @Override
    public String getErrors() {
        return errors;
    }


}
