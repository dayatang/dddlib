package org.openkoala.opencis.jenkins;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.opencis.api.Project;

/**
 * 
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:56:28 AM
 */
public class JenkinsCISClientTest {
	
	private JenkinsCISClient jenkinsCISClient;
	
	private Project project;
	
	@Before
	public void setUp() {
		jenkinsCISClient = new JenkinsCISClient();
		jenkinsCISClient.setJenkinsHost("http://localhost:8888/jenkins");
		project = new Project();
		project.setArtifactId("myJob");
	}

	@Test
	public void testCreateJob() {
		jenkinsCISClient.createProject(project);
	}
	
	@After
	public void tearDown() {
		
	}
}
