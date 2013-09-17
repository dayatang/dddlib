package org.openkoala.opencis.jira.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.openkoala.opencis.jira.service.JiraOperateService;
import org.openkoala.opencis.jira.service.JiraLoginInfo;
import org.openkoala.opencis.jira.service.JiraProjectInfo;
import org.openkoala.opencis.jira.service.JiraRoleInfo;
import org.openkoala.opencis.jira.service.JiraUserInfo;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemotePermissionException;
import com.atlassian.jira.rpc.soap.client.RemoteProject;
import com.atlassian.jira.rpc.soap.client.RemoteProjectRole;
import com.atlassian.jira.rpc.soap.client.RemoteUser;

public class JiraOperateServiceImpl implements JiraOperateService {
	
	/**
	 * jira soap服务接口
	 */
	private JiraSoapService jiraService;
	
	/**
	 * 用户登录返回的token
	 */
	private String token;

	public void createProjectToJira(JiraProjectInfo projectInfo) {
		projectInfo.checkNotBlank();
		this.loginToJira(projectInfo);
		
		boolean projectExisted = this.checkProjectExistence(projectInfo);
		if(projectExisted){
			throw new ProjectExistException("项目已经存在！");
		}
		
		boolean userExisted = this.checkUserExistence(projectInfo.getProjectLead());
		if( !userExisted ){
			throw new UserNotExistException("用户 '" + projectInfo.getProjectLead() + "' 不存在！");
		}
		
		this.createProject(projectInfo);
	}
	
	public void createUserToJiraIfNecessary(JiraUserInfo userInfo) {
		userInfo.checkNotBlank();
		this.loginToJira(userInfo);
		
		//用户存在，则不创建，忽略
		boolean userExisted = this.checkUserExistence(userInfo.getUserName());
		if( !userExisted ){
			//throw new UserExistException("用户 '" + userInfo.getUserName() + "' 已经存在！");
			this.createUser(userInfo);
		}
	}

	public void createRoleToJira(JiraRoleInfo roleInfo) {
		roleInfo.checkNotBlank();
		this.loginToJira(roleInfo);
		
		//角色存在，则不创建，忽略
		boolean roleExist = this.checkRoleExist(roleInfo);
		if( !roleExist ){
			//throw new RoleExistException("角色 '" + roleInfo.getRoleName() + "' 已经存在！");
			this.createRole(roleInfo);
		}
	}
	
	//assign..
	public void addProjectRoleToUser(JiraProjectInfo projectInfo, JiraUserInfo userInfo, JiraRoleInfo roleInfo) {
		this.loginToJira(projectInfo);
		this.checkProjectRoleUserAllExist(projectInfo, userInfo, roleInfo);
		this.addProjectRoleToUser(projectInfo.getProjectKey(), userInfo.getUserName(), roleInfo.getRoleName());
	}
	
	//Set
	public List<String> getAllRoleNamesFromJira(JiraLoginInfo loginInfo) {
		this.loginToJira(loginInfo);
		RemoteProjectRole[] remoteProjectRoleArray = this.getAllRoles();
		return this.getAllRoleNames(remoteProjectRoleArray);
	}
	
