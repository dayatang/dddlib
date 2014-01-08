package org.openkoala.opencis.jenkins;

import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.openkoala.opencis.AuthenticationException;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.authentication.CISAuthentication;
import org.openkoala.opencis.authorize.CISAuthorization;
import org.openkoala.opencis.jenkins.project.ProjectCreateStrategy;

/**
 * Jenkins CIS客户端
 *
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:35:24 AM
 */
public class JenkinsCISClient implements CISClient {


    public static final String CREATE_ITEM_API = "/createItem?name=";

    private URL jenkinsUrl;

    /**
     * 授权
     */
    private CISAuthorization authorization;

    private ProjectCreateStrategy projectCreateStrategy;

    /**
     * 认证
     */
    private CISAuthentication cisAuthentication;

    private String createProjectUrl;


    private static Logger logger = Logger.getLogger(JenkinsCISClient.class);


    public JenkinsCISClient(URL jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
        createProjectUrl = jenkinsUrl.toString() + CREATE_ITEM_API;
    }

    /**
     * 认证
     */
    public boolean authenticateBy(CISAuthentication authentication) {
        authentication.setAppURL(jenkinsUrl);
        return authentication.authenticate();
    }

    public void setProjectCreateStrategy(ProjectCreateStrategy projectCreateStrategy) {
        this.projectCreateStrategy = projectCreateStrategy;
    }

    public void setAuthorization(CISAuthorization authorization) {
        this.authorization = authorization;
    }

    @Override
    public void createProject(Project project) {
        if (!authenticateBy(cisAuthentication)) {
            throw new AuthenticationException("CIS authentication failure");
        }
        projectCreateStrategy.create(project, cisAuthentication.getContext());
    }


    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        if (!authenticateBy(cisAuthentication)) {
            throw new AuthenticationException("CIS authentication failure");
        }
        authorization.authorize(project, developer, cisAuthentication.getContext());
    }

    @Override
    public void createRoleIfNecessary(Project project, String roleName) {
    }

    @Override
    public void assignUserToRole(Project project, String userId, String role) {



    }


    @Override
    public boolean canConnect() {
        return false;
    }


    @Override
    public void assignUsersToRole(Project project, List<String> userName,
                                  String role) {
        if (null == userName || userName.size() == 0) {
            return;
        }
        for (String each : userName) {
            assignUserToRole(project, each, role);
        }
    }
}
