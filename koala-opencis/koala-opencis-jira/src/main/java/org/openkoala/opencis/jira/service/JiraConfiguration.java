package org.openkoala.opencis.jira.service;

import org.apache.commons.lang.StringUtils;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.jira.service.impl.AdminPasswordBlankException;
import org.openkoala.opencis.jira.service.impl.AdminUserNameBlankException;
import org.openkoala.opencis.jira.service.impl.ServerAddressBlankException;

public class JiraConfiguration {

    private String serverAddress;

    private String adminUserName;

    private String adminPassword;

    public JiraConfiguration() {

    }

    public JiraConfiguration(String serverAddress, String adminUserName, String adminPassword) {
        this.serverAddress = serverAddress;
        this.adminUserName = adminUserName;
        this.adminPassword = adminPassword;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    /**
     * 检查登陆信息是否为空
     *
     * @return
     */
    public boolean checkLoginInfoNotBlank() {
        if (StringUtils.isBlank(serverAddress)) {
            throw new CISClientBaseRuntimeException("jira.serverAddress.null");
        }
        if (StringUtils.isBlank(adminUserName)) {
            throw new AdminUserNameBlankException("jira.adminUserName.null");
        }
        if (StringUtils.isBlank(adminPassword)) {
            throw new AdminPasswordBlankException("jira.adminPassword.null");
        }
        return true;
    }

}
