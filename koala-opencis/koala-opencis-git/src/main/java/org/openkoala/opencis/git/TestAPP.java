package org.openkoala.opencis.git;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.GatheringByteChannel;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabUser;

public class TestAPP {

	/**
	 * @param args
	 * @throws GitAPIException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws GitAPIException, IOException {
		
		
//		File testProject = new File("E:/temp/temp/testproject");
		
//		if (!testProject.exists()) {
//			testProject.mkdir();
//		}
//		
//		File gitfile = new File("E:/temp/temp/testproject/.git");
//		
//		if (!gitfile.exists()) {
//			Git.init().setDirectory(testProject).call();
//		}
		
//		
//		Git git = Git.open(testProject);
//		
//		GitlabAPI gitlabAPI = GitlabAPI.connect("http://10.108.1.83", "Hroq8x5zFZasydzFXEvC");
//		System.out.println(gitlabAPI.getAPIUrl(""));
//		
//		GitlabUser gitlabUser = new GitlabUser();
////		gitlabUser.setEmail("fxmlyh@126.com");
////		gitlabUser.setUsername("testuser1");
////		gitlabUser.setName("test-name");
////		gitlabUser.setState("active");
//		
//		GitlabHTTPRequestor gitlabHTTPRequestor = new GitlabHTTPRequestor(gitlabAPI);
//		
//		gitlabHTTPRequestor.method("POST")
//			.with("email", "fxmlyh@126.com").with("username", "testuser1").with("name", "test-name").with("password", "testuser1")
//			.to(GitlabUser.URL, GitlabUser.class, gitlabUser);
//		
//		System.out.println(gitlabUser.getEmail());
//		
////		List<LinkedHashMap<String, Object>> usersMap = gitlabHTTPRequestor.method("GET").to("users", List.class);
////		for (LinkedHashMap<String, Object> userMap : usersMap) {
////			if ((Integer) userMap.get("id") == 1) {
////				GitlabUser user = new GitlabUser();
//////				user.setEmail(String.valueOf(userMap.get("email")));
//////				user.setUsername(String.valueOf(userMap.get("username")));
//////				user.setName(String.valueOf(userMap.get("name")));
//////				user.setState(String.valueOf(userMap.get("state")));
//////				user.setCreatedAt((Date) userMap.get("created_at"));
////				user.setSkype("aaa");
////				gitlabHTTPRequestor.method("PUT").to(GitlabUser.URL + "/1", GitlabUser.class, user);
////			}
////		}
//		
//		System.out.println(gitlabHTTPRequestor.method("GET").to("users", List.class));
////		System.out.println(gitlabHTTPRequestor.method("GET").to("users" + "/1", GitlabUser.class));
		
		Repository repository = null;
		
		String path = "E:/temp/temp/jgitproject";
		
		InitCommand init = new InitCommand();
		init.setDirectory(new File(path));
		
		Git git;
		try {
			git = init.call();
			repository = git.getRepository();
			System.out.println("Create repository success.");
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		
		StoredConfig config = repository.getConfig();
		try {
			RemoteConfig remoteConfig = new RemoteConfig(config, "origin");
			
			URIish uri = new URIish("http://10.108.1.83/xinminfang/jgitproject");
			
			RefSpec refSpec = new RefSpec("+refs/head/*:refs/remotes/origin/*");
			
			remoteConfig.addFetchRefSpec(refSpec);
			remoteConfig.addPushRefSpec(refSpec);
			remoteConfig.addURI(uri);
			remoteConfig.addPushURI(uri);
			
			remoteConfig.update(config);
			config.save();
			repository.close();
			
			File addfile = new File("E:/temp/temp/jgitproject/addfile.txt");
			FileWriter fileWriter = new FileWriter(addfile);
			fileWriter.write("test");
			fileWriter.flush();
			fileWriter.close();
			
			File testProject = new File("E:/temp/temp/jgitproject");
			Git theGit = Git.open(testProject);
			
			theGit.add().addFilepattern(".+").call();
			theGit.commit().setCommitter("xinminfang", "xinmin.fang@gmail.com").setMessage("test jgit commit").call();
			theGit.push().call();
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
