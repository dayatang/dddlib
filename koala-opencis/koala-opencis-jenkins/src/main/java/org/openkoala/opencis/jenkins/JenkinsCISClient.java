package org.openkoala.opencis.jenkins;

import java.util.List;

import org.openkoala.opencis.api.AuthenticationStrategy;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.authorize.CISAuthorization;
import org.openkoala.opencis.jenkins.configureApi.ProjectCreateStrategy;
import org.openqa.selenium.WebDriver;

/**
 * Jenkins CIS客户端
 *
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:35:24 AM
 */
public class JenkinsCISClient implements CISClient {


    public static final String CREATE_ITEM_API = "/createItem?name=";

    private String jenkinsUrl;

    private WebDriver driver;

    /**
     * 授权
     */
    private CISAuthorization authorization;

    /**
     * 创建项目
     */
    private ProjectCreateStrategy projectCreateStrategy;


    private String errors;

    public JenkinsCISClient(String jenkinsUrl, WebDriver driver) {
        this.jenkinsUrl = jenkinsUrl;
        this.driver = driver;
    }


    public void setProjectCreateStrategy(ProjectCreateStrategy projectCreateStrategy) {
        this.projectCreateStrategy = projectCreateStrategy;
    }

    public void setAuthorizationStrategy(CISAuthorization authorization) {
        this.authorization = authorization;
    }


    @Override
    public void close() {
        driver.quit();
    }

    @Override
    public String getErrors() {
        return errors;
    }

    @Override
    public boolean createProject(Project project) {
        if (!projectCreateStrategy.createAndConfig(jenkinsUrl, project, driver)) {
            errors = projectCreateStrategy.getErrors();
            return false;
        }
        return true;
    }


    @Override
    public boolean createUserIfNecessary(Project project, Developer developer) {
        if (!authorization.authorize(jenkinsUrl, project, driver, developer)) {
            errors = authorization.getErrors();
            return false;
        }
        return true;

    }

    @Override
    public boolean createRoleIfNecessary(Project project, String roleName) {
        return false;

    }

    @Override
    public boolean assignUserToRole(Project project, String userId, String role) {
        return false;
    }


    @Override
    public boolean assignUsersToRole(Project project, List<String> userName,
                                     String role) {
        if (null == userName || userName.size() == 0) {
            return false;
        }
        for (String each : userName) {
            assignUserToRole(project, each, role);
        }
        return true;
    }
}
