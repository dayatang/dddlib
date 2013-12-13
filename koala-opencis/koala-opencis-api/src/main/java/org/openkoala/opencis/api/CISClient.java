package org.openkoala.opencis.api;

/**
 * CIS的能用接口
 * @author lingen
 *
 */
public interface CISClient {

	/**
	 * 创建CIS某个工具中创建一个项目
	 * @param project
	 */
	public void createProject(Project project);
	
	/**
	 * 在CIS某个工具中创建一个用户，如果此用户不存在的话
	 * @param developer
	 */
	public void createUserIfNecessary(Project project,Developer developer);
	
	/**
	 * 在 CIS 某个工具中创建一个角色，如果此角色不存在
	 * @param roleName
	 */
	public void createRoleIfNessceary(Project project,String roleName);
	
	/**
	 * 在 CIS 某个工具中将角色同某个用户关联
	 * @param usrId
	 * @param role
	 */
	public void assignUserToRole(Project project,String userId,String role);
	
	/**
	 * 检测指定工具是否可连接
	 * @param url
	 * @param username
	 * @param password
	 */
	public boolean canConnect();
	
}
