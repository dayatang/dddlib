package org.openkoala.koala.auth.vo;

import java.util.Date;
import java.util.List;

import org.openkoala.koala.auth.UserDetails;


public class JdbcCustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	private List<String> authorities;
	private String password;
	private String useraccount;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	private String description;
	private String email;
	private Date passwordLastUpdateDate;
	private String realName;
	private Date registryDate;
	private boolean isSuper;
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getPasswordLastUpdateDate() {
		return passwordLastUpdateDate;
	}

	public void setPasswordLastUpdateDate(Date passwordLastUpdateDate) {
		this.passwordLastUpdateDate = passwordLastUpdateDate;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Date getRegistryDate() {
		return registryDate;
	}

	public void setRegistryDate(Date registryDate) {
		this.registryDate = registryDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public String getPassword() {
		return password;
	}

	public String getUseraccount() {
		return useraccount;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isSuper() {
        return isSuper;
    }

    public void setSuper(boolean isSuper) {
        this.isSuper = isSuper;
    }

    public JdbcCustomUserDetails()
	{
		
	}
	
}
