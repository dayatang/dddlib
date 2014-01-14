package org.openkoala.opencis.jenkins.impl;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import java.io.IOException;
import java.util.List;

/**
 * User: zjzhai
 * Date: 1/14/14
 * Time: 9:31 AM
 */
public class JenkinsCISClient implements CISClient {

    private CloseableHttpClient client;

    private final String jenkinsUrl;

    private String error;

    public JenkinsCISClient(String jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
    }


    @Override
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getErrors() {
        return error;
    }

    @Override
    public boolean createProject(Project project) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean createUserIfNecessary(Project project, Developer developer) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean createRoleIfNecessary(Project project, String roleName) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean assignUserToRole(Project project, String userId, String role) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean assignUsersToRole(Project project, List<String> userName, String role) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
