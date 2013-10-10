package org.openkoala.opencis.jira.service;

import java.util.List;

/**
 * 通过代码操作JIRA服务器接口
 * @author lambo
 *
 */
public interface JiraOperateService {

	/**
	 * 创建项目
	 * @param projectInfo
	 */
	void createProjectToJira(JiraProjectInfo projectInfo);
	
	/**
	 * 创建用户（若未输入密码，则密码默认等于用户名）
	 * @param userInfo
	 */
	void createUserToJiraIfNecessary(JiraUserInfo userInfo);
	
	/**
	 * 创建角色
	 * @param roleInfo
	 */
	void createRoleToJira(JiraRoleInfo roleInfo);
	
	/**
	 * 为用户分配项目角色
	 * @param projectInfo
	 * @param userInfo
	 * @param roleInfo
	 */
	void addProjectRoleToUser(JiraProjectInfo projectInfo, JiraUserInfo userInfo, JiraRoleInfo roleInfo);
	
	/**
	 * 从JIRA服务器查询得到所有的角色名称
	 * @param loginInfo
	 * @return
	 */
	List<String> getAllRoleNamesFromJira(JiraLoginInfo loginInfo);
	
}
