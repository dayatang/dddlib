package org.openkoala.opencis.jira.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jira.service.GiraConfiguration;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemotePermissionException;
import com.atlassian.jira.rpc.soap.client.RemoteProject;
import com.atlassian.jira.rpc.soap.client.RemoteProjectRole;
import com.atlassian.jira.rpc.soap.client.RemoteUser;

public class GiraCISClient implements CISClient {
	
	GiraConfiguration giraConfiguration;
	
	/**
	 * jira soap服务接口
	 */
	private JiraSoapService jiraService;
	
	protected void setJiraService(JiraSoapService jiraService) {
		this.jiraService = jiraService;
	}

	/**
	 * 用户登录返回的token
	 */
	private String token;
	
	public GiraCISClient(GiraConfiguration giraConfiguration){
		this.giraConfiguration = giraConfiguration;
		giraConfiguration.checkLoginInfoNotBlank();
		loginToJira(giraConfiguration);
	}

	@Override
	public void createProject(Project project) {
		checkProjectInfoNotBlank(project);
		
		project.setProjectKey(convertProjectKeyToValidFormat(project.getProjectKey()));
		
		if(checkProjectExistence(project)){
			throw new ProjectExistException("项目已经存在！");
		}
		
		if( !checkUserExistence(project.getProjectLead()) ){
			throw new UserNotExistException("用户 '" + project.getProjectLead() + "' 不存在！");
		}
		
		this.createProjectToJira(project);
	}
	
	/**
	 * 登陆到JIRA服务器
	 * @param loginInfo
	 */
	protected void loginToJira(GiraConfiguration giraConfiguration){
		if(jiraService == null){
			try {
				jiraService = this.getJiraSoapService(giraConfiguration.getServerAddress());
				token = jiraService.login(giraConfiguration.getAdminUserName(), giraConfiguration.getAdminPassword());
			} catch (RemoteAuthenticationException e) {
				throw new AdminUserNameOrPasswordErrorException("管理员登陆账号或密码错误！");
			} catch (Exception e) {
				throw new ServerAddressErrorException("服务器地址错误！");
			}
		}
	}
	
	/**
	 * 获取jira服务接口
	 * @param projectInfo
	 * @return
	 * @throws ServiceException
	 */
	private JiraSoapService getJiraSoapService(String serverAddress) throws ServiceException{
		JiraSoapServiceServiceLocator jiraSoapServiceLocator = new JiraSoapServiceServiceLocator();
		jiraSoapServiceLocator.setJirasoapserviceV2EndpointAddress(serverAddress
				+ "/rpc/soap/jirasoapservice-v2?wsdl");
		JiraSoapService jiraService = jiraSoapServiceLocator.getJirasoapserviceV2();
		return jiraService;
	}
	
