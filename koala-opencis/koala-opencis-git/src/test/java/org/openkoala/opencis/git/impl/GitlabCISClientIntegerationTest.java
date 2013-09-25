package org.openkoala.opencis.git.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.junit.Test;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;

public class GitlabCISClientIntegerationTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateProject() throws IOException {
		GitCISProject project = getProjectForTest();
		GitlabConfiguration configuration = getConfigurationForTest();
		
		GitlabAPI gitlabAPI = GitlabAPI.connect(configuration.getGitHostURL(), configuration.getToken());
		GitlabHTTPRequestor gitlabHTTPRequestor = new GitlabHTTPRequestor(gitlabAPI);
		Set<String> projectNames = generateProjectNames(gitlabHTTPRequestor.method("GET").to(GitlabProject.URL, List.class));
		assertFalse(projectNames.contains(project.getProjectName()));
		
		CISClient cisClient = new GitlabCISClient(configuration);
		cisClient.createProject(project);
		
		projectNames = generateProjectNames(gitlabHTTPRequestor.method("GET").to(GitlabProject.URL, List.class));
		assertTrue(projectNames.contains(project.getProjectName()));
	}

	private Set<String> generateProjectNames(List<LinkedHashMap<String, Object>> projectsMap) {
		Set<String> results = new HashSet<String>();
		for (LinkedHashMap<String, Object> projectMap : projectsMap) {
			results.add((String) projectMap.get("name"));
		}
		return results;
	}
	
	private GitCISProject getProjectForTest() {
		List<Developer> developers = new ArrayList<Developer>();
		Developer developer1 = new Developer();
		developer1.setId("testuser1");
		developer1.setEmail("fangxinmin1@foreveross.com");
		developer1.setName("test-user1");
		developers.add(developer1);
		
		Developer developer2 = new Developer();
		developer2.setId("testuser2");
		developer2.setEmail("fangxinmin2@foreveross.com");
		developer2.setName("test-user2");
		developers.add(developer2);

		Developer developer3 = new Developer();
		developer3.setId("testuser3");
		developer3.setEmail("fangxinmin3@foreveross.com");
		developer3.setName("test-user3");
		developers.add(developer3);

		GitCISProject result = new GitCISProject();
		result.setArtifactId("projectForTest");
		result.setDescription("This project is for test");
		result.setProjectName("projectForTest");
		result.setProjectDeveloper(developers);
		result.setProjectPath("E:\\temp\\temp\\projectForTest");
		
		return result;
	}
	
	private GitlabConfiguration getConfigurationForTest() {
		GitlabConfiguration result = new GitlabConfiguration();
		result.setAdminUsername("xinminfang");
		result.setAdminPassword("xmfang");
		result.setAdminEmail("xinmin.fang@gmail.com");
		result.setGitHostURL("http://192.168.100.122");
		result.setToken("Hroq8x5zFZasydzFXEvC");
		return result;
	}
	
	@Test
	public void testCreateUserIfNecessary() {
		Developer developer = new Developer();
		developer.setId("testuser");
		developer.setEmail("fangxinmin@foreveross.com");
		developer.setName("test-user");
		
		GitlabConfiguration configuration = getConfigurationForTest();
		
		assertFalse(getCurrentGitlabUserAccounts(configuration).contains(developer.getId()));
		
		CISClient cisClient = new GitlabCISClient(configuration);
		cisClient.createUserIfNecessary(developer);
		
		List<String> userAccounts = getCurrentGitlabUserAccounts(configuration);
		assertTrue(userAccounts.contains(developer.getId()));
		assertTrue(onlyContainsOne(userAccounts, developer.getId()));
		
		cisClient.createUserIfNecessary(developer);
		assertTrue(onlyContainsOne(userAccounts, developer.getId()));
	}
	
	private boolean onlyContainsOne(List<String> collection, String theString) {
		int result = 0;
		
		for (String single : collection) {
			if (single.equals(theString)) {
				result ++;
			}
		}
		
		return result == 1;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getCurrentGitlabUserAccounts(GitlabConfiguration configuration) {
		List<String> results = new ArrayList<String>();

		GitlabAPI gitlabAPI = GitlabAPI.connect(configuration.getGitHostURL(), configuration.getToken());
		GitlabHTTPRequestor gitlabHTTPRequestor = new GitlabHTTPRequestor(gitlabAPI);
		List<LinkedHashMap<String, Object>> usersMap = null;
		try {
			usersMap = gitlabHTTPRequestor.method("GET").to(GitlabUser.URL, List.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (LinkedHashMap<String, Object> userMap : usersMap) {
			results.add((String) userMap.get("username"));
		}
		return results;
	}
}
