package org.openkoala.opencis.jenkins;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import java.net.MalformedURLException;
import java.net.URL;

/**                               /createItem?name=
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:56:28 AM
 */
@Ignore
public class JenkinsCISClientTest {


    private static URL JENKINS_URL;

    private static URL CAS_URL;

    private JenkinsCISClient jenkinsCISClient;

    private static Project project = new Project();

    private static Developer developer = new Developer();


    static {
        try {
            JENKINS_URL = new URL("http", "localhost", 8080, "/jenkins");
            CAS_URL = new URL("http", "localhost", 8080, "/cas/v1/tickets/");
            project.setArtifactId("Artifactdddfee3323");
            developer.setName("www");
            developer.setEmail("admin@gmail.com");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        jenkinsCISClient = new JenkinsCISClient(JENKINS_URL);
        HttpCASAuthentication authentication = new HttpCASAuthentication(CAS_URL, "admin", "admin");
        authentication.setJenkinsAuthenticationUrl(JENKINS_URL);
        jenkinsCISClient.addAuthentication(authentication);
        //jenkinsCISClient.createProject(project);
    }

    @After
    public void tearDown() throws Exception {
        //jenkinsCISClient.confirmRemoveJob(project.getArtifactId());
    }



    @Test
    public void testOperationProject() throws MalformedURLException {
        Project project1 = new Project();
        project1.setArtifactId("Artifacadmin1tIsaaadfffggsdfs23");
        project1.setProjectName("projectNamesdfdsfsfs");
        jenkinsCISClient.createProject(project1);
        jenkinsCISClient.confirmRemoveJob(project1.getArtifactId());
    }


    @Test
    public void testCreateUserIfNecessary() {
        jenkinsCISClient.createUserIfNecessary(project, developer);
    }

    @Test
    public void testCreateRole() {
        jenkinsCISClient.createRoleIfNecessary(project, "roleExample");
    }


}
