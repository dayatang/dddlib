package org.openkoala.opencis.sonar;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import java.util.UUID;

@Ignore
public class SonarCISClientIntegrationTest {

    public static final String address2 = "http://10.108.1.138:9000";
    public static final String username = "admin";
    public static final String password = "admin";


    @Test
    public void testAuthenticate() {
        SonarCISClient sonarCISClient = new SonarCISClient(new SonarConnectConfig(address2, username, password));
        assert sonarCISClient.authenticate();

        Project project = new Project();
        project.setProjectName("projdrr");
        project.setArtifactId("proffr");
        project.setGroupId("com.grou3");
        project.setDescription("description");
        project.setProjectLead("xxx");

        Developer developer = new Developer();
        developer.setName(UUID.randomUUID().toString());
        developer.setEmail("xxx@dxx.com");
        developer.setPassword("xxxx1111");
        developer.setFullName("fullname");


        sonarCISClient.createProject(project);

        sonarCISClient.createUserIfNecessary(project, developer);

        assert sonarCISClient.existsUser(developer.getName());


        sonarCISClient.assignUsersToRole(project, "", developer);

        SonarCISClient developerClient = new SonarCISClient(new SonarConnectConfig(address2, developer.getName(), developer.getPassword()));
        assert developerClient.authenticate();
        developerClient.close();


        sonarCISClient.removeUser(project, developer);

        assert !sonarCISClient.isActivated(developer.getName());

        sonarCISClient.removeProject(project);

        sonarCISClient.close();


        SonarCISClient developerClient1 = new SonarCISClient(new SonarConnectConfig(address2, developer.getName(), developer.getPassword()));
        assert !developerClient1.authenticate();
        developerClient.close();


    }


}
