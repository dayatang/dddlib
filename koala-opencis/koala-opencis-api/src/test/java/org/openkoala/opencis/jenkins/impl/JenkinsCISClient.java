package org.openkoala.opencis.jenkins.impl;

import org.openkoala.opencis.api.AuthenticationStrategy;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import java.util.List;

/**
 * User: zjzhai
 * Date: 1/14/14
 * Time: 9:31 AM
 */
public class JenkinsCISClient implements CISClient {

    private AuthenticationStrategy authentication;

    private String error;

    private HttpClient httpClient;

    public JenkinsCISClient(AuthenticationStrategy authenticationStrategy) {
        authenticattion = authenticationStrategy;
    }

    @Override
    public boolean authenticate() {
        boolean result = authentication.authenticate();
        error = authentication.getErrors();
        httpClient = (HttpClient) authentication.getContext();
        return result;
    }

    @Override
    public void close() {
        httpClient.manage().shutdown();
    }

    @Override
    public String getErrors() {
        return error;
    }

    @Override
    public boolean createProject(Project project) {

        // httpClient do something

        return true;
    }

    @Override
    public boolean createUserIfNecessary(Project project, Developer developer) {

        // httpClient do something

        return true;
    }

    @Override
    public boolean createRoleIfNecessary(Project project, String roleName) {

        // httpClient do something

        return true;

    }

    @Override
    public boolean assignUserToRole(Project project, String userId, String role) {

        // httpClient do something

        return true;
    }

    @Override
    public boolean assignUsersToRole(Project project, List<String> userName, String role) {

        // httpClient do something

        return true;
    }
}
