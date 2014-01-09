package org.openkoala.opencis.jenkins.configureApi;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.jenkins.util.UrlUtil;

/**
 * User: zjzhai
 * Date: 1/8/14
 * Time: 3:24 PM
 */
public abstract class UserCreator {
    private String jenkinsUrl;

    private String username;

    private String password;

    private String fullname;

    private String email;

    private String error;

    private UserCreator() {
    }

    public UserCreator(String jenkinsUrl) {
        this.jenkinsUrl = UrlUtil.removeEndIfExists(jenkinsUrl, "/");
    }

    public abstract boolean createUser(Developer developer);

    protected void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public String getJenkinsUrl() {
        return jenkinsUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }
}
