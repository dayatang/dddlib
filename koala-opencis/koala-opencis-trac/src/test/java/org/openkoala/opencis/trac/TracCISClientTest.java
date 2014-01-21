package org.openkoala.opencis.trac;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SSHConnectConfig;

//@Ignore
public class TracCISClientTest {

    private Project project = new MockProject();
    
    private Developer developer = null;

    private TracCISClient client = null;

    @Before
    public void setUp() throws Exception {
        SSHConnectConfig sshConnectConfig = new SSHConnectConfig();
        sshConnectConfig.setHost("10.108.1.87");
        sshConnectConfig.setUsername("root");
        sshConnectConfig.setPassword("password");
        sshConnectConfig.setStorePath("/opencis/trac/");
        client = new TracCISClient(sshConnectConfig);
        
        developer = new Developer();
        developer.setId("zjh");
        developer.setName("zjh");
        developer.setPassword("zjh");
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
        client.createUserIfNecessary(project, developer);
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
    	client.removeUser(project, developer);
    }
    
//    public static void main(String[] args) {
//    	TracCISClientTest cisClient = new TracCISClientTest();
//    	try {
//    		cisClient.setUp();
//    		cisClient.testRemoveProject();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//	}
}
