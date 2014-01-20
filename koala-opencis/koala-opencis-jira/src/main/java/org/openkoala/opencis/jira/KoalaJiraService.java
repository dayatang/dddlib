package org.openkoala.opencis.jira;

import com.atlassian.jira.rpc.soap.client.*;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: zjzhai
 * Date: 1/19/14
 * Time: 2:36 PM
 */
public class KoalaJiraService {

    private String token;

    private JiraSoapService jiraService;


    private KoalaJiraService() {
    }

    public KoalaJiraService(String token, JiraSoapService jiraService) {
        this.token = token;
        this.jiraService = jiraService;
    }

    public RemoteProject[] getProjectsNoSchemes() throws RemoteException {
        return jiraService.getProjectsNoSchemes(token);
    }


    public RemoteUser getUser(String userName) throws RemoteException {
        return jiraService.getUser(token, userName);
    }


    public void createProject(Project project, String jiraServerAddress) throws RemoteException {
        jiraService.createProject(token, project.getProjectName(), project.getProjectName(),
                project.getDescription(), jiraServerAddress, project.getProjectLead(),
                jiraService.getPermissionSchemes(token)[0], null, null);
    }

    public void createRole(String roleName) throws RemoteException {
        RemoteProjectRole remoteProjectRole = new RemoteProjectRole();
        remoteProjectRole.setName(roleName);
        jiraService.createProjectRole(token, remoteProjectRole);
    }

    public boolean projectExists(Project project) {
        RemoteProject[] remoteProjectArray = getAllProjects();
        if (remoteProjectArray == null || remoteProjectArray.length == 0) {
            return false;
        }
        for (RemoteProject remoteProject : remoteProjectArray) {
            if (remoteProject.getKey().equals(project.getProjectName())
                    || remoteProject.getName().equals(project.getProjectName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取JIRA上的所有角色
     *
     * @return
     */
    public RemoteProjectRole[] getAllRoles() {
        RemoteProjectRole[] remoteProjectRoleArray = null;
        try {
            remoteProjectRoleArray = jiraService.getProjectRoles(token);
        } catch (java.rmi.RemoteException e) {
            throw new CISClientBaseRuntimeException("jira.remoteException", e);
        }
        return remoteProjectRoleArray;
    }

    public boolean roleExists(String roleName) {
        List<String> roleNames = null;
        try {
            roleNames = getAllProjectRoleNames();
        } catch (java.rmi.RemoteException e) {
            throw new CISClientBaseRuntimeException("jira.getAllRoleNameFailure");
        }
        if (roleNames.isEmpty()) {
            return false;
        }
        for (String everyRoleName : roleNames) {
            if (everyRoleName.equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    public boolean userExists(String userName) {
        RemoteUser remoteUser = null;
        try {
            remoteUser = jiraService.getUser(token, userName);
        } catch (RemotePermissionException e1) {
            throw new CISClientBaseRuntimeException("该账号没有权限查询用户！");
        } catch (RemoteAuthenticationException e1) {
            throw new RuntimeException("the token is invalid or the SOAP session has timed out,please login again.", e1);
        } catch (java.rmi.RemoteException e1) {
            throw new RuntimeException(e1);
        }
        return remoteUser != null;

    }

    public void deleteProject(String projectName) throws RemoteException {
        jiraService.deleteProject(token, projectName);
    }

    public RemoteProjectRole getRemoteProjectRoleId(String roleName) {
        RemoteProjectRole[] remoteProjectRoleArray = getAllRoles();
        for (RemoteProjectRole remoteProjectRole : remoteProjectRoleArray) {
            if (remoteProjectRole.getName().equals(roleName)) {
                return remoteProjectRole;
            }
        }
        return null;
    }

    public RemoteProject getRemoteProjectByProjectName(String projectName) {
        RemoteProject remoteProject = null;
        try {
            remoteProject = jiraService.getProjectByKey(token, projectName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return remoteProject;
    }

    public RemoteProject[] getAllProjects() {
        RemoteProject[] remoteProjectArray = null;
        try {
            remoteProjectArray = jiraService.getProjectsNoSchemes(token);
        } catch (RemotePermissionException e) {
            throw new CISClientBaseRuntimeException("jira.remotePermissionException", e);

        } catch (Exception e) {
            throw new CISClientBaseRuntimeException("jira.unkownException", e);
        }
        return remoteProjectArray;
    }


    public RemoteProjectRole[] getProjectRoles() throws RemoteException {
        return jiraService.getProjectRoles(token);
    }

    private List<String> getAllProjectRoleNames() throws RemoteException {
        List<String> roleNames = new ArrayList<String>();
        for (RemoteProjectRole remoteProjectRole : getProjectRoles()) {
            roleNames.add(remoteProjectRole.getName());
        }
        return roleNames;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public JiraSoapService getJiraService() {
        return jiraService;
    }

    public void setJiraService(JiraSoapService jiraService) {
        this.jiraService = jiraService;
    }

    public void createUser(Developer developer) throws RemoteException {
        jiraService.createUser(token, developer.getName(), developer.getPassword(), developer.getFullName(), developer.getEmail());
    }

    public void deleteUser(String userName) throws RemoteException {
        jiraService.deleteUser(token, userName);
    }

    public void addActorsToProjectRole(String projectName, String userName, String projectRoleName) throws RemoteException {
        String[] userNames = new String[]{userName};
        RemoteProjectRole remoteProjectRole = getRemoteProjectRoleId(projectRoleName);
        RemoteProject remoteProject = getRemoteProjectByProjectName(projectName);
        //只有两种类型：atlassian-user-role-actor  atlassian-group-role-actor
        String actorType = "atlassian-user-role-actor";
        jiraService.addActorsToProjectRole(token, userNames, remoteProjectRole, remoteProject, actorType);

    }
}
