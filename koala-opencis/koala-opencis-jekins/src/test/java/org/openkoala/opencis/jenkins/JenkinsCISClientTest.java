package org.openkoala.opencis.jenkins;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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
@Ignore
public class JenkinsCISClientTest {
	
	private static final String JOB_NAME = "tenpay";

	private static final String JENKINS_HOST = "http://localhost:8080/jenkins";

	private JenkinsCISClient jenkinsCISClient;
	
	private Project project;
	
	private Developer developer;
	
	private JenkinsServerConfiguration jenkinsServerConfiguration = new JenkinsServerConfiguration(JENKINS_HOST, "test", "test");
	
	@Before
	public void setUp() {
		init();
	}

	private void init() {
		jenkinsCISClient = new JenkinsCISClient(jenkinsServerConfiguration);
		project = new Project();
		project.setArtifactId(JOB_NAME);
		developer = new Developer();
		developer.setName("admin");
		developer.setEmail("admin@gmail.com");
	}

	@Test
	public void testCreateJob() {
		jenkinsCISClient.createProject(project);
		confirmRemoveJob();
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
	
	private void confirmRemoveJob() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(getConfirmRemoveJobUrl());
		try {
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
				throw new RemoveJobFailureException("Remove job failure.");
			}
		} catch (Exception e) {
			throw new RemoveJobFailureException(e);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	private String getConfirmRemoveJobUrl() {
		return new StringBuilder(JENKINS_HOST).append("/job/").append(JOB_NAME).append("/doDelete").toString();
	}

}
