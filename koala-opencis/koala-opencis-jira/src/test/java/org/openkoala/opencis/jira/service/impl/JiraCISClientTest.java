package org.openkoala.opencis.jira.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.*;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jira.service.JiraConfiguration;

@Ignore
public class JiraCISClientTest {

	private JiraConfiguration jiraConfiguration;
	private static JiraCISClient instance;
	private Project project;
	private Developer developer;
	
	/**管理员登陆信息**/
	private static InputStream inputFile = 
			new JiraCISClientTest().getClass().getResourceAsStream("/loginToJiraConfig.properties");
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
	private String roleName;
	
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
		jiraConfiguration = new JiraConfiguration(serverAddress, adminUserName, adminPassword);
		instance = new JiraCISClient(jiraConfiguration);
		this.initProjectInfo();
		this.initUserInfo();
		this.initRoleInfo();
	}

	@After
	public void tearDown() throws Exception {
		project = null;
		developer = null;
		roleName = null;
	}
	
	@Test(expected = ServerAddressBlankException.class)
	public void testServerAddressBlank(){
		this.packagingServerAddressBlank();
		jiraConfiguration.checkLoginInfoNotBlank();
	}
	
	@Test(expected = AdminUserNameBlankException.class)
	public void testAdminUserNameBlank(){
		this.packagingAdminUserNameBlank();
		jiraConfiguration.checkLoginInfoNotBlank();
	}
	
	@Test(expected = AdminPasswordBlankException.class)
	public void testAdminPasswordBlank(){
		this.packagingAdminPasswordBlank();
		jiraConfiguration.checkLoginInfoNotBlank();
	}
	
	@Test(expected = ProjectKeyBlankException.class)
	public void testProjectKeyBlank(){
		this.packagingProjectKeyBlank();
		instance.createProject(project);
	}
	
	@Test(expected = ProjectKeyNotAllCharacterLettersException.class)
	public void testProjectKeyNotAllCharacterLetters(){
		this.packagingProjectKeyNotAllUppercaseLetters();
		instance.createProject(project);
	}
	
	@Test(expected = ProjectKeyNotAllCharacterLettersException.class)
	public void testProjectKeyNotAllCharacterLetters2(){
		this.packagingProjectKeyNotAllUppercaseLetters2();
		instance.createProject(project);
	}
	
	@Test(expected = ProjectKeyLengthNotBetweenTwoAndTenCharacterLettersException.class)
	public void testProjectKeyNotAtLeastTwoCharacters(){
		this.packagingProjectKeyNotAtLeastTwoCharacters();
		instance.createProject(project);
	}
	
	@Test(expected = ProjectLeadBlankException.class)
	public void testProjectLeadBlank(){
		this.packagingProjectLeadBlank();
		instance.createProject(project);
	}
	
	@Test(expected = ServerAddressErrorException.class)
	public void testServerAddressError(){
		this.packagingServerAddressErrorWithoutProtocal();
		instance.loginToJira(jiraConfiguration);
	}
	
	@Test(expected = ServerAddressErrorException.class)
	public void testServerAddressErrorWithWrongIp(){
		this.packagingServerAddressErrorWithWrongIp();
		instance.loginToJira(jiraConfiguration);
	}
	
	@Test(expected = ServerAddressErrorException.class)
	public void testServerAddressErrorWithWrongPort(){
		this.packagingServerAddressErrorWithWrongPort();
		instance.loginToJira(jiraConfiguration);
	}
	
	@Test(expected = AdminUserNameOrPasswordErrorException.class)
	public void testAdminUserNameNotExist(){
		this.packagingAdminUserNameNotExist();
		instance.loginToJira(jiraConfiguration);
	}
	
	@Test(expected = AdminUserNameOrPasswordErrorException.class)
	public void testAdminPasswordNotCorrect(){
		this.packagingAdminPasswordNotCorrect();
		instance.loginToJira(jiraConfiguration);
	}
	
	@Test
	public void testProjectKeyExist(){
		this.packagingProjectCanBeCreatedWithoutDesc();
		instance.createUserIfNecessary(project, developer);
		instance.createProject(project);
		
		try {
			project.setProjectName(project.getProjectName() + "1");
			instance.createProject(project);
		} catch (ProjectExistException e) {
			assertTrue("预期抛出项目存在的异常！", true);
		}catch(Exception e){
			instance.removeProject(project);
			instance.removeUser(developer.getName());
			fail("测试 '预期抛出项目存在的异常' 失败！");
		}
		
		instance.removeProject(project);
		instance.removeUser(developer.getName());
	}
	
	@Test
	public void testProjectNameExist(){
		instance.createUserIfNecessary(project, developer);
		
		this.packagingProjectCanBeCreatedWithoutDesc();
		instance.createProject(project);
		
		String existProjectKey = project.getProjectKey();
		try{
			project.setProjectKey(project.getProjectKey() + "A");
			instance.createProject(project);
		}catch (ProjectExistException e) {
			assertTrue("预期抛出项目存在的异常！", true);
		}catch(Exception e){
			project.setProjectKey(existProjectKey);
			instance.removeProject(project);
			instance.removeUser(developer.getName());
			fail("测试'预期抛出项目存在的异常' 失败！");
		}

		project.setProjectKey(existProjectKey);
		instance.removeProject(project);
		instance.removeUser(developer.getName());
	}
	
	@Test(expected = UserNotExistException.class)
	public void testProjectLeadNotExist(){
		this.packagingProjectLeadNotExist();
		instance.createProject(project);
	}
	
	@Test
	public void testCreateProject() {
		instance.createUserIfNecessary(project, developer);
		
		this.packagingProjectCanBeCreatedWithoutDesc();
		instance.createProject(project);
		instance.removeProject(project);
		assertTrue("应该能成功创建没有提供描述信息的项目！", true);
		
		this.packagingProjectCanBeCreatedWithDesc();
		instance.createProject(project);
		instance.removeProject(project);

		instance.removeUser(developer.getName());
		
		assertTrue("应该能成功创建有提供描述信息的项目！", true);
	}
	
	@Test(expected = UserNameBlankException.class)
	public void testUserNameBlank(){
		this.packagingUserNameBlank();
		instance.createUserIfNecessary(project, developer);
	}
	
	@Test(expected = UserFullNameBlankException.class)
	public void testFullNameBlank(){
		this.packagingFullNameBlank();
		instance.createUserIfNecessary(project, developer);
	}
	
	@Test(expected = UserEmailBlankException.class)
	public void testEmailBlank(){
		this.packagingEmailBlank();
		instance.createUserIfNecessary(project, developer);
	}
	
	@Test
	public void testCreateUserIfNecessary() {
		this.packagingUserInfoNotExistWithoutPwd();
		instance.createUserIfNecessary(project, developer);
		instance.removeUser(developer.getName());
		assertTrue("应该能成功创建没有提供密码的用户！", true);
		
		this.packagingUserInfoNotExistWithPwd();
		instance.createUserIfNecessary(project, developer);
		instance.removeUser(developer.getName());
		assertTrue("应该能成功创建提供密码的用户！", true);
	}
	
	@Test(expected = RoleNameBlankException.class)
	public void testRoleNameBlank(){
		this.packagingRoleNameBlank();
		instance.createRoleIfNecessary(project, roleName);
	}

	@Test
	public void testCreateRoleIfNessceary() {
		instance.createRoleIfNecessary(project, roleName);
		instance.removeRole(roleName);
		assertTrue("创建角色成功！", true);
	}
	
	@Test
	public void testAddProjectRoleToUserButProjectNotExist(){
		instance.createUserIfNecessary(project, developer);
		instance.createRoleIfNecessary(project, roleName);
		
		try {
			instance.assignUserToRole(project, developer.getName(), roleName);
		} catch (ProjectNotExistException e) {
			assertTrue("预期项目不存在！",true);
		} catch(Exception e){
			instance.removeUser(developer.getName());
			instance.removeRole(roleName);
			fail("测试 '预期项目不存在' 失败！");
		}

		instance.removeUser(developer.getName());
		instance.removeRole(roleName);
	}
	
	@Test
	public void testAddProjectRoleToUserButRoleNotExist(){
		instance.createUserIfNecessary(project, developer);
		instance.createProject(project);
		
		try {
			instance.assignUserToRole(project, developer.getName(), roleName);
		} catch (RoleNotExistException e) {
			assertTrue("预期角色不存在！",true);
		}catch(Exception e){
			instance.removeProject(project);
			instance.removeUser(developer.getName());
			fail("测试 '预期角色不存在' 失败！");
		}

		instance.removeProject(project);
		instance.removeUser(developer.getName());
	}
	
	@Test
	public void testAddProjectRoleToUserButUserNotExist(){
		project.setProjectLead(jiraConfiguration.getAdminUserName());
		instance.createProject(project);
		instance.createRoleIfNecessary(project, roleName);
		
		try {
			instance.assignUserToRole(project, developer.getName(), roleName);
		} catch (UserNotExistException e) {
			assertTrue("预期用户不存在！",true);
		}catch(Exception e){
			instance.removeProject(project);
			instance.removeRole(roleName);
			fail("测试 '预期用户不存在' 失败！");
		}

		instance.removeProject(project);
		instance.removeRole(roleName);
	}

	@Test
	public void testAssignUserToRole() {
		instance.createUserIfNecessary(project, developer);
		instance.createProject(project);
		instance.createRoleIfNecessary(project, roleName);

		instance.assignUserToRole(project, developer.getName(), roleName);

		instance.removeProject(project);
		instance.removeUser(developer.getName());
		instance.removeRole(roleName);
		assertTrue("为用户分配角色成功！", true);
	}


	/**
	 * 确保必填项不为空
	 */
	private void initProjectInfo(){
		project = new Project();
		project.setProjectKey(projectKey);
		project.setProjectName(projectName);
		project.setProjectLead(projectLead);
		project.setDescription(desc);
	}
	
	/**
	 * 确保必填项不为空
	 */
	private void initUserInfo(){
		developer = new Developer();
		developer.setName(userName);
		developer.setPassword(password);
		developer.setFullName(fullName);
		developer.setEmail(email);
	}
	
	private void initRoleInfo(){
		roleName = "roleName";
	}
	
	private void packagingServerAddressBlank(){
		jiraConfiguration.setServerAddress(null);
	}
	
	private void packagingAdminUserNameBlank(){
		jiraConfiguration.setAdminUserName(null);
	}
	
	private void packagingAdminPasswordBlank(){
		jiraConfiguration.setAdminPassword(null);
	}
	
	private void packagingProjectKeyBlank(){
		project.setProjectKey(null);
	}
	
	private void packagingProjectKeyNotAllUppercaseLetters(){
		project.setProjectKey("A123");
	}
	
	private void packagingProjectKeyNotAllUppercaseLetters2(){
		project.setProjectKey("A_BC");
	}
	
	private void packagingProjectKeyNotAtLeastTwoCharacters(){
		project.setProjectKey("A");
	}
	
	private void packagingProjectLeadBlank(){
		project.setProjectLead(null);
	}
	
	private void packagingServerAddressErrorWithoutProtocal(){
		jiraConfiguration.setServerAddress("aaaaaa");
		instance.setJiraService(null);
	}
	
	private void packagingServerAddressErrorWithWrongIp(){
		jiraConfiguration.setServerAddress("http://localhosta:8080");
		instance.setJiraService(null);
	}
	
	private void packagingServerAddressErrorWithWrongPort(){
		jiraConfiguration.setServerAddress("http://localhost:80805");
		instance.setJiraService(null);
	}
	
	private void packagingAdminUserNameNotExist(){
		jiraConfiguration.setAdminUserName("errorName");
		instance.setJiraService(null);
	}
	
	private void packagingAdminPasswordNotCorrect(){
		jiraConfiguration.setAdminPassword("errorPassword");
		instance.setJiraService(null);
	}
	
	private void packagingProjectCanBeCreatedWithoutDesc(){
		project.setDescription(null);
	}
	
	private void packagingProjectLeadNotExist(){
		project.setProjectLead("errorName");
	}
	
	private void packagingProjectCanBeCreatedWithDesc(){
		project.setDescription(desc);
	}
	
	private void packagingUserNameBlank(){
		developer.setName(null);
	}
	
	private void packagingFullNameBlank(){
		developer.setFullName(null);
	}
	
	private void packagingEmailBlank(){
		developer.setEmail(null);
	}
	
	private void packagingUserInfoNotExistWithoutPwd(){
		developer.setName("userName");
		developer.setPassword(null);
	}
	
	private void packagingUserInfoNotExistWithPwd(){
		developer.setName("userName");
	}
	
	private void packagingRoleNameBlank(){
		roleName = null;
	}
	
}
