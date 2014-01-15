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
import org.openkoala.opencis.api.SSHConnectConfig;
import org.openkoala.opencis.exception.HostBlankException;
import org.openkoala.opencis.exception.HostCannotConnectException;
import org.openkoala.opencis.exception.PasswordBlankException;
import org.openkoala.opencis.exception.ProjectBlankException;
import org.openkoala.opencis.exception.ProjectExistenceException;
import org.openkoala.opencis.exception.RoleBlankException;
import org.openkoala.opencis.exception.UserBlankException;
import org.openkoala.opencis.exception.UserListBlankException;
import org.openkoala.opencis.exception.UserOrPasswordErrorException;


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
        configuration = new SSHConnectConfig("10.108.1.131", "root", "openkoala");
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
            instance.removeProjcet(project);
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
        instance.createProject(project);
        try {
            instance.createUserIfNecessary(project, developer);
            assertTrue(true);
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            instance.removeProjcet(project);
        }
    }

    @Test(expected = UserListBlankException.class)
    public void testUserListBlank() {
        instance = new SvnCISClient(configuration);
        instance.assignUsersToRole(project, null, "Architect");
    }

    @Test(expected = UserListBlankException.class)
    public void testUserListBlank2() {
        instance = new SvnCISClient(configuration);
        instance.assignUsersToRole(project, new ArrayList<String>(), "Architect");
    }

    @Test(expected = RoleBlankException.class)
    public void testRoleBlank() {
        initUserListAndRoleName();
        instance = new SvnCISClient(configuration);
        instance.assignUsersToRole(project, userNames, null);
    }

    @Test
    public void testAssignUsersToRole() {
        try {
            initUserListAndRoleName();
            instance = new SvnCISClient(configuration);
            instance.createProject(project);
            instance.assignUsersToRole(project, userNames, roleName);
            instance.removeProjcet(project);
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
    }

    private void initDeveloperInfo() {
        developer = new Developer();
        developer.setName("projectUserTest");
        developer.setPassword("pwdTest");
    }

    private void initUserListAndRoleName() {
        userNames = new ArrayList<String>();
        userNames.add("aaa");
        userNames.add("bbb");

        roleName = "Architect";
    }

}
