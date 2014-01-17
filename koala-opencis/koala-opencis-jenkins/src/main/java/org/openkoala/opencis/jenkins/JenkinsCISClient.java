package org.openkoala.opencis.jenkins;

import java.util.List;

import org.openkoala.opencis.api.*;
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

    private AuthenticationStrategy authenticationStrategy;


    /**
     * 授权
     */
    private CISAuthorization authorization;

    /**
     * 创建项目
     */
    private ProjectCreateStrategy projectCreateStrategy;


    public JenkinsCISClient(String jenkinsUrl, AuthenticationStrategy authenticationStrategy) {
        this.jenkinsUrl = jenkinsUrl;
        this.authenticationStrategy = authenticationStrategy;
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
    public void createProject(Project project) {
        if (!projectCreateStrategy.createAndConfig(jenkinsUrl, project, driver)) {
        }
    }

    @Override
    public void removeProject(Project project) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        if (!authorization.authorize(jenkinsUrl, project, driver, developer)) {
            errors = authorization.getErrors();
        }

    }

    @Override
    public void removeUser(Developer developer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createRoleIfNecessary(Project project, String roleName) {

    }

    public void assignUserToRole(Project project, String userId, String role) {
    }


    public void assignUsersToRole(Project project,
                                  String role, String... userId) {
        for (String each : userId) {
            assignUserToRole(project, each, role);
        }
    }

    @Override
    public boolean authenticate() {
        AuthenticationResult authenticationResult = authenticationStrategy.authenticate();
        driver = (WebDriver) authenticationResult.getContext();
        return authenticationResult.isSuccess();
    }
}
