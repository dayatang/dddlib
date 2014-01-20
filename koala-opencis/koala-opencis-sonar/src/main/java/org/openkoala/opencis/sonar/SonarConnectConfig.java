package org.openkoala.opencis.sonar;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.opencis.CISClientBaseRuntimeException;

import java.net.MalformedURLException;
import java.net.URL;

public class SonarConnectConfig {

    private String address;

    private String username;

    private String password;

    private URL addressURL;

    public SonarConnectConfig(String address, String username, String password) {

        this.address = address;
        this.username = username;
        this.password = password;
        validate();

    }

    private void validate() {
        if (StringUtils.isBlank(address)) {
            throw new CISClientBaseRuntimeException("sonar.connectAddress.null");
        }

        if (StringUtils.isBlank(username)) {
            throw new CISClientBaseRuntimeException("sonar.connectUsername.null");
        }

        if (StringUtils.isBlank(password)) {
            throw new CISClientBaseRuntimeException("sonar.connectPassword.null");
        }

        try {
            addressURL = new URL(address);
        } catch (MalformedURLException e) {
            throw new CISClientBaseRuntimeException("sonar.connectAddress.MalformedURLException");
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getHost() {
        return addressURL.getHost();
    }

    public int getPort() {
        return addressURL.getPort();
    }

    public String getProtocol() {
        return addressURL.getProtocol();
    }


}
