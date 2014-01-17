package org.openkoala.opencis.jenkins;

import org.openkoala.opencis.api.*;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.jenkins.configureApi.ScmConfigStrategy;
import org.openkoala.opencis.jenkins.configureImpl.authorize.GlobalProjectAuthorization;
import org.openkoala.opencis.jenkins.configureImpl.authorize.ProjectAuthorization;
import org.openkoala.opencis.jenkins.configureImpl.project.MavenProjectCreator;
import org.openqa.selenium.WebDriver;

/**
 * Jenkins CIS客户端
 *
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:35:24 AM
 */
public class JenkinsCISClient implements CISClient {

    private String jenkinsUrl;

    private WebDriver driver;

    private AuthenticationStrategy authenticationStrategy;


    /**
     * 源码版本控制 svn or git
     */
    private ScmConfigStrategy scmConfig;

    public JenkinsCISClient(String jenkinsUrl, AuthenticationStrategy authenticationStrategy) {
        this.jenkinsUrl = jenkinsUrl;
        this.authenticationStrategy = authenticationStrategy;
    }

    public void setScmConfig(ScmConfigStrategy scmConfig) {
        this.scmConfig = scmConfig;
    }

    @Override
    public void close() {
        driver.quit();
    }


    @Override
    public void createProject(Project project) {
        if (null == scmConfig) {
            throw new CISClientBaseRuntimeException("jenkins.scmConfig.null");
        }
        MavenProjectCreator creator = new MavenProjectCreator();
        creator.setScmConfig(scmConfig);
        creator.createAndConfig(jenkinsUrl, project, driver);

    }

    @Override
    public void removeProject(Project project) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        new ProjectAuthorization().authorize(jenkinsUrl, project, driver, developer);
    }

    @Override
    public void removeUser(Project project, Developer developer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createRoleIfNecessary(Project project, String roleName) {

    }

    public void assignUsersToRole(Project project,
                                  String role, Developer... developers) {
        new GlobalProjectAuthorization().authorize(jenkinsUrl, driver, developers);
    }

    @Override
    public boolean authenticate() {
        AuthenticationResult authenticationResult = authenticationStrategy.authenticate();
        driver = (WebDriver) authenticationResult.getContext();
        return authenticationResult.isSuccess();
    }
}
