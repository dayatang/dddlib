package org.openkoala.opencis.sonar;

import com.github.dreamhead.moco.Moco;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runnable;
import org.openkoala.opencis.api.Project;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


/**
 * User: zjzhai
 * Date: 1/21/14
 * Time: 9:35 AM
 */
public class SonarCISClientTest {

    public static final String address2 = "http://localhost:12306";
    public static final String username = "admin";
    public static final String password = "admin";

    private HttpServer server = httpserver(12306);

    private SonarCISClient sonarCISClient;

    @Before
    public void setUp() throws Exception {
        server.get(by(uri("/api/authentication/validate"))).response(with("{\"validate\":true}"), status(200));
        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                sonarCISClient = new SonarCISClient(new SonarConnectConfig(address2, username, password));
                assert sonarCISClient.authenticate();
            }
        });

    }

    @Test
    public void test(){}

    @Ignore
    @Test
    public void testCreateProject() throws Exception {
        server.post(and(by(uri("/api/projects/create")),
                eq(query("key"), SonarCISClient.getKeyOf(getProject())),
                eq(query("name"), getProject().getProjectName())))
                .response(with("{\n" +
                        "    \"id\":\"12\",\n" +
                        "    \"k\":\"" + SonarCISClient.getKeyOf(getProject()) + "\",\n" +
                        "    \"nm\":\"yyy\",\n" +
                        "    \"sc\":\"PRJ\",\n" +
                        "    \"qu\":\"TRK\"\n" +
                        "}"), status(200));

        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                sonarCISClient.createProject(getProject());
            }
        });

    }

    private Project getProject() {
        Project project = new Project();
        project.setProjectLead("lead");
        project.setArtifactId("artifact");
        project.setDescription("xxx");
        project.setGroupId("group");
        project.setProjectName("yyy");
        project.setPhysicalPath("physical");
        return project;
    }

}
