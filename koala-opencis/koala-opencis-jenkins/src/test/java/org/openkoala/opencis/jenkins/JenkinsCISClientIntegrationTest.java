package org.openkoala.opencis.jenkins;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import java.net.MalformedURLException;
import java.util.UUID;



@Ignore
public class JenkinsCISClientIntegrationTest {


    public static String jenkinsURL = "http://127.0.0.1:8080/jenkins";
    public static String username = "admin";
    public static String apiToken = "3235dddf0084d7e70d4190b30d75aeb6";

    public static String svnUrl = "http://10.108.1.138/svn/projec";

    @Test
    public void test() throws MalformedURLException {

        Project project = getProject("hhhhh00");

        JenkinsCISClient client = new JenkinsCISClient(jenkinsURL, username, apiToken);

        assert client.authenticate();

        client.setKoalaScmConfig(new KoalaSvnConfig(svnUrl));

        client.createProject(project);


        client.createUserIfNecessary(project, getDeveloper("hhh00"));
        client.createUserIfNecessary(project, getDeveloper("hhh100"));
        client.createUserIfNecessary(project, getDeveloper("hhh200"));

        client.assignUsersToRole(project, "", getDeveloper("hhh100"),getDeveloper("hhh200"));
        //client.removeProject(project);
        client.close();


    }

    public Developer getDeveloper(String name) {
        Developer developer = new Developer();
        developer.setName(name);
        developer.setId(name);
        developer.setPassword("admin");
        developer.setEmail(UUID.randomUUID().toString() + "@gmail.com");
        return developer;
    }


    public Project getProject(String name) {
        Project project = new Project();
        project.setProjectName(name);
        project.setGroupId("com.c");
        project.setPhysicalPath("plpl");
        project.setDescription("descript");
        project.setArtifactId(name);
        return project;
    }


}
