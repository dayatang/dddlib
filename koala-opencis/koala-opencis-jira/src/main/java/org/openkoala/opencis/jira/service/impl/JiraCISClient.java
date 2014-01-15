package org.openkoala.opencis.jira.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.atlassian.jira.rpc.soap.client.*;
import com.atlassian.jira.rpc.soap.client.RemoteException;
import org.apache.commons.lang.StringUtils;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jira.service.JiraConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JiraCISClient implements CISClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JiraCISClient.class);

    JiraConfiguration jiraConfiguration;

    /**
     * jira soap服务接口
     */
    private JiraSoapService jiraService;


    private String errors;


    protected void setJiraService(JiraSoapService jiraService) {
        this.jiraService = jiraService;
    }

    /**
     * 用户登录返回的token
     */
    private String token;

    public JiraCISClient(JiraConfiguration jiraConfiguration) {
        this.jiraConfiguration = jiraConfiguration;
        loginToJira(jiraConfiguration);
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public String getErrors() {
        return errors;
    }

    @Override
    public boolean createProject(Project project) {
        checkProjectInfoNotBlank(project);

        project.setProjectKey(convertProjectKeyToValidFormat(project.getProjectKey()));

        if (checkProjectExistence(project)) {
            errors = "project:" + project.getArtifactId() + " is exist";
            return false;
        }

        if (!checkUserExistence(project.getProjectLead())) {
            errors = "user '" + project.getProjectLead() + "' is not exist！";
            return false;
        }

        return createProjectToJira(project);
    }

    /**
     * 登陆到JIRA服务器
     */
    protected boolean loginToJira(JiraConfiguration jiraConfiguration) {
        if (jiraService == null) {
            try {
                jiraService = this.getJiraSoapService(jiraConfiguration.getServerAddress());
                token = jiraService.login(jiraConfiguration.getAdminUserName(), jiraConfiguration.getAdminPassword());
            } catch (RemoteAuthenticationException e) {
                throw new AdminUserNameOrPasswordErrorException("管理员登陆账号或密码错误！");
            } catch (Exception e) {
                throw new ServerAddressErrorException("服务器地址错误！");
            }
        }
        return true;
    }

    /**
     * 获取jira服务接口
     */
    private JiraSoapService getJiraSoapService(String serverAddress) throws ServiceException {
        JiraSoapServiceServiceLocator jiraSoapServiceLocator = new JiraSoapServiceServiceLocator();
        jiraSoapServiceLocator.setJirasoapserviceV2EndpointAddress(serverAddress
                + "/rpc/soap/jirasoapservice-v2?wsdl");
        JiraSoapService jiraService = jiraSoapServiceLocator.getJirasoapserviceV2();
        return jiraService;
    }

    /**
     * 检查项目是否存在
     *
     * @return
     */
    private boolean checkProjectExistence(Project project) {
        RemoteProject[] remoteProjectArray = getAllProjects();
        if (remoteProjectArray == null || remoteProjectArray.length == 0) {
            return false;
        }
        for (RemoteProject remoteProject : remoteProjectArray) {
            if (remoteProject.getKey().equals(project.getProjectKey())
                    || remoteProject.getName().equals(project.getProjectName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从JIRA上获取所有项目
     *
     * @return
     */
    private RemoteProject[] getAllProjects() {
        RemoteProject[] remoteProjectArray = null;
        try {
            remoteProjectArray = jiraService.getProjectsNoSchemes(token);
        } catch (RemotePermissionException e) {
            throw new UserPermissionNotEnoughException("该账号 '" + jiraConfiguration.getAdminUserName() + "' 没有权限创建项目！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return remoteProjectArray;
    }

    /**
     * 检查用户是否存在
     *
     * @param userName
     * @return
     */
    private boolean checkUserExistence(String userName) {
        RemoteUser remoteUser = null;
        try {
            remoteUser = jiraService.getUser(token, userName);
        } catch (RemotePermissionException e1) {
            throw new UserPermissionNotEnoughException("该账号没有权限查询用户！");
        } catch (RemoteAuthenticationException e1) {
            throw new RuntimeException("the token is invalid or the SOAP session has timed out,please login again.", e1);
        } catch (java.rmi.RemoteException e1) {
            throw new RuntimeException(e1);
        }
        return remoteUser != null;
    }

    private boolean createProjectToJira(Project project) {
        try {
            jiraService.createProject(token, project.getProjectKey(), project.getProjectName(),
                    project.getDescription(), jiraConfiguration.getServerAddress(), project.getProjectLead(),
                    jiraService.getPermissionSchemes(token)[0], null, null);
            return true;
        } catch (RemotePermissionException e) {
            errors = "jira createProject permission denied";
            LOGGER.info(errors, e);
            return false;
        } catch (RemoteValidationException e) {
            errors = "jira RemoteValidationException";
            LOGGER.error(errors, e);
            return false;
        } catch (RemoteAuthenticationException e) {
            errors = "jira RemoteAuthenticationException";
            LOGGER.error(errors, e);
            return false;
        } catch (RemoteException e) {
            errors = "jira RemoteException";
            LOGGER.error(errors, e);
            return false;
        } catch (java.rmi.RemoteException e) {
            errors = "jira RemoteException";
            LOGGER.error(errors, e);
            return false;
        }
    }

    @Override
    public boolean createUserIfNecessary(Project project, Developer developer) {
        checkUserInfoNotBlank(developer);
        //用户存在，则不创建，忽略
        boolean userExisted = checkUserExistence(developer.getName());
        if (!userExisted) {
            return createUser(developer);
        }
        return true;
    }

    private boolean checkUserInfoNotBlank(Developer developer) {
        if (StringUtils.isBlank(developer.getName())) {
            errors = "developer";
        }
        if (StringUtils.isBlank(developer.getPassword())) {
            developer.setPassword(developer.getName());
        }
        if (StringUtils.isBlank(developer.getFullName())) {
            throw new UserFullNameBlankException("用户全名不能为空！");
        }
        if (StringUtils.isBlank(developer.getEmail())) {
            throw new UserEmailBlankException("用户邮箱不能为空！");
        }
        return true;
    }

    private boolean createUser(Developer developer) {
        try {
            jiraService.createUser(token, developer.getName(), developer.getPassword(),
                    developer.getFullName(), developer.getEmail());
        } catch (RemotePermissionException e) {
            errors = "current user create user permission denied";
            LOGGER.info(errors, e);
            return false;
        } catch (RemoteValidationException e) {
            errors = "jira RemoteValidationException when create user";
            LOGGER.error(errors, e);
            return false;
        } catch (RemoteAuthenticationException e) {
            errors = "current user create user permission denied";
            LOGGER.error(errors, e);
            return false;
        } catch (RemoteException e) {
            errors = "jira create user RemoteException";
            LOGGER.error(errors, e);
            return false;
        } catch (java.rmi.RemoteException e) {
            errors = "jira create user RemoteException";
            LOGGER.error(errors, e);

            return false;
        }
        return true;
    }

    @Override
    public boolean createRoleIfNecessary(Project project, String roleName) {
        //角色存在，则不创建
        if (!checkRoleExist(roleName)) {
            return createRole(roleName);
        }
        return true;
    }


    /**
     * 检查角色是否存在
     *
     * @return
     */
    private boolean checkRoleExist(String roleName) {
        List<String> roleNames = getAllRoleNamesFromJira();
        if (roleNames.isEmpty()) {
            return false;
        }

        boolean roleExist = false;
        for (String everyRoleName : roleNames) {
            if (everyRoleName.equals(roleName)) {
                roleExist = true;
                break;
            }
        }
        return roleExist;
    }

    private List<String> getAllRoleNamesFromJira() {
        RemoteProjectRole[] remoteProjectRoleArray = this.getAllRoles();
        return getAllRoleNames(remoteProjectRoleArray);
    }

    /**
     * 获取JIRA上的所有角色名称
     *
     * @param remoteProjectRoleArray
     * @return
     */
    private List<String> getAllRoleNames(RemoteProjectRole[] remoteProjectRoleArray) {
        List<String> roleNames = new ArrayList<String>();
        for (RemoteProjectRole remoteProjectRole : remoteProjectRoleArray) {
            roleNames.add(remoteProjectRole.getName());
        }
        return roleNames;
    }

    /**
     * 获取JIRA上的所有角色
     *
     * @return
     */
    private RemoteProjectRole[] getAllRoles() {
        RemoteProjectRole[] remoteProjectRoleArray = null;
        try {
            remoteProjectRoleArray = jiraService.getProjectRoles(token);
        } catch (Exception e) {
            errors = "find all roles failure";
            return null;
        }
        return remoteProjectRoleArray;
    }

    /**
     * 创建角色到JIRA
     */
    private boolean createRole(String roleName) {
        RemoteProjectRole remoteProjectRole = packgingRemoteProjectRole(roleName);
        try {
            jiraService.createProjectRole(token, remoteProjectRole);
        } catch (Exception e) {
            errors = "jira create role : " + roleName + " failure";
            return false;
        }
        return true;
    }

    /**
     * 将角色信息封装成JIRA需要的角色对象
     */
    private RemoteProjectRole packgingRemoteProjectRole(String roleName) {
        RemoteProjectRole remoteProjectRole = new RemoteProjectRole();
        remoteProjectRole.setName(roleName);
//		remoteProjectRole.setDescription(roleInfo.getTypeDesc());
        return remoteProjectRole;
    }

    @Override
    public boolean assignUserToRole(Project project, String userName, String roleName) {

        checkProjectRoleUserAllExist(project, userName, roleName);


        addProjectRoleToUser(project.getProjectKey(), userName, roleName);
        return true;
    }

    /**
     * 检查项目、用户、角色是否都存在
     *
     * @return
     */
    private boolean checkProjectRoleUserAllExist(Project project, String userName, String roleName) {
        if (!checkProjectExistence(project)) {
            errors = "project is not exist";
            return false;
        }
        if (!checkUserExistence(userName)) {
            errors = "user : " + userName + " is not exist";
            return false;
        }
        if (!checkRoleExist(roleName)) {
            errors = "role : " + roleName + " is not exist";
            return false;
        }
        return true;
    }

    /**
     * 为用户分配角色
     *
     * @param projectKey
     * @param userName
     * @param projectRoleName
     */
    private void addProjectRoleToUser(String projectKey, String userName, String projectRoleName) {
        String[] userNames = new String[]{userName};
        RemoteProjectRole remoteProjectRole = this.getRemoteProjectRoleId(projectRoleName);
        RemoteProject remoteProject = this.getRemoteProjectId(projectKey);
        //只有两种类型：atlassian-user-role-actor  atlassian-group-role-actor
        String actorType = "atlassian-user-role-actor";
        try {
            jiraService.addActorsToProjectRole(token, userNames, remoteProjectRole, remoteProject, actorType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过角色名称获取JIRA对应的角色信息（含有角色id）
     *
     * @param roleName
     * @return
     */
    private RemoteProjectRole getRemoteProjectRoleId(String roleName) {
        RemoteProjectRole[] remoteProjectRoleArray = this.getAllRoles();
        for (RemoteProjectRole remoteProjectRole : remoteProjectRoleArray) {
            if (remoteProjectRole.getName().equals(roleName)) {
                return remoteProjectRole;
            }
        }
        return null;
    }

    /**
     * 通过project key获取JIRA对应的项目信息（含有项目id）
     *
     * @param projectKey
     * @return
     */
    private RemoteProject getRemoteProjectId(String projectKey) {
        RemoteProject remoteProject = null;
        try {
            remoteProject = jiraService.getProjectByKey(token, projectKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return remoteProject;
    }

    /**
     * 检查登陆信息和项目信息是否为空
     */
    private boolean checkProjectInfoNotBlank(Project project) {
        if (StringUtils.isBlank(project.getProjectKey())) {
            throw new ProjectKeyBlankException("project key不能为空！");
        }

        if (StringUtils.isBlank(project.getProjectLead())) {
            throw new ProjectLeadBlankException("项目负责人不能为空！");
        }
        return true;
    }

    private String convertProjectKeyToValidFormat(String projectKey) {
        return checkProjectKeyAndTurnToUppercase(projectKey);
    }

    /**
     * 检查project key 必须都是英文字母，且必须都大写（由代码负责转换），且必须至少2个字母
     *
     * @param projectKey
     */
    private String checkProjectKeyAndTurnToUppercase(String projectKey) {
        if (projectKey.length() < 2 || projectKey.length() > 10) {
            throw new ProjectKeyLengthNotBetweenTwoAndTenCharacterLettersException(
                    "project key '" + projectKey + "' 长度必须为2-10个英文字母！");
        }
        for (int i = 0; i < projectKey.length(); i++) {
            char c = projectKey.charAt(i);
            if (!((c >= 65 && c <= 90) || (c >= 97 && c <= 120))) {
                throw new ProjectKeyNotAllCharacterLettersException(
                        "project key '" + projectKey + "' 必须都是英文字母！");
            }
        }
        return projectKey.toUpperCase();
    }

    /**
     * 仅供单元测试调用
     */
    protected void removeProject(Project project) {
        try {
            jiraService.deleteProject(token, project.getProjectKey());
        } catch (RemotePermissionException e) {
            throw new UserPermissionNotEnoughException("该账号 '" + jiraConfiguration.getAdminUserName() + "' 没有权限删除项目！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 仅供单元测试调用
     *
     * @param userName
     */
    protected void removeUser(String userName) {
        try {
            jiraService.deleteUser(token, userName);
        } catch (RemotePermissionException e) {
            throw new UserPermissionNotEnoughException("该账号 '" + jiraConfiguration.getAdminUserName() + "' 没有权限删除项目！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 仅供单元测试调用
     */
    protected void removeRole(String roleName) {
        RemoteProjectRole remoteProjectRole = this.getRemoteProjectRoleId(roleName);
        try {
            jiraService.deleteProjectRole(token, remoteProjectRole, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean assignUsersToRole(Project project, List<String> userName,
                                     String role) {
        // do nothing
        return false;
    }

}
