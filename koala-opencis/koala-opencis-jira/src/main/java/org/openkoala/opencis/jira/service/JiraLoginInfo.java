package org.openkoala.opencis.jira.service;

import java.io.Serializable;

public class JiraLoginInfo implements Serializable {
	private static final long serialVersionUID = -3313144886863753222L;

	/**
	 * 服务器地址（必填）
	 */
	private String serverAddress;

	/**
	 * 管理员账号（即能创建project的账号）（必填）
	 */
	private String adminUserName;

	/**
	 * 管理员密码（必填）
	 */
	private String adminPassword;
	
	public JiraLoginInfo(){
		
	}
	
	public JiraLoginInfo(String serverAddress, String adminUserName, String adminPassword){
		this.serverAddress = serverAddress;
		this.adminUserName = adminUserName;
		this.adminPassword = adminPassword;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getAdminUserName() {
		return adminUserName;
	}

	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	
	
}
