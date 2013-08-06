package org.openkoala.auth.application.vo;

import java.io.Serializable;


public class RoleVO extends IdentityVO implements Serializable{


	
	/**
     * 
     */
    private static final long serialVersionUID = 7192376219578931945L;
    private String roleDesc;
	
	public String getRoleDesc() {
		return roleDesc;
	}
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
}
