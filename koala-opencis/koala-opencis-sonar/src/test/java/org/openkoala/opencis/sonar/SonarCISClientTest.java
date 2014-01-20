package org.openkoala.opencis.sonar;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.SonarCISClientHelper;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.sonar.wsclient.user.User;
@Ignore
public class SonarCISClientTest {

    public static final String NAME = "koala";
    public static final String address = "http://localhost:8888";
    public static final String address2 = "http://10.108.1.138:9000";
    public static final String username = "admin";
    public static final String password = "admin";
    public static final String PROJECT_ARTIFACTID = "koala-cas-management";
    public static final String PROJECT_GROUPID = "org.openkoala.cas";


    private SonarCISClientHelper cisClient;
    private Developer developer;


    @Test
    public void testAuthenticate() {
        SonarCISClient sonarCISClient = new SonarCISClient(new SonarConnectConfig(address2, username, password));
        sonarCISClient.authenticate();

        Project project = new Project();
        project.setProjectName("projectName");
        project.setArtifactId("project");
        project.setGroupId("com.group");
        project.setDescription("description");

        sonarCISClient.createProject(project);

    }


    @Test
    public void testCreateUser() {
        cisClient.createUserIfNecessary(null, developer);
        User user = cisClient.findUserByName(developer.getId());
        assertNotNull(user);
        assertEquals(developer.getName(), user.name());
        cisClient.removeUserIfNecessary(developer);
    }

    @Test
    public void testAssignUserToRole() {
        cisClient.createUserIfNecessary(null, developer);
        Project project = createProject();
        cisClient.removeUserPermission(project, NAME);
        cisClient.removeUserIfNecessary(developer);
    }


    @Before
    public void init() {
        cisClient = new SonarCISClientHelper(new SonarConnectConfig(address, username, password));
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
