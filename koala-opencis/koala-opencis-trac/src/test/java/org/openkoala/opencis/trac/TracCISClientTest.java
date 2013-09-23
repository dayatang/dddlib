package org.openkoala.opencis.trac;

import static org.junit.Assert.fail;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.opencis.api.Project;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

public class TracCISClientTest {

	Project project = null;
	Connection conn = null;
	@Before
	public void setUp() throws Exception {
		project = new TestTracProject();
		conn = new Connection("10.108.1.134");
	}

	@After
	public void tearDown() throws Exception {
		conn.close();
	}

	@Test
	public void testCreateProject() {
		Session session = null;
		String createProjectCommand = "trac-admin " + project.getProjectPath() + " initenv " + project.getArtifactId() + " sqlite:db/trac.db";
		System.out.println(createProjectCommand);
		try {
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword("root", "password");
//			Assert.assertFalse(isAuthenticated);
			if (isAuthenticated == false){
				throw new IOException("Authentication failed.");
			}
			session = conn.openSession();
			session.execCommand(createProjectCommand);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			session.close();
		}

	}

	@Test
	public void testCreateUserIfNecessary() {
		Session session = null;
		String createUserCommand = "trac-admin " + project.getProjectPath() + " permission add zjh2 TRAC_ADMIN"; 
		System.out.println(createUserCommand);
		try {
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword("root", "password");
//			Assert.assertFalse(isAuthenticated);
			if (isAuthenticated == false){
				throw new IOException("Authentication failed.");
			}
			session = conn.openSession();
			session.execCommand(createUserCommand);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			session.close();
		}
	}

	@Test
	public void testCreateRoleIfNessceary() {
		Session session = null;
		String createUserCommand = "trac-admin " + project.getProjectPath() + " permission add zjh2 TRAC_ADMIN"; 
		System.out.println(createUserCommand);
		try {
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword("root", "password");
//			Assert.assertFalse(isAuthenticated);
			if (isAuthenticated == false){
				throw new IOException("Authentication failed.");
			}
			session = conn.openSession();
			session.execCommand(createUserCommand);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			session.close();
		}
	}

	@Test
	public void testAssignUserToRole() {
		fail("Not yet implemented");
	}

}
