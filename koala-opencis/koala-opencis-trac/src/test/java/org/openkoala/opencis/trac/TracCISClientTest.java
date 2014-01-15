package org.openkoala.opencis.trac;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Project;

import org.openkoala.opencis.api.SSHConnectConfig;

@Ignore
public class TracCISClientTest {

    private Project project = new MockProject();

    private TracCISClient client = null;

    @Before
    public void setUp() throws Exception {
        SSHConnectConfig sshConnectConfig = new SSHConnectConfig();
        sshConnectConfig.setHost("127.0.0.1");
        sshConnectConfig.setUsername("user");
        sshConnectConfig.setPassword("password");
        client = new TracCISClient(sshConnectConfig);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreateProject() {
        client.createProject(project);
        assertTrue(client.isSuccess());
    }

    @Test
    public void testCreateRoleIfNessceary() {
        client.createRoleIfNecessary(project, "developer");
        assertTrue(client.isSuccess());
    }

    @Test
    public void testAssignUserToRole() {
        client.assignUserToRole(project, "zjh", "developer");
        assertTrue(client.isSuccess());
    }

}
