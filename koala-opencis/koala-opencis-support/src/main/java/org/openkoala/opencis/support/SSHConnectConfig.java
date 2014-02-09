package org.openkoala.opencis.support;

/**
 * SSH连接参数
 * User: zjzhai
 * Date: 1/14/14
 * Time: 3:24 PM
 */

public class SSHConnectConfig {
	private String host;

	private String username;

	private String password;
    
	private String storePath;
	
    public SSHConnectConfig() {

    }

    public SSHConnectConfig(String host, String username, String password,String storePath) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.storePath = storePath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}
}
