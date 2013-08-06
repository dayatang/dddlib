package org.openkoala.auth.application.vo;

import java.io.Serializable;

public class UserVO extends IdentityVO implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = -6619649965246109915L;
	private String lastLoginTime;
	private String userAccount;
	private String userPassword;
	private String userDesc;
	private String lastModifyTime;
	private boolean valid;

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getUserDesc() {
		return userDesc;
	}

	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public UserVO() {

	}

	public UserVO(Long id, String name, int sortOrder, String userAccount, String userDesc, boolean valid) {
		this.setId(id);
		this.setName(name);
		this.setSortOrder(sortOrder);
		this.setUserAccount(userAccount);
		this.setUserDesc(userDesc);
		this.setValid(valid);
	}

}
