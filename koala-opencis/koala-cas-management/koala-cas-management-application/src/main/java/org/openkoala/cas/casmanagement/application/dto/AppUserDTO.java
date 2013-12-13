package org.openkoala.cas.casmanagement.application.dto;

import org.openkoala.cas.casmanagement.core.*;
import java.io.Serializable;

public class AppUserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6555343866505716054L;

	private Long id;

	private String username;

	private String email;

	private String password;

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void domain2Dto(AppUser appUser) {
		this.setId(appUser.getId());
		this.setUsername(appUser.getUsername());
		this.setEmail(appUser.getEmail());
		this.setPassword(appUser.getPassword());
	}

	public void dto2Domain(AppUser appUser) {
		appUser.setUsername(this.getUsername());
		appUser.setEmail(this.getEmail());
		appUser.setPassword(this.getPassword());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		AppUserDTO other = (AppUserDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}