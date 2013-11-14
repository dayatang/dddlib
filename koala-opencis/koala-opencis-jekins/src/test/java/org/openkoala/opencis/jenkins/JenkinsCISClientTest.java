package org.openkoala.opencis.jenkins;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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
	
	private static final String JOB_NAME = "myJob";

	private static final String JENKINS_HOST = "http://localhost:8080/jenkins";

	private JenkinsCISClient jenkinsCISClient;
	
	private Project project;
	
	@Before
	public void setUp() {
		init();
	}

	private void init() {
		jenkinsCISClient = new JenkinsCISClient();
		jenkinsCISClient.setJenkinsHost(JENKINS_HOST);
		project = new Project();
		project.setArtifactId(JOB_NAME);
	}

	@Test
	public void testCreateJob() {
		jenkinsCISClient.createProject(project);
	}
	
	private void confirmRemoveJob() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(getConfirmRemoveJobUrl());
		try {
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST
					|| response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				throw new RemoveJobFailureException("Remove job failure.");
			}
		} catch (Exception e) {
			throw new RemoveJobFailureException(e);
		}
	}
	
	private String getConfirmRemoveJobUrl() {
		return new StringBuilder(JENKINS_HOST).append("/job/").append(JOB_NAME).append("/doDelete").toString();
	}

	@After
	public void tearDown() {
		confirmRemoveJob();
	}
}
