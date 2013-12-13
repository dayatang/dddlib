package org.openkoala.opencis.pojo;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.opencis.PropertyIllegalException;

public class SonarServerConfiguration {

	private String serverAddress;
	
	private String username;
	
	private String password;

	public SonarServerConfiguration(String serverAddress, String username, String password) {
		this.serverAddress = serverAddress;
		this.username = username;
		this.password = password;
		verifyNotBlank();
	}

	private void verifyNotBlank() {
		if(StringUtils.isBlank(serverAddress)){
			throw new PropertyIllegalException("Sonar服务器地址不能为空！");
		}
		if(StringUtils.isBlank(username)){
			throw new PropertyIllegalException("管理员登陆账号不能为空！");
		}
		if(StringUtils.isBlank(password)){
			throw new PropertyIllegalException("管理员登陆密码不能为空！");
		}
	}
	
	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

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
	
}
