package org.openkoala.opencis.jira.service;

/**
 * 待创建的用户信息
 * 
 * @author lambo
 * 
 */
public class JiraUserInfo extends JiraLoginInfo {

	private static final long serialVersionUID = -2012309131315178162L;

	/**
	 * 用户账号
	 */
	private String userName;

	/**
	 * 用户密码
	 */
	private String password;

	/**
	 * 用户全名
	 */
	private String fullName;

	/**
	 * 用户邮箱
	 */
	private String email;

	public JiraUserInfo() {

	}
	
	/**
	 * 所有必填项
	 * @param serverAddress
	 * @param adminUserName
	 * @param adminPassword
	 * @param userName
	 * @param fullName
	 * @param email
	 */
	public JiraUserInfo(String serverAddress, String adminUserName, String adminPassword, 
			String userName, String fullName, String email) {
		this(serverAddress, adminUserName, adminPassword,userName,null,fullName,email);
	}

	/**
	 * 所有属性
	 * @param serverAddress
	 * @param adminUserName
	 * @param adminPassword
	 * @param userName
	 * @param password
	 * @param fullName
	 * @param email
	 */
	public JiraUserInfo(String serverAddress, String adminUserName, String adminPassword,
			String userName, String password, String fullName, String email) {
		super(serverAddress, adminUserName, adminPassword);
		this.userName = userName;
		this.password = password;
		this.fullName = fullName;
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
