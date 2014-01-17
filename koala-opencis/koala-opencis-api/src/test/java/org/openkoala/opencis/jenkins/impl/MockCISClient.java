package org.openkoala.opencis.jenkins.impl;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openkoala.opencis.api.*;

import java.io.IOException;
import java.util.List;

/**
 * User: zjzhai
 * Date: 1/14/14
 * Time: 9:31 AM
 */
public class MockCISClient implements CISClient {

    private CloseableHttpClient client;

    private String jenkinsUrl;

    AuthenticationStrategy authenticationStrategy;

    public MockCISClient(String jenkinsUrl, AuthenticationStrategy authenticationStrategy) {
        this.jenkinsUrl = jenkinsUrl;
        this.authenticationStrategy = authenticationStrategy;
    }


    @Override
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException("close failure");
        }
    }

    @Override
    public void createProject(Project project) {
    }

    @Override
    public void removeProject(Project project) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
    }

    @Override
    public void removeUser(Project project, Developer developer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createRoleIfNecessary(Project project, String roleName) {
    }

    @Override
    public void assignUsersToRole(Project project, String role, String... usersId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public boolean assignUsersToRole(Project project, List<String> userName, String role) {
        return false;
    }

    @Override
    public boolean authenticate() {
        AuthenticationResult result = authenticationStrategy.authenticate();
        client = (CloseableHttpClient) result.getContext();
        return result.isSuccess();
    }


}
