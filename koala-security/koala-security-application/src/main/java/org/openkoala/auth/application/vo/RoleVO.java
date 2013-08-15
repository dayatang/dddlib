package org.openkoala.auth.application.vo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openkoala.koala.auth.core.domain.Role;

import com.dayatang.utils.DateUtils;

public class RoleVO extends IdentityVO implements Serializable {

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
	
	public void domain2Vo(Role role) {
		DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.setAbolishDate(formater.format(role.getAbolishDate()));
        this.setCreateDate(formater.format(role.getCreateDate()));
        this.setCreateOwner(role.getCreateOwner());
        this.setId(role.getId());
        this.setName(role.getName());
        this.setSerialNumber(role.getSerialNumber());
        this.setSortOrder(role.getSortOrder());
        this.setRoleDesc(role.getRoleDesc());
        this.setValid(role.isValid());
        this.setVersion(role.getVersion());
	}
	
	public void vo2Domain(Role role) {
		role.setAbolishDate(DateUtils.MAX_DATE);
		role.setCreateDate(new Date());
		role.setCreateOwner(this.getCreateOwner());
		role.setName(this.getName());
		role.setRoleDesc(this.getRoleDesc());
		role.setSerialNumber(this.getSerialNumber());
		role.setSortOrder(this.getSortOrder());
		role.setValid(this.isValid());
	}
	
}
