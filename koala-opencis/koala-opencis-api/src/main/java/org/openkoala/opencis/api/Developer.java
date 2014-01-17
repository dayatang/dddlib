package org.openkoala.opencis.api;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 开发者对象
 * @author lingen
 *
 */
public class Developer {
	
	/**
	 * 用户 ID
	 */
	private String id;
	
	/**
	 * 用户名称
	 */
	private String name;
	
	/**
	 * 密码
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
	
	/**
	 * 用户角色
	 */
	private List<String> roles;


    public void validate() {
        if (StringUtils.isBlank(name) ) {
        }


    }

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
