package org.openkoala.opencis.trac;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.opencis.api.Project;

public class TracAppTest {

	private Project project = null;
	
	@Before
	public void setUp() throws Exception {
		project = new TestTracProject();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateProject() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testCreateRole(){
		
	}

}
