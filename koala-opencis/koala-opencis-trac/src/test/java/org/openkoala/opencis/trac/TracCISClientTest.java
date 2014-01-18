package org.openkoala.opencis.trac;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SSHConnectConfig;

@Ignore
public class TracCISClientTest {

    private Project project = new MockProject();

    private TracCISClient client = null;

    @Before
    public void setUp() throws Exception {
        SSHConnectConfig sshConnectConfig = new SSHConnectConfig();
        sshConnectConfig.setHost("10.108.1.134");
        sshConnectConfig.setUsername("root");
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

    @Test
    public void testRemoveProject(){
    	client.removeProject(project);
    	assertTrue(client.isSuccess());
    }
    
    @Test
    public void testRemoveUser(){
    	client.removeUser(project, project.getDevelopers().get(0));
    }
    
    public static void main(String[] args) {
    	TracCISClientTest cisClient = new TracCISClientTest();
    	try {
    		cisClient.setUp();
    		cisClient.testRemoveProject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
}
