package org.openkoala.opencis.jira.service;

public class JiraProjectInfo extends JiraLoginInfo{
	private static final long serialVersionUID = -2012309131315178162L;

	/**
	 * the new project key 自定义项目唯一标识（必填） 只能是英文字母，且所有英文字母必须大写
	 */
	private String projectKey;

	/**
	 * 需要创建的项目名称（若不填，默认和projectKey一样）
	 */
	private String projectName;

	/**
	 * 项目负责人（必填）
	 */
	private String projectLead;

	/**
	 * 项目描述（选填）
	 */
	private String desc;

	public JiraProjectInfo() {

	}

	/**
	 * 所有必填项
	 * @param serverAddress
	 * @param adminUserName
	 * @param adminPassword
	 * @param projectKey
	 * @param projectLead
	 */
	public JiraProjectInfo(String serverAddress, String adminUserName,
			String adminPassword, String projectKey, String projectLead) {
		this(serverAddress, adminUserName, adminPassword, projectKey,
				projectKey, projectLead, null);
	}

	/**
	 * 所有属性
	 * @param serverAddress
	 * @param adminUserName
	 * @param adminPassword
	 * @param projectKey
	 * @param projectName
	 * @param projectLead
	 * @param desc
	 */
	public JiraProjectInfo(String serverAddress, String adminUserName,
			String adminPassword, String projectKey, String projectName,
			String projectLead, String desc) {
		super(serverAddress,adminUserName,adminPassword);
		this.projectKey = projectKey;
		this.projectName = projectName;
		this.projectLead = projectLead;
		this.desc = desc;
	}

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectLead() {
		return projectLead;
	}

	public void setProjectLead(String projectLead) {
		this.projectLead = projectLead;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
