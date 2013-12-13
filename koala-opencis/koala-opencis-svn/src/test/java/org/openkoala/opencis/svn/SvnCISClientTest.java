package org.openkoala.opencis.svn;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.opencis.api.Project;

import com.dayatang.configuration.Configuration;

public class SvnCISClientTest {
	
	private Configuration configuration;
	private SvnCISClient instance;
	private Project project;
	
	private String projectName = "projectUnitTest";

	@Before
	public void setUp() throws Exception {
		configuration = new MockConfiguration();
		this.initProjectInfo();
	}

	@After
	public void tearDown() throws Exception {
		configuration = null;
		instance = null;
		project = null;
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
	
	@Test
	public void testProjectExistence(){
		instance = new SvnCISClient(configuration);
		instance.createProject(project);
		try {
			instance.createProject(project);
		} catch (ProjectExistenceException e) {
			assertTrue("预期抛出项目存在的异常！", true);
		}
		instance.removeProjcet(project);
	}

	@Test
	public void testCreateProject() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateUserIfNecessary() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateRoleIfNessceary() {
		fail("Not yet implemented");
	}

	@Test
	public void testAssignUserToRole() {
		fail("Not yet implemented");
	}

	@Test
	public void testCanConnect() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsSuccess() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetSuccess() {
		fail("Not yet implemented");
	}
	
	/**
	 * 确保必填项不为空
	 */
	private void initProjectInfo(){
		project = new Project();
		project.setProjectName(projectName);
	}

}
