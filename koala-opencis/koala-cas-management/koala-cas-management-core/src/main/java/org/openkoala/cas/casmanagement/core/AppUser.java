package org.openkoala.cas.casmanagement.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dayatang.domain.AbstractEntity;

/**
 * CAS用户
 * @author zhuyuanbiao
 * @date 2013年12月6日 上午10:43:20
 *
 */
@Entity
@Table(name = "app_user")
public class AppUser extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5603177917911045405L;

	@Column(length = 30, nullable = false)
	private String username;
	
	@Column(length = 30, nullable = false)
	private String userAccount;
	
	@Column(length = 100)
	private String description;
	
	@Column(length = 50, nullable = false)
	private String password;
	
	@Column(length = 50)
	private String email;
	
	private boolean enabled;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void enabled() {
		this.setEnabled(true);
		this.save();
	}
	
	public void disable() {
		this.setEnabled(false);
		this.save();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppUser other = (AppUser) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AppUser [username=" + username + ", password=" + password
				+ ", email=" + email + "]";
	}

}
