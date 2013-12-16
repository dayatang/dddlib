package org.openkoala.opencis.svn;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import com.dayatang.configuration.Configuration;

public class SvnCISClientTest {
	
	private Configuration configuration;
	private SvnCISClient instance;
	private Project project;
	private Developer developer;
	private String projectName = "projectUnitTest";

	@Before
	public void setUp() throws Exception {
		configuration = new MockConfiguration();
		this.initProjectInfo();
		this.initDeveloperInfo();
	}

	@After
	public void tearDown() throws Exception {
		configuration = null;
		instance = null;
		project = null;
		developer = null;
	}
	
	@Test(expected = HostBlankException.class)
	public void testHostBlank(){
		configuration.setString("HOST", null);
		instance = new SvnCISClient(configuration);
		instance.isConfigurationCorrect();
	}
	
	@Test(expected = UserBlankException.class)
	public void testUserBlank(){
		configuration.setString("USER", null);
		instance = new SvnCISClient(configuration);
		instance.isConfigurationCorrect();
	}
	
	@Test(expected = PasswordBlankException.class)
	public void testPasswordBlank(){
		configuration.setString("PASSWORD", null);
		instance = new SvnCISClient(configuration);
		instance.isConfigurationCorrect();
	}
	
	@Test(expected = HostCannotConnectException.class)
	public void testHostCannotConnect(){
		configuration.setString("HOST", "aaaaaaaa");
		instance = new SvnCISClient(configuration);
		instance.isConfigurationCorrect();
	}
	
	@Test(expected = UserOrPasswordErrorException.class)
	public void testUserError(){
		configuration.setString("USER", "usererror");
		instance = new SvnCISClient(configuration);
		instance.isConfigurationCorrect();
	}
	
	@Test(expected = UserOrPasswordErrorException.class)
	public void testPasswordError(){
		configuration.setString("PASSWORD", "pwderror");
		instance = new SvnCISClient(configuration);
		instance.isConfigurationCorrect();
	}
	
	@Test(expected = ProjectBlankException.class)
	public void testProjectInfoBlank(){
		project.setProjectName(null);
		instance = new SvnCISClient(configuration);
		instance.createProject(project);
	}
	
	@Test
	public void testProjectExistence(){
		instance = new SvnCISClient(configuration);
		instance.createProject(project);
		try {
			instance.createProject(project);
		} catch (ProjectExistenceException e) {
			assertTrue("预期抛出项目存在的异常！", true);
		}finally{
			instance.removeProjcet(project);
		}
	}
	
	@Test(expected = UserBlankException.class)
	public void testProjectUserBlank(){
		developer.setName(null);
		instance = new SvnCISClient(configuration);
		instance.createUserIfNecessary(project, developer);
	}
	
	@Test(expected = PasswordBlankException.class)
	public void testProjectUserPassowrdBlank(){
		developer.setPassword(null);
		instance = new SvnCISClient(configuration);
		instance.createUserIfNecessary(project, developer);
	}

	@Test
	public void testCreateUserIfNecessary() {
		instance = new SvnCISClient(configuration);
		//要先创建项目，再为项目创建用户
		instance.createProject(project);
		try {
			instance.createUserIfNecessary(project, developer);
			assertTrue(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}finally{
			instance.removeProjcet(project);
		}
	}

	@Test
	@Ignore
	public void testAssignUserToRole() {
		
	}

	@Test
	public void testCanConnect() {
		instance = new SvnCISClient(configuration);
		boolean result = instance.canConnect();
		assertTrue(result);
	}

	/**
	 * 确保必填项不为空
	 */
	private void initProjectInfo(){
		project = new Project();
		project.setProjectName(projectName);
	}
	
	private void initDeveloperInfo(){
		developer = new Developer();
		developer.setName("projectUserTest");
		developer.setPassword("pwdTest");
	}

}
