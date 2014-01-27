package org.openkoala.koala.auth.impl;

/**
 * 用户配置
 * @author zhuyuanbiao
 * @date Dec 25, 2013 10:46:46 AM
 *
 */
public class UserConfig {

	private String adminAccount;
	
	private String adminPassword;
	
	private boolean enabled;
	
	private String adminRealName;

	public String getAdminAccount() {
		return adminAccount;
	}

	public void setAdminAccount(String adminAccount) {
		this.adminAccount = adminAccount;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAdminRealName() {
		return adminRealName;
	}

	public void setAdminRealName(String adminRealName) {
		this.adminRealName = adminRealName;
	}
	
}
