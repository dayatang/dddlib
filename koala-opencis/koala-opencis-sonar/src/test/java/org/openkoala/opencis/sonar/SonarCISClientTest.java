package org.openkoala.opencis.sonar;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.PropertyIllegalException;
import org.openkoala.opencis.SonarCISClientHelper;
import org.openkoala.opencis.SonarServiceNotExistException;
import org.openkoala.opencis.SonarUserExistException;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.pojo.SonarServerConfiguration;
import org.sonar.wsclient.user.User;

@Ignore
public class SonarCISClientTest {

	public static final String NAME = "koala";
	public static final String SONAR_SERVER_ADDRESS = "http://localhost:8888";
	public static final String SONAR_SERVER_ADDRESS2 = "http://10.108.1.138:9001";
	public static final String SONAR_ADMIN_USERNAME = "admin";
	public static final String SONAR_ADMIN_PASSWORD = "admin";
	public static final String PROJECT_ARTIFACTID = "koala-cas-management";
	public static final String PROJECT_GROUPID = "org.openkoala.cas";
	
	
	private SonarCISClientHelper cisClient;
	private Developer developer;
	
	@Test
	public void testCanConnect() {
		assertTrue(cisClient.canConnect());
	}
	
	@Test(expected = SonarServiceNotExistException.class)
	public void testCanConnectIfServiceNotExists() {
		cisClient.setSonarServerConfiguration(new SonarServerConfiguration(SONAR_SERVER_ADDRESS2, SONAR_ADMIN_USERNAME, SONAR_ADMIN_PASSWORD));
		cisClient.canConnect();
	}
	
	@Test
	public void testCreateUser() {
		cisClient.createUserIfNecessary(null, developer);
		User user = cisClient.findUserByName(developer.getId());
		assertNotNull(user);
		assertEquals(developer.getName(), user.name());
		cisClient.removeUserIfNecessary(developer);
	}
	
	@Test(expected = SonarUserExistException.class)
	public void testCreateUserIfExists() {
		developer.setId("admin");
		cisClient.createUserIfNecessary(null, developer);
	}
	
	@Test(expected = PropertyIllegalException.class)
	public void testCreateUserIfNull() {
		cisClient.createUserIfNecessary(null, null);
	}

	@Test(expected = PropertyIllegalException.class)
	public void testCreateUserIfIllegal() {
		developer.setId("aa");
		cisClient.createUserIfNecessary(null, developer);
	}
	
	@Test(expected = PropertyIllegalException.class)
	public void testCreateUserIfIllegal2() {
		developer.setName("");
		cisClient.createUserIfNecessary(null, developer);
	}
	
	@Test
	public void testAssignUserToRole() {
		cisClient.createUserIfNecessary(null, developer);
		Project project = createProject();
		cisClient.assignUserToRole(project, NAME, null);
		cisClient.removeUserPermission(project, NAME);
		cisClient.removeUserIfNecessary(developer);
	}
	
	@Test(expected = PropertyIllegalException.class)
	public void testAssignUserToRoleIfProjectIsNull() {
		cisClient.assignUserToRole(null, NAME, null);
	}
	
	@Test(expected = PropertyIllegalException.class)
	public void testAssignUserToRoleIfUserIdIsNull() {
		cisClient.assignUserToRole(createProject(), null, null);
	}
	
	@Before
	public void init() {
		cisClient = new SonarCISClientHelper(new SonarServerConfiguration(SONAR_SERVER_ADDRESS, SONAR_ADMIN_USERNAME, SONAR_ADMIN_PASSWORD));
		developer = createDeveloper();
	}
	
	private Developer createDeveloper() {
		Developer developer = new Developer();
		developer.setId(NAME);
		developer.setName(NAME);
		developer.setEmail(NAME + "@" + NAME + ".com");
		return developer;
	}
	
	private Project createProject() {
		Project project = new Project();
		project.setArtifactId(PROJECT_ARTIFACTID);
		project.setGroupId(PROJECT_GROUPID);
		return project;
	}
	
	@After
	public void destory() {
		cisClient = null;
		developer = null;
	}
}