	/**
	 * 创建角色到JIRA
	 * @param roleInfo
	 */
	private void createRole(JiraRoleInfo roleInfo){
		RemoteProjectRole remoteProjectRole = this.packgingRemoteProjectRole(roleInfo);
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
	private RemoteProjectRole packgingRemoteProjectRole(JiraRoleInfo roleInfo){
		RemoteProjectRole remoteProjectRole = new  RemoteProjectRole();
		remoteProjectRole.setName(roleInfo.getRoleName());
		remoteProjectRole.setDescription(roleInfo.getTypeDesc());
		return remoteProjectRole;
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
	 * 检查项目、用户、角色是否都存在
	 * @param projectInfo
	 * @param userInfo
	 * @param roleInfo
	 * @return
	 */
	private boolean checkProjectRoleUserAllExist(JiraProjectInfo projectInfo, JiraUserInfo userInfo, JiraRoleInfo roleInfo){
		boolean projectExisted = this.checkProjectExistence(projectInfo);
		if( !projectExisted ){
			throw new ProjectNotExistException("项目不存在！");
		}
		
		boolean userExisted = this.checkUserExistence(userInfo.getUserName());
		if( !userExisted ){
			throw new UserNotExistException("用户 '" + userInfo.getUserName() + "' 不存在");
		}
		
		boolean roleExist = this.checkRoleExist(roleInfo);
		if( !roleExist ){
			throw new RoleNotExistException("角色 '" + roleInfo.getRoleName() + "' 不存在");
		}
		
		return true;
	}
	
	/**
	 * 通过project key获取JIRA对应的项目信息（含有项目id）
	 * @param projectKey
	 * @return
	 *///getRemoteProject
	private RemoteProject getRemoteProjectId(String projectKey){
		RemoteProject remoteProject = null;
		try {
			remoteProject = jiraService.getProjectByKey(token, projectKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		return remoteProject;
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
	 * 仅供单元测试调用
	 * @param projectInfo
	 */
	protected void removeProject(JiraProjectInfo projectInfo) {
		try {
			this.loginToJira(projectInfo);
			jiraService.deleteProject(token, projectInfo.getProjectKey());
		} catch (RemotePermissionException e) {
			throw new UserPermissionNotEnoughException("该账号 '" + projectInfo.getAdminUserName() + "' 没有权限删除项目！");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 仅供单元测试调用
	 * @param projectInfo
	 */
	protected void removeUser(JiraUserInfo userInfo){
		try {
			this.loginToJira(userInfo);
			jiraService.deleteUser(token, userInfo.getUserName());
		} catch (RemotePermissionException e) {
			throw new UserPermissionNotEnoughException("该账号 '" + userInfo.getAdminUserName() + "' 没有权限删除项目！");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 仅供单元测试调用
	 * @param projectInfo
	 */
	protected void removeRole(JiraRoleInfo roleInfo){
		RemoteProjectRole remoteProjectRole = this.getRemoteProjectRoleId(roleInfo.getRoleName());
		try {
			jiraService.deleteProjectRole(token, remoteProjectRole, true);
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
	 * 检查项目信息是否为空（若用户输入的project key不全为大写，则代码将其转成大写）
	 * @param projectInfo
	 * @return
	 */
	/*private boolean checkProjectNotBlank(JiraProjectInfo projectInfo){
		
		projectInfo.checkNotBlank();
		
		if(StringManager.isEmpty(projectInfo.getProjectKey())){//StringUtils.isBlank(str)
			throw new ProjectKeyBlankException("project key不能为空！");
		}
		
		projectInfo.setProjectKey(checkProjectKeyAndTurnToUppercase(projectInfo.getProjectKey()));
		
		if(StringManager.isEmpty(projectInfo.getProjectLead())){
			throw new ProjectLeadBlankException("项目负责人不能为空！");
		}
		return true;
	}*/
	
	/**
	 * 检查用户信息是否为空（若未输入密码，则密码默认等于用户名）
	 * @param userInfo
	 * @return
	 */
	/*private boolean checkUserNotBlank(JiraUserInfo userInfo){
		
		this.checkLoginInfoNotBlank(userInfo);
		
		if(StringManager.isEmpty(userInfo.getUserName())){
			throw new UserNameBlankException("用户名不能为空！");
		}
		if(StringManager.isEmpty(userInfo.getPassword())){
			userInfo.setPassword(userInfo.getUserName());
		}
		if(StringManager.isEmpty(userInfo.getFullName())){
			throw new UserFullNameBlankException("用户全名不能为空！");
		}
		if(StringManager.isEmpty(userInfo.getEmail())){
			throw new UserEmailBlankException("用户邮箱不能为空！");
		}
		return true;
	}*/
	
	/**
	 * 检查角色信息是否为空
	 * @param roleInfo
	 * @return
	 */
	/*private boolean checkRoleNotBlank(JiraRoleInfo roleInfo){
		roleInfo.checkNotBlank();
		if(StringManager.isEmpty(roleInfo.getRoleName())){
			throw new RoleNameBlankException("角色名称不能为空！");
		}
		return true;
	}*/
	
	/**
	 * 检查登陆信息是否为空
	 * @param loginInfo
	 * @return
	 */
	/*private boolean checkLoginInfoNotBlank(JiraLoginInfo loginInfo){
		if(StringManager.isEmpty(loginInfo.getServerAddress())){
			throw new ServerAddressBlankException("服务器地址不能为空！");
		}
		if(StringManager.isEmpty(loginInfo.getAdminUserName())){
			throw new AdminUserNameBlankException("管理员登陆账号不能为空！");
		}
		if(StringManager.isEmpty(loginInfo.getAdminPassword())){
			throw new AdminPasswordBlankException("管理员登陆密码不能为空！");
		}
		return true;
	}*/
	
	/**
	 * 检查project key 必须都是英文字母，且必须都大写（由代码负责转换），且必须至少2个字母
	 * @param projectKey
	 */
	/*private String checkProjectKeyAndTurnToUppercase(String projectKey){
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
	}*/
	
	/**
	 * 登陆到JIRA服务器
	 * @param loginInfo
	 */
	private void loginToJira(JiraLoginInfo loginInfo){
		if(jiraService == null){
			try {
				jiraService = this.getJiraSoapService(loginInfo.getServerAddress());
				token = jiraService.login(loginInfo.getAdminUserName(), loginInfo.getAdminPassword());
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
	private boolean checkProjectExistence(JiraProjectInfo projectInfo){
		RemoteProject[] remoteProjectArray = this.getAllProjects(projectInfo.getAdminUserName());
		if(remoteProjectArray== null || remoteProjectArray.length==0){
			return false;
		}
		return this.checkProjectExist(remoteProjectArray, projectInfo);
	}
	
	/**
	 * 从JIRA上获取所有项目
	 * @param adminUserName
	 * @return
	 */
	private RemoteProject[] getAllProjects(String adminUserName){
		RemoteProject[] remoteProjectArray = null;
		try {
			remoteProjectArray = jiraService.getProjectsNoSchemes(token);
		} catch (RemotePermissionException e) {
			throw new UserPermissionNotEnoughException("该账号 '" + adminUserName + "' 没有权限创建项目！");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return remoteProjectArray;
	}
	
	/**
	 * 遍历所有项目，检查指定项目key或项目名称是否存在
	 * @param remoteProjectArray
	 * @param projectInfo
	 * @return
	 */
	private boolean checkProjectExist(RemoteProject[] remoteProjectArray, JiraProjectInfo projectInfo){
		for(RemoteProject remoteProject : remoteProjectArray){
			if(remoteProject.getKey().equals(projectInfo.getProjectKey())
					|| remoteProject.getName().equals(projectInfo.getProjectName())){
				return true;
			}
		}
		return false;
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
			throw new UserPermissionNotEnoughException("该账号 '" + userName + "' 没有权限查询用户！");
		} catch (RemoteAuthenticationException e1) {
			throw new RuntimeException("the token is invalid or the SOAP session has timed out,please login again.",e1);
		} catch (java.rmi.RemoteException e1) {
			throw new RuntimeException(e1);
		}
		return remoteUser != null;
	}
	
	/**
	 * 检查角色是否存在
	 * @param roleInfo
	 * @return
	 */
	private boolean checkRoleExist(JiraRoleInfo roleInfo){
		List<String> roleNames = this.getAllRoleNamesFromJira(roleInfo);
		if(roleNames.isEmpty()){
			return false;
		}
		
		boolean roleExist = false;
		for(String roleName : roleNames){
			if(roleName.equals(roleInfo.getRoleName())){
				roleExist = true;
				break;
			}
		}
		return roleExist;
	}
	
	private void createProject(JiraProjectInfo projectInfo){
		try {
			jiraService.createProject(token, projectInfo.getProjectKey(), projectInfo.getProjectName(),
					projectInfo.getDesc(), projectInfo.getServerAddress(), projectInfo.getProjectLead(),
					jiraService.getPermissionSchemes(token)[0],null,null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	private void createUser(JiraUserInfo userInfo){
		try {
			jiraService.createUser(token, userInfo.getUserName(), 
					userInfo.getPassword(),userInfo.getFullName(), userInfo.getEmail());
		} catch (RemotePermissionException e) {
			throw new UserPermissionNotEnoughException("该账号 '" + userInfo.getUserName() + "' 没有权限创建用户！");
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

}
