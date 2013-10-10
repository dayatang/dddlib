package org.openkoala.opencis.jira.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openkoala.opencis.jira.service.JiraProjectInfo;
import org.openkoala.opencis.jira.service.JiraRoleInfo;
import org.openkoala.opencis.jira.service.JiraUserInfo;

/**
 * 如果不能访问jira服务端，则不测试，抛出警告
 * @author lambo
 *
 */
public class JiraOperateServiceImplTest {

	private JiraOperateServiceImpl jiraOperateService = new JiraOperateServiceImpl();
	private JiraProjectInfo projectInfo;
	private JiraUserInfo userInfo;
	private JiraRoleInfo roleInfo;
	
	/**管理员登陆信息**/
	private static InputStream inputFile = 
			new JiraOperateServiceImplTest().getClass().getResourceAsStream("/loginToJiraConfig.properties");
	private static String serverAddress;// = "http://localhost:8080"
	private static String adminUserName;// = "lishibin"
	private static String adminPassword;// = "87809237"
	/**针对创建项目**/
	private String projectKey = "KEYTESt";
	private String projectName = "projectUnitTest";
	private String projectLead = "laozhang";
	private String desc = "单元测试生成的项目";
	/**针对创建用户**/
	private String userName = "laozhang";
	private String password = "111111";
	private String fullName = "zhangsan";
	private String email = "test@gmail.com";
	/**针对创建角色**/
	private String roleName = "roleName";
	private String typeDesc = "角色描述";
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException{
		Properties p = new Properties();
		p.load(inputFile);
		serverAddress = p.getProperty("serverAddress");
		adminUserName = p.getProperty("adminUserName");
		adminPassword = p.getProperty("adminPassword");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException{
		inputFile.close();
	}

	@Before
	public void setUp() throws Exception {
		this.initProjectInfo();
		this.initUserInfo();
		this.initRoleInfo();
	}

	@After
	public void tearDown() throws Exception {
		projectInfo = null;
		userInfo = null;
		roleInfo = null;
	}

	@Test(expected = ServerAddressBlankException.class)
	public void serverAddressBlank(){
		this.packagingServerAddressBlank();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test(expected = AdminUserNameBlankException.class)
	public void adminUserNameBlank(){
		this.packagingAdminUserNameBlank();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test(expected = AdminPasswordBlankException.class)
	public void adminPasswordBlank(){
		this.packagingAdminPasswordBlank();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test(expected = ProjectKeyBlankException.class)
	public void projectKeyBlank(){
		this.packagingProjectKeyBlank();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test(expected = ProjectKeyNotAllCharacterLettersException.class)
	public void projectKeyNotAllCharacterLetters(){
		this.packagingProjectKeyNotAllUppercaseLetters();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test(expected = ProjectKeyNotAllCharacterLettersException.class)
	public void projectKeyNotAllCharacterLetters2(){
		this.packagingProjectKeyNotAllUppercaseLetters2();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test(expected = ProjectKeyLengthNotBetweenTwoAndTenCharacterLettersException.class)
	public void projectKeyNotAtLeastTwoCharacters(){
		this.packagingProjectKeyNotAtLeastTwoCharacters();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test(expected = ProjectLeadBlankException.class)
	public void projectLeadBlank(){
		this.packagingProjectLeadBlank();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test(expected = ServerAddressErrorException.class)
	public void serverAddressError(){
		this.packagingServerAddressErrorWithoutProtocal();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test(expected = ServerAddressErrorException.class)
	public void serverAddressErrorWithWrongIp(){
		this.packagingServerAddressErrorWithWrongIp();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test(expected = ServerAddressErrorException.class)
	public void serverAddressErrorWithWrongPort(){
		this.packagingServerAddressErrorWithWrongPort();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test(expected = AdminUserNameOrPasswordErrorException.class)
	public void adminUserNameNotExist(){
		this.packagingAdminUserNameNotExist();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test(expected = AdminUserNameOrPasswordErrorException.class)
	public void adminPasswordNotCorrect(){
		this.packagingAdminPasswordNotCorrect();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test
	public void projectKeyExist(){
		this.packagingProjectCanBeCreatedWithoutDesc();
		jiraOperateService.createUserToJiraIfNecessary(userInfo);
		jiraOperateService.createProjectToJira(projectInfo);
		
		try {
			projectInfo.setProjectName(projectInfo.getProjectName() + "1");
			jiraOperateService.createProjectToJira(projectInfo);
		} catch (ProjectExistException e) {
			assertTrue("预期抛出项目存在的异常！", true);
		}catch(Exception e){
			jiraOperateService.removeProject(projectInfo);
			jiraOperateService.removeUser(userInfo);
			fail("测试 '预期抛出项目存在的异常' 失败！");
		}
		
		jiraOperateService.removeProject(projectInfo);
		jiraOperateService.removeUser(userInfo);
	}
	
	@Test
	public void projectNameExist(){
		jiraOperateService.createUserToJiraIfNecessary(userInfo);
		
		this.packagingProjectCanBeCreatedWithoutDesc();
		jiraOperateService.createProjectToJira(projectInfo);
		
		String existProjectKey = projectInfo.getProjectKey();
		try{
			projectInfo.setProjectKey(projectInfo.getProjectKey() + "A");
			jiraOperateService.createProjectToJira(projectInfo);
		}catch (ProjectExistException e) {
			assertTrue("预期抛出项目存在的异常！", true);
		}catch(Exception e){
			projectInfo.setProjectKey(existProjectKey);
			jiraOperateService.removeProject(projectInfo);
			jiraOperateService.removeUser(userInfo);
			fail("测试'预期抛出项目存在的异常' 失败！");
		}

		projectInfo.setProjectKey(existProjectKey);
		jiraOperateService.removeProject(projectInfo);
		jiraOperateService.removeUser(userInfo);
	}
	
	@Test(expected = UserNotExistException.class)
	public void projectLeadNotExist(){
		this.packagingProjectLeadNotExist();
		jiraOperateService.createProjectToJira(projectInfo);
	}
	
	@Test
	public void testcreateProjectToJira() {
		jiraOperateService.createUserToJiraIfNecessary(userInfo);
		
		this.packagingProjectCanBeCreatedWithoutDesc();
		jiraOperateService.createProjectToJira(projectInfo);
		jiraOperateService.removeProject(projectInfo);
		assertTrue("应该能成功创建没有提供描述信息的项目！", true);
		
		this.packagingProjectCanBeCreatedWithDesc();
		jiraOperateService.createProjectToJira(projectInfo);
		jiraOperateService.removeProject(projectInfo);
		
		jiraOperateService.removeUser(userInfo);
		
		assertTrue("应该能成功创建有提供描述信息的项目！", true);
	}
	
	@Test(expected = UserNameBlankException.class)
	public void userNameBlank(){
		this.packagingUserNameBlank();
		jiraOperateService.createUserToJiraIfNecessary(userInfo);
	}
	
	@Test(expected = UserFullNameBlankException.class)
	public void fullNameBlank(){
		this.packagingFullNameBlank();
		jiraOperateService.createUserToJiraIfNecessary(userInfo);
	}
	
	@Test(expected = UserEmailBlankException.class)
	public void emailBlank(){
		this.packagingEmailBlank();
		jiraOperateService.createUserToJiraIfNecessary(userInfo);
	}
	
	@Test(expected = UserExistException.class)
	public void checkUserExist() {
		this.packagingUserInfoExist();
		jiraOperateService.createUserToJiraIfNecessary(userInfo);
	}
	
	@Test
	public void testcreateUserToJiraIfNecessary() {
		this.packagingUserInfoNotExistWithoutPwd();
		jiraOperateService.createUserToJiraIfNecessary(userInfo);
		jiraOperateService.removeUser(userInfo);
		assertTrue("应该能成功创建没有提供密码的用户！", true);
		
		this.packagingUserInfoNotExistWithPwd();
		jiraOperateService.createUserToJiraIfNecessary(userInfo);
		jiraOperateService.removeUser(userInfo);
		assertTrue("应该能成功创建提供密码的用户！", true);
	}
	
	@Test
	public void testGetAllRoleNamesFromJira(){
		List<String> roleNames = jiraOperateService.getAllRoleNamesFromJira(projectInfo);
		assertTrue("从jira服务器上获取的角色数量应该大于0！", roleNames.size() > 0);
	}
	
	@Test(expected = RoleNameBlankException.class)
	public void roleNameBlank(){
		this.packagingRoleNameBlank();
		jiraOperateService.createRoleToJira(roleInfo);
	}
	
	@Test
	public void checkRoleExist(){
		jiraOperateService.createRoleToJira(roleInfo);
		
		try {
			jiraOperateService.createRoleToJira(roleInfo);
		} catch (RoleExistException e) {
			assertTrue("预期抛出角色存在异常！", true);
		}catch(Exception e){
			jiraOperateService.removeRole(roleInfo);
			fail("测试 '预期抛出角色存在异常' 失败！");
		}
		
		jiraOperateService.removeRole(roleInfo);
	}
	
	@Test
	public void testcreateRoleToJira() {
		jiraOperateService.createRoleToJira(roleInfo);
		jiraOperateService.removeRole(roleInfo);
		assertTrue("创建角色成功！", true);
	}
	
	@Test
	public void addProjectRoleToUserButProjectNotExist(){
		jiraOperateService.createUserToJiraIfNecessary(userInfo);
		jiraOperateService.createRoleToJira(roleInfo);
		
		try {
			jiraOperateService.addProjectRoleToUser(projectInfo, userInfo, roleInfo);
		} catch (ProjectNotExistException e) {
			assertTrue("预期项目不存在！",true);
		} catch(Exception e){
			jiraOperateService.removeUser(userInfo);
			jiraOperateService.removeRole(roleInfo);
			fail("测试 '预期项目不存在' 失败！");
		}
		
		jiraOperateService.removeUser(userInfo);
		jiraOperateService.removeRole(roleInfo);
	}
	
	@Test
	public void addProjectRoleToUserButRoleNotExist(){
		jiraOperateService.createUserToJiraIfNecessary(userInfo);
		jiraOperateService.createProjectToJira(projectInfo);
		
		try {
			jiraOperateService.addProjectRoleToUser(projectInfo, userInfo, roleInfo);
		} catch (RoleNotExistException e) {
			assertTrue("预期角色不存在！",true);
		}catch(Exception e){
			jiraOperateService.removeProject(projectInfo);
			jiraOperateService.removeUser(userInfo);
			fail("测试 '预期角色不存在' 失败！");
		}

		jiraOperateService.removeProject(projectInfo);
		jiraOperateService.removeUser(userInfo);
	}
	
	@Test
	public void addProjectRoleToUserButUserNotExist(){
		projectInfo.setProjectLead(projectInfo.getAdminUserName());
		jiraOperateService.createProjectToJira(projectInfo);
		jiraOperateService.createRoleToJira(roleInfo);
		
		try {
			jiraOperateService.addProjectRoleToUser(projectInfo, userInfo, roleInfo);
		} catch (UserNotExistException e) {
			assertTrue("预期用户不存在！",true);
		}catch(Exception e){
			jiraOperateService.removeProject(projectInfo);
			jiraOperateService.removeRole(roleInfo);
			fail("测试 '预期用户不存在' 失败！");
		}
		
		jiraOperateService.removeProject(projectInfo);
		jiraOperateService.removeRole(roleInfo);
	}
	
	@Test
	public void testAddProjectRoleToUser(){
		jiraOperateService.createUserToJiraIfNecessary(userInfo);
		jiraOperateService.createProjectToJira(projectInfo);
		jiraOperateService.createRoleToJira(roleInfo);
		
		jiraOperateService.addProjectRoleToUser(projectInfo, userInfo, roleInfo);

		jiraOperateService.removeProject(projectInfo);
		jiraOperateService.removeUser(userInfo);
		jiraOperateService.removeRole(roleInfo);
		assertTrue("为用户分配角色成功！", true);
	}
	
	private void packagingServerAddressBlank(){
		projectInfo.setServerAddress(null);
	}
	
	private void packagingServerAddressErrorWithoutProtocal(){
		projectInfo.setServerAddress("aaaaaa");
	}
	
	private void packagingServerAddressErrorWithWrongIp(){
		projectInfo.setServerAddress("http://localhosta:8080");
	}
	
	private void packagingServerAddressErrorWithWrongPort(){
		projectInfo.setServerAddress("http://localhost:80805");
	}
	
	private void packagingAdminUserNameBlank(){
		projectInfo.setAdminUserName(null);
	}
	
	private void packagingAdminUserNameNotExist(){
		projectInfo.setAdminUserName("errorName");
	}
	
	private void packagingAdminPasswordBlank(){
		projectInfo.setAdminPassword(null);
	}
	
	private void packagingAdminPasswordNotCorrect(){
		projectInfo.setAdminPassword("errorPassword");
	}
	
	private void packagingProjectKeyBlank(){
		projectInfo.setProjectKey(null);
	}
	
	private void packagingProjectKeyNotAllUppercaseLetters(){
		projectInfo.setProjectKey("A123");
	}
	
	private void packagingProjectKeyNotAllUppercaseLetters2(){
		projectInfo.setProjectKey("A_BC");
	}
	
	private void packagingProjectKeyNotAtLeastTwoCharacters(){
		projectInfo.setProjectKey("A");
	}
	
	private void packagingProjectLeadBlank(){
		projectInfo.setProjectLead(null);
	}
	
	private void packagingProjectLeadNotExist(){
		projectInfo.setProjectLead("errorName");
	}
	
	private void packagingProjectCanBeCreatedWithoutDesc(){
		projectInfo.setDesc(null);
	}
	
	private void packagingProjectCanBeCreatedWithDesc(){
		projectInfo.setDesc(desc);
	}
	
	private void packagingUserNameBlank(){
		userInfo.setUserName(null);
	}
	
	private void packagingFullNameBlank(){
		userInfo.setFullName(null);
	}
	
	private void packagingEmailBlank(){
		userInfo.setEmail(null);
	}
	
	private void packagingUserInfoExist(){
		userInfo.setUserName(adminUserName);
	}
	
	private void packagingUserInfoNotExistWithoutPwd(){
		userInfo.setUserName("userName");
		userInfo.setPassword(null);
	}
	
	private void packagingUserInfoNotExistWithPwd(){
		userInfo.setUserName("userName");
	}
	
	private void packagingRoleNameBlank(){
		roleInfo.setRoleName(null);
	}
	
	/**
	 * 确保必填项不为空
	 */
	private void initProjectInfo(){
		projectInfo = new JiraProjectInfo(serverAddress, adminUserName, adminPassword,
				projectKey, projectName, projectLead, desc);
	}
	
	/**
	 * 确保必填项不为空
	 */
	private void initUserInfo(){
		userInfo = new JiraUserInfo(serverAddress, adminUserName, adminPassword,
				userName, password, fullName, email);
	}
	
	/**
	 * 确保必填项不为空
	 */
	private void initRoleInfo(){
		roleInfo = new JiraRoleInfo(serverAddress, adminUserName, adminPassword,
				roleName, null, typeDesc);
	}

}
