package org.openkoala.opencis.git.impl;

public class GitlabConfiguration {

	private String token;
	
	private String gitHostURL;

	private String adminUsername;
	
	private String adminPassword;
	
	private String adminEmail;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getGitHostURL() {
		return gitHostURL;
	}

	public void setGitHostURL(String gitHostURL) {
		this.gitHostURL = gitHostURL;
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
	
}
