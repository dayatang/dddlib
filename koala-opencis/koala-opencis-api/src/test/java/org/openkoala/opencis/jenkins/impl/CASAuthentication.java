package org.openkoala.opencis.jenkins.impl;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openkoala.opencis.api.AuthenticationResult;
import org.openkoala.opencis.api.AuthenticationStrategy;
import sun.net.www.http.HttpClient;

/**
 * User: zjzhai
 * Date: 1/14/14
 * Time: 9:25 AM
 */
public class CASAuthentication implements AuthenticationStrategy {

    private String jenkinsURL = null;

    private String username = null;

    private String password = null;

    private CloseableHttpClient httpClient;


    @Override
    public AuthenticationResult authenticate() {
        AuthenticationResult authenticationResult = new AuthenticationResult();
        httpClient = HttpClientBuilder.create().build();
        authenticationResult.setContext(httpClient);
        return authenticationResult;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String getJenkinsURL() {
        return jenkinsURL;
    }

    public void setJenkinsURL(String jenkinsURL) {
        this.jenkinsURL = jenkinsURL;
    }
}
