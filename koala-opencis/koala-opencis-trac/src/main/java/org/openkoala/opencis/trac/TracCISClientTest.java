package org.openkoala.opencis.trac;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.openkoala.opencis.api.Project;

import com.dayatang.configuration.Configuration;

public class TracCISClientTest {

	private Project project = new MockProject();
	
	private Configuration configuration = new MockConfiguration();
	
	private TracCISClient client = null;
	
	@Before
	public void setUp() throws Exception {
		client = new TracCISClient(configuration);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=java.lang.Exception.class)
	public void testCreateProject() {
		client.createProject(project);
	}

	@Test
	public void testCreateRoleIfNessceary() {
		client.createRoleIfNessceary(project, "developer");
	}

	@Test
	public void testAssignUserToRole() {
		client.assignUserToRole(project, "zjh", "developer");
	}

}
