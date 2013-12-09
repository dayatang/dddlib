package org.openkoala.opencis.jira.service;

import org.apache.commons.lang.StringUtils;
import org.openkoala.opencis.jira.service.impl.ProjectKeyBlankException;
import org.openkoala.opencis.jira.service.impl.ProjectKeyLengthNotBetweenTwoAndTenCharacterLettersException;
import org.openkoala.opencis.jira.service.impl.ProjectKeyNotAllCharacterLettersException;
import org.openkoala.opencis.jira.service.impl.ProjectLeadBlankException;

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
	
	/**
	 * 检查登陆信息和项目信息是否为空
	 */
	@Override
	public boolean checkNotBlank() {
		super.checkNotBlank();
		if(StringUtils.isBlank(projectKey)){
			throw new ProjectKeyBlankException("project key不能为空！");
		}
		
		projectKey = checkProjectKeyAndTurnToUppercase();
		
		if(StringUtils.isBlank(projectLead)){
			throw new ProjectLeadBlankException("项目负责人不能为空！");
		}
		return true;
	}
	
	/**
	 * 检查project key 必须都是英文字母，且必须都大写（由代码负责转换），且必须至少2个字母
	 * @param projectKey
	 */
	private String checkProjectKeyAndTurnToUppercase(){
		if(projectKey.length() < 2 || projectKey.length() > 10){
			throw new ProjectKeyLengthNotBetweenTwoAndTenCharacterLettersException(
					"project key '" + projectKey + "' 长度必须为2-10个英文字母！");
		}
		for(int i=0; i<projectKey.length(); i++){
			char c = projectKey.charAt(i);
			if(!((c>=65 && c<=90) || (c>=97 && c<=120))){
				throw new ProjectKeyNotAllCharacterLettersException(
						"project key '" + projectKey + "' 必须都是英文字母！");
			}
		}
		return projectKey.toUpperCase();
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
