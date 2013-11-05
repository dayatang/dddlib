package org.openkoala.opencis.hudson;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

public class HudsonCISClientTest {

	private static final String SCM_ADDRESS = "http://git.oschina.net/openkoala/koala.git";
	
	private Project project;
	private HudsonCISClient cisClient;
	
//	@Before
//	public void setUp() throws Exception {
//		project = createProject();
//		cisClient = new HudsonCISClient();
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		removeProject();
//	}
	
	@Test
	public void testCreateProject() {
		cisClient.createProject(project);
	}
	
	@Test
	public void testCreateUserAccount() {
		Developer developer = new Developer();
		developer.setName("zhangsan");
		developer.setEmail("zhangsan@gmail.com");
		cisClient.createUserIfNecessary(null, developer);
	}
	
	@Test
	public void testCreateRole() {
		cisClient = new HudsonCISClient();
		cisClient.authenticate("admin", "admin");
//		cisClient.createRoleIfNessceary(null, null);
	}
	
	private Project createProject() {
		Project project = new Project();
		project.setArtifactId("demo");
		project.setNodeElements(createNodeElments());
		return project;
	}
	
	private Map<String, Object> createNodeElments() {
		Map<String, Object> nodeElements = new HashMap<String, Object>();
		nodeElements.put("project", createScmNode());
		
		return nodeElements;
	}
	
	private Map<String, Object> createScmNode() {
		Map<String, Object> nodeElements = new HashMap<String, Object>();
		nodeElements.put("scm", createScmDetailElement());
		return nodeElements;
	}
	
	private Map<String, Object> createScmDetailElement() {
		Map<String, Object> scmNodeElements = new HashMap<String, Object>();
		scmNodeElements.put("connection", SCM_ADDRESS);
		scmNodeElements.put("developerConnection", SCM_ADDRESS);
		scmNodeElements.put("tag", "HEAD");
		return scmNodeElements;
	}
	
	private void removeProject() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://localhost:8080/hudson/job/demo/doDelete");
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
			System.out.println(response.getStatusLine().getStatusCode());
			System.out.println(response.getEntity().getContent().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
