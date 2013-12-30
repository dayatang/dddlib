package org.openkoala.koala.auth;

/**
 * 认证信息
 * @author Ken
 * @date Dec 24, 2013 9:28:31 AM
 *
 */
public class AuthInfo {

	private String username;
	
	private String password;
	
	private String encodedPassword;
	
	private Object salt;

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

	public String getEncodedPassword() {
		return encodedPassword;
	}

	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	public Object getSalt() {
		return salt;
	}

	public void setSalt(Object salt) {
		this.salt = salt;
	}
	
}
