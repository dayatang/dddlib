package org.openkoala.opencis.api;

/**
 * SSH连接参数
 * User: zjzhai
 * Date: 1/14/14
 * Time: 3:24 PM
 */

public class SSHConnectConfig {
    public String host;

    public String username;

    public String password;

    public SSHConnectConfig() {

    }

    public SSHConnectConfig(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
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
}
