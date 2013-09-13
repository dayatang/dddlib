package org.openkoala.opencis.jira.service;

public class JiraRoleInfo extends JiraLoginInfo {
	private static final long serialVersionUID = 1660394632346975308L;

	private String roleName;
	private Long permission;
	private String typeDesc;
	
	public JiraRoleInfo(){
		
	}
	
	/**
	 * 所有必填项
	 * @param roleName
	 */
	public JiraRoleInfo(String serverAddress, String adminUserName,
			String adminPassword, String roleName){
		this(serverAddress, adminUserName, adminPassword, roleName, null, null);
	}
	
	/**
	 * 所有属性
	 * @param roleName
	 * @param permission
	 * @param typeDesc
	 */
	public JiraRoleInfo(String serverAddress, String adminUserName, String adminPassword,
			String roleName, Long permission, String typeDesc){
		super(serverAddress, adminUserName, adminPassword);
		this.roleName = roleName;
		this.permission = permission;
		this.typeDesc = typeDesc;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getPermission() {
		return permission;
	}

	public void setPermission(Long permission) {
		this.permission = permission;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

}
