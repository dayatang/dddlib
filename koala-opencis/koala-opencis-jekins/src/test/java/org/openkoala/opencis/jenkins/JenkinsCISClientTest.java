package org.openkoala.opencis.jenkins;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.JenkinsServerConfigurationNullException;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.pojo.JenkinsServerConfiguration;

/**
 * 
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:56:28 AM
 */
public class JenkinsCISClientTest {
	
	private static final String JOB_NAME = "tenpay";

	private static final String JENKINS_HOST = "http://localhost:8888/jenkins";

	private JenkinsCISClient jenkinsCISClient;
	
	private Project project;
	
	private Developer developer;
	
	private JenkinsServerConfiguration jenkinsServerConfiguration = new JenkinsServerConfiguration(JENKINS_HOST, "test", "test");
	
	@Before
	public void setUp() {
		init();
	}
	
	@After
	public void tearDown() {
		jenkinsCISClient.confirmRemoveJob(JOB_NAME);
	}

	private void init() {
		jenkinsCISClient = new JenkinsCISClient(jenkinsServerConfiguration);
		project = new Project();
		project.setArtifactId(JOB_NAME);
		developer = new Developer();
		developer.setName("www");
		developer.setEmail("admin@gmail.com");
		createJob();
	}

	private void createJob() {
		jenkinsCISClient.createProject(project);
	}

	@Ignore
	@Test(expected = JenkinsServerConfigurationNullException.class)
	public void testJenkinsCISIfJenkinsServerConfigurationNull() {
		jenkinsCISClient = new JenkinsCISClient(null);
	}
	
	@Test
	public void testCreateAccount() {
		jenkinsCISClient.createUserIfNecessary(project, developer);
	}
	
	@Test
	public void testCreateRole() {
		jenkinsCISClient.createRoleIfNessceary(null, "koala");
	}
	
	@Test
	public void testAssignUserToRole() {
		jenkinsCISClient.assignUserToRole(project, "koala", null);
	}
	
}