	/**
	 * 检查项目是否存在
	 * @param projectInfo
	 * @return
	 */
	private boolean checkProjectExistence(Project project){
		RemoteProject[] remoteProjectArray = getAllProjects();
		if(remoteProjectArray== null || remoteProjectArray.length==0){
			return false;
		}
		for(RemoteProject remoteProject : remoteProjectArray){
			if(remoteProject.getKey().equals(project.getProjectKey())
					|| remoteProject.getName().equals(project.getProjectName())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 从JIRA上获取所有项目
	 * @param adminUserName
	 * @return
	 */
	private RemoteProject[] getAllProjects(){
		RemoteProject[] remoteProjectArray = null;
		try {
			remoteProjectArray = jiraService.getProjectsNoSchemes(token);
		} catch (RemotePermissionException e) {
			throw new UserPermissionNotEnoughException("该账号 '" + giraConfiguration.getAdminUserName() + "' 没有权限创建项目！");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return remoteProjectArray;
	}
	
	/**
	 * 检查用户是否存在
	 * @param userName
	 * @return
	 */
	private boolean checkUserExistence(String userName){
		RemoteUser remoteUser = null;
		try {
			remoteUser = jiraService.getUser(token, userName);
		} catch (RemotePermissionException e1) {
			throw new UserPermissionNotEnoughException("该账号没有权限查询用户！");
		} catch (RemoteAuthenticationException e1) {
			throw new RuntimeException("the token is invalid or the SOAP session has timed out,please login again.",e1);
		} catch (java.rmi.RemoteException e1) {
			throw new RuntimeException(e1);
		}
		return remoteUser != null;
	}
	
	private void createProjectToJira(Project project){
		try {
			jiraService.createProject(token, project.getProjectKey(), project.getProjectName(),
					project.getDescription(), giraConfiguration.getServerAddress(), project.getProjectLead(),
					jiraService.getPermissionSchemes(token)[0],null,null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	@Override
	public void createUserIfNecessary(Project project, Developer developer) {
		checkUserInfoNotBlank(developer);
		//用户存在，则不创建，忽略
		boolean userExisted = checkUserExistence(developer.getName());
		if( !userExisted ){
			createUser(developer);
		}
	}
	
	private boolean checkUserInfoNotBlank(Developer developer) {
		if(StringUtils.isBlank(developer.getName())){
			throw new UserNameBlankException("用户名不能为空！");
		}
		if(StringUtils.isBlank(developer.getPassword())){
			developer.setPassword(developer.getName());
		}
		if(StringUtils.isBlank(developer.getFullName())){
			throw new UserFullNameBlankException("用户全名不能为空！");
		}
		if(StringUtils.isBlank(developer.getEmail())){
			throw new UserEmailBlankException("用户邮箱不能为空！");
		}
		return true;
	}
	
	private void createUser(Developer developer){
		try {
			jiraService.createUser(token, developer.getName(), developer.getPassword(),
					developer.getFullName(), developer.getEmail());
		} catch (RemotePermissionException e) {
			throw new UserPermissionNotEnoughException("该账号没有权限创建用户！");
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	@Override
	public void createRoleIfNessceary(Project project, String roleName) {
		checkRoleNameNotBlank(roleName);
		//角色存在，则不创建
		if( !checkRoleExist(roleName)){
			this.createRole(roleName);
		}
	}
	
	private boolean checkRoleNameNotBlank(String roleName) {
		if(StringUtils.isBlank(roleName)){
			throw new RoleNameBlankException("角色名称不能为空！");
		}
		return true;
	}
	
	/**
	 * 检查角色是否存在
	 * @param roleInfo
	 * @return
	 */
	private boolean checkRoleExist(String roleName){
		List<String> roleNames = getAllRoleNamesFromJira();
		if(roleNames.isEmpty()){
			return false;
		}
		
		boolean roleExist = false;
		for(String everyRoleName : roleNames){
			if(everyRoleName.equals(roleName)){
				roleExist = true;
				break;
			}
		}
		return roleExist;
	}
	
	private List<String> getAllRoleNamesFromJira() {
		RemoteProjectRole[] remoteProjectRoleArray = this.getAllRoles();
		return this.getAllRoleNames(remoteProjectRoleArray);
	}
	
	/**
	 * 获取JIRA上的所有角色名称
	 * @param remoteProjectRoleArray
	 * @return
	 */
	private List<String> getAllRoleNames(RemoteProjectRole[] remoteProjectRoleArray){
		List<String> roleNames = new ArrayList<String>();
		for(RemoteProjectRole remoteProjectRole : remoteProjectRoleArray){
			roleNames.add(remoteProjectRole.getName());
		}
		return roleNames;
	}
	
	/**
	 * 获取JIRA上的所有角色
	 * @return
	 */
	private RemoteProjectRole[] getAllRoles(){
		RemoteProjectRole[] remoteProjectRoleArray = null;
		try {
			remoteProjectRoleArray = jiraService.getProjectRoles(token);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		return remoteProjectRoleArray;
	}
	
	/**
	 * 创建角色到JIRA
	 * @param roleInfo
	 */
	private void createRole(String roleName){
		RemoteProjectRole remoteProjectRole = packgingRemoteProjectRole(roleName);
		try {
			jiraService.createProjectRole(token, remoteProjectRole);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	/**
	 * 将角色信息封装成JIRA需要的角色对象
	 * @param roleInfo
	 * @return
	 */
	private RemoteProjectRole packgingRemoteProjectRole(String roleName){
		RemoteProjectRole remoteProjectRole = new  RemoteProjectRole();
		remoteProjectRole.setName(roleName);
//		remoteProjectRole.setDescription(roleInfo.getTypeDesc());
		return remoteProjectRole;
	}

	@Override
	public void assignUserToRole(Project project, String userName, String roleName) {
		this.checkProjectRoleUserAllExist(project, userName, roleName);
		this.addProjectRoleToUser(project.getProjectKey(), userName, roleName);
	}
	
	/**
	 * 检查项目、用户、角色是否都存在
	 * @param projectInfo
	 * @param userInfo
	 * @param roleInfo
	 * @return
	 */
	private boolean checkProjectRoleUserAllExist(Project project, String userName, String roleName){
		if( !checkProjectExistence(project) ){
			throw new ProjectNotExistException("项目不存在！");
		}
		if( !checkUserExistence(userName) ){
			throw new UserNotExistException("用户 '" + userName + "' 不存在");
		}
		if( !checkRoleExist(roleName) ){
			throw new RoleNotExistException("角色 '" + roleName + "' 不存在");
		}
		return true;
	}
	
	/**
	 * 为用户分配角色
	 * @param projectKey
	 * @param userName
	 * @param projectRoleName
	 */
	private void addProjectRoleToUser(String projectKey, String userName, String projectRoleName){
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
	 * @param roleName
	 * @return
	 */
	private RemoteProjectRole getRemoteProjectRoleId(String roleName){
		RemoteProjectRole[] remoteProjectRoleArray = this.getAllRoles();
		for(RemoteProjectRole remoteProjectRole : remoteProjectRoleArray){
			if(remoteProjectRole.getName().equals(roleName)){
				return remoteProjectRole;
			}
		}
		return null;
	}
	
	/**
	 * 通过project key获取JIRA对应的项目信息（含有项目id）
	 * @param projectKey
	 * @return
	 */
	private RemoteProject getRemoteProjectId(String projectKey){
		RemoteProject remoteProject = null;
		try {
			remoteProject = jiraService.getProjectByKey(token, projectKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		return remoteProject;
	}

	@Override
	public boolean canConnect() {
		return token != null;
	}
	
	/**
	 * 检查登陆信息和项目信息是否为空
	 */
	private boolean checkProjectInfoNotBlank(Project project) {
		if(StringUtils.isBlank(project.getProjectKey())){
			throw new ProjectKeyBlankException("project key不能为空！");
		}
		
		if(StringUtils.isBlank(project.getProjectLead())){
			throw new ProjectLeadBlankException("项目负责人不能为空！");
		}
		return true;
	}
	
	private String convertProjectKeyToValidFormat(String projectKey){
		return checkProjectKeyAndTurnToUppercase(projectKey);
	}
	
	/**
	 * 检查project key 必须都是英文字母，且必须都大写（由代码负责转换），且必须至少2个字母
	 * @param projectKey
	 */
	private String checkProjectKeyAndTurnToUppercase(String projectKey){
		if(projectKey.length() < 2 || projectKey.length() > 10){
			throw new ProjectKeyLengthNotBetweenTwoAndTenCharacterLettersException(
					"project key '" + projectKey + "' 长度必须为2-10个英文字母！");
		}
		for(int i=0; i<projectKey.length(); i++){
			char c = projectKey.charAt(i);
			if(!((c>=65 && c<=90) || (c>=97 && c<=120))){
				throw new ProjectKeyNotAllCharacterLettersException(
						"project key '" + projectKey + "' 必须都是英文字母！");
			}
		}
		return projectKey.toUpperCase();
	}
	
	/**
	 * 仅供单元测试调用
	 * @param projectInfo
	 */
	protected void removeProject(Project project) {
		try {
			jiraService.deleteProject(token, project.getProjectKey());
		} catch (RemotePermissionException e) {
			throw new UserPermissionNotEnoughException("该账号 '" + giraConfiguration.getAdminUserName() + "' 没有权限删除项目！");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 仅供单元测试调用
	 * @param projectInfo
	 */
	protected void removeUser(String userName){
		try {
			jiraService.deleteUser(token, userName);
		} catch (RemotePermissionException e) {
			throw new UserPermissionNotEnoughException("该账号 '" + giraConfiguration.getAdminUserName() + "' 没有权限删除项目！");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 仅供单元测试调用
	 * @param projectInfo
	 */
	protected void removeRole(String roleName){
		RemoteProjectRole remoteProjectRole = this.getRemoteProjectRoleId(roleName);
		try {
			jiraService.deleteProjectRole(token, remoteProjectRole, true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
