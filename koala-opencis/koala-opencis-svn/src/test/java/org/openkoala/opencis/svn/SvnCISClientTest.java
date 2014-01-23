package org.openkoala.opencis.svn;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.exception.HostBlankException;
import org.openkoala.opencis.exception.HostCannotConnectException;
import org.openkoala.opencis.exception.PasswordBlankException;
import org.openkoala.opencis.exception.ProjectBlankException;
import org.openkoala.opencis.exception.ProjectExistenceException;
import org.openkoala.opencis.exception.RoleBlankException;
import org.openkoala.opencis.exception.UserBlankException;
import org.openkoala.opencis.exception.UserListBlankException;
import org.openkoala.opencis.exception.UserOrPasswordErrorException;
import org.openkoala.opencis.support.SSHConnectConfig;

@Ignore
public class SvnCISClientTest {

    private SSHConnectConfig configuration;
    private SvnCISClient instance;
    private Project project;
    private Developer developer;
    private String projectName = "projectUnitTest";
    private List<String> userNames;
    private String roleName;

    @Before
    public void setUp() throws Exception {
        configuration = new SSHConnectConfig("10.108.1.106", "root", "password","/opencis/svn/");
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
    public void testHostBlank() {
        configuration.setHost(null);
        instance = new SvnCISClient(configuration);
    }

    @Test(expected = UserBlankException.class)
    public void testUserBlank() {
        configuration.setUsername(null);
        instance = new SvnCISClient(configuration);
    }

    @Test(expected = PasswordBlankException.class)
    public void testPasswordBlank() {
        configuration.setPassword(null);
        instance = new SvnCISClient(configuration);
    }

    @Test(expected = HostCannotConnectException.class)
    public void testHostCannotConnect() {
        configuration.setHost("aaa");
        instance = new SvnCISClient(configuration);
        instance.canConnect();
    }

    @Test(expected = UserOrPasswordErrorException.class)
    public void testUserError() {
        configuration.setUsername("xxxx");
        instance = new SvnCISClient(configuration);
        instance.canConnect();
    }

    @Test(expected = UserOrPasswordErrorException.class)
    public void testPasswordError() {
        configuration.setPassword("wwwww");
        instance = new SvnCISClient(configuration);
        instance.canConnect();
    }

    @Test(expected = ProjectBlankException.class)
    public void testProjectInfoBlank() {
        project.setProjectName(null);
        instance = new SvnCISClient(configuration);
        instance.createProject(project);
    }

    @Test
    public void testProjectExistence() {
        instance = new SvnCISClient(configuration);
        instance.createProject(project);
        try {
            instance.createProject(project);
        } catch (ProjectExistenceException e) {
            assertTrue("预期抛出项目存在的异常！", true);
        } finally {
//            instance.removeProject(project);
        }
    }
    
    @Test
    public void testRemoveProject(){
    	instance = new SvnCISClient(configuration);
    	try {
    		instance.removeProject(project);
        	assertTrue("成功删除项目",true);
		} catch (Exception e) {
			// TODO: handle exception
			fail("删除项目失败");
		}
    	
    }
    
    @Test
    public void testRemoveUser(){
    	instance = new SvnCISClient(configuration);
    	try {
			instance.removeUser(project, developer);
			assertTrue("成功删除用户" + developer.getId(), true);
		} catch (Exception e) {
			// TODO: handle exception
			fail("删除用户失败");
		}
    }

    @Test(expected = UserBlankException.class)
    public void testProjectUserBlank() {
        developer.setName(null);
        instance = new SvnCISClient(configuration);
        instance.createUserIfNecessary(project, developer);
    }

    @Test(expected = PasswordBlankException.class)
    public void testProjectUserPassowrdBlank() {
        developer.setPassword(null);
        instance = new SvnCISClient(configuration);
        instance.createUserIfNecessary(project, developer);
    }

    @Test
    public void testCreateUserIfNecessary() {
        instance = new SvnCISClient(configuration);
        //要先创建项目，再为项目创建用户
//        instance.createProject(project);
        try {
            instance.createUserIfNecessary(project, developer);
            assertTrue(true);
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
//            instance.removeProject(project);
        }
    }

    @Test(expected = UserListBlankException.class)
    public void testUserListBlank() {
        instance = new SvnCISClient(configuration);
        instance.assignUsersToRole(project, "Architect", null);
    }

    @Test(expected = UserListBlankException.class)
    public void testUserListBlank2() {
        instance = new SvnCISClient(configuration);
        instance.assignUsersToRole(project, "Architect");
    }

    @Test(expected = RoleBlankException.class)
    public void testRoleBlank() {
        initUserListAndRoleName();
        instance = new SvnCISClient(configuration);
        instance.assignUsersToRole(project, null,developer);
    }

    @Test
    public void testAssignUsersToRole() {
        try {
            initUserListAndRoleName();
            instance = new SvnCISClient(configuration);
//            instance.createProject(project);
            instance.assignUsersToRole(project, roleName,developer);
//            instance.removeProject(project);
            assertTrue(true);
        } catch (Exception e) {
            fail("授权失败！");
        }
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
    private void initProjectInfo() {
        project = new Project();
        project.setProjectName(projectName);
        project.setPhysicalPath("/usr/local/subversion/");
    }

    private void initDeveloperInfo() {
        developer = new Developer();
        developer.setId("zjh2");
        developer.setName("projectUserTest");
        developer.setPassword("pwdTest");
    }

    private void initUserListAndRoleName() {
        userNames = new ArrayList<String>();
        userNames.add("zjh");
        userNames.add("bbb");

        roleName = "Architect";
    }

//    public static void main(String[] args) {
//		SvnCISClientTest test = new SvnCISClientTest();
//		try {
//			test.setUp();
////			test.testProjectExistence();
//			test.testCreateUserIfNecessary();
//			test.testRemoveUser();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
