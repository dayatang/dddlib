package org.openkoala.auth.application.vo;

import java.io.Serializable;
import java.util.Date;
import org.openkoala.koala.auth.core.domain.User;
import com.dayatang.utils.DateUtils;

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
	private String email;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void domain2Vo(User user) {
        this.setId(user.getId());
        this.setLastLoginTime(!"".equals(user.getLastLoginTime()) ? "" : user.getLastLoginTime().toString());
        this.setUserAccount(user.getUserAccount());
        this.setUserPassword(user.getUserPassword());
        this.setUserDesc(user.getUserDesc());
        this.setAbolishDate(!"".equals(user.getAbolishDate()) ? "" : user.getAbolishDate().toString());
        this.setCreateDate(!"".equals(user.getCreateDate()) ? "" : user.getCreateDate().toString());
        this.setCreateOwner(user.getCreateOwner());
        this.setName(user.getName());
        this.setSerialNumber(user.getSerialNumber());
        this.setSortOrder(user.getSortOrder());
        this.setValid(user.isValid());
        this.setEmail(user.getEmail());
	}
	
	public void vo2Domain(User user) {
		user.setAbolishDate(DateUtils.MAX_DATE);
        user.setCreateDate(new Date());
        user.setUserAccount(this.getUserAccount());
        user.setUserPassword(this.getUserPassword());
        user.setUserDesc(this.getUserDesc());
        user.setCreateOwner(this.getCreateOwner());
        user.setName(this.getName());
        user.setSerialNumber(this.getSerialNumber());
        user.setSortOrder(this.getSortOrder());
        user.setValid(this.isValid());
        user.setEmail(this.getEmail());
	}
	
}
