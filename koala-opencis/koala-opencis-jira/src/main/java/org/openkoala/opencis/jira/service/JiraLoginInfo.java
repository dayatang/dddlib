package org.openkoala.opencis.jira.service;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.openkoala.opencis.jira.service.impl.AdminPasswordBlankException;
import org.openkoala.opencis.jira.service.impl.AdminUserNameBlankException;
import org.openkoala.opencis.jira.service.impl.ServerAddressBlankException;

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
	
	/**
	 * 检查登陆信息是否为空
	 * @param loginInfo
	 * @return
	 */
	public boolean checkNotBlank(){
		if(StringUtils.isBlank(serverAddress)){
			throw new ServerAddressBlankException("服务器地址不能为空！");
		}
		if(StringUtils.isBlank(adminUserName)){
			throw new AdminUserNameBlankException("管理员登陆账号不能为空！");
		}
		if(StringUtils.isBlank(adminPassword)){
			throw new AdminPasswordBlankException("管理员登陆密码不能为空！");
		}
		return true;
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
