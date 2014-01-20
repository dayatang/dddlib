package org.openkoala.opencis.sonar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.SonarUserExistException;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.sonar.wsclient.internal.HttpRequestFactory;
import org.sonar.wsclient.permissions.PermissionParameters;
import org.sonar.wsclient.permissions.internal.DefaultPermissionClient;
import org.sonar.wsclient.user.UserParameters;
import org.sonar.wsclient.user.internal.DefaultUserClient;

public class SonarCISClient implements CISClient {

    protected static final String SONAN_PERMISSION_USER = "user";

    private SonarConnectConfig connectConfig;

    private SonarCISClient() {
    }

    public SonarCISClient(SonarConnectConfig connectConfig) {
        this.connectConfig = connectConfig;
    }

    @Override
    public void close() {
        // do nothing
    }


    @Override
    public void createProject(Project project) {
        HttpHost targetHost = new HttpHost(connectConfig.getHost(), connectConfig.getPort(), connectConfig.getProtocol());
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials(connectConfig.getUsername(), connectConfig.getPassword()));
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
        HttpPost httpPost = new HttpPost("/api/projects/create");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("key", "value1"));
        formparams.add(new BasicNameValuePair("name", "value2"));
        System.out.println(httpPost.getURI().toString());
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(entity);
            response = httpclient.execute(targetHost, httpPost);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("sonar.authenticateFailure", e);
        } finally {
            try {
                response.close();
                httpclient.close();
            } catch (IOException e) {
                throw new CISClientBaseRuntimeException("sonar.httpClientCloseFailure", e);
            }
        }
    }

    @Override
    public void removeProject(Project project) {


    }

    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        developer.validate();
        try {
            DefaultUserClient client = new DefaultUserClient(createHttpRequestFactory());
            UserParameters params = UserParameters.create().login(developer.getName())
                    .password(developer.getPassword()).passwordConfirmation(developer.getPassword())
                    .name(developer.getFullName()).email(developer.getEmail());
            client.create(params);
        } catch (Exception e) {
            throw new SonarUserExistException();
        }
    }

    @Override
    public void removeUser(Project project, Developer developer) {
    }

    @Override
    public void createRoleIfNecessary(Project project, String roleName) {

    }

    @Override
    public void assignUsersToRole(Project project, String role, Developer... developers) {
        project.validate();
        for (Developer each : developers) {
            each.validate();
            String component = project.getGroupId() + ":" + project.getArtifactId();
            try {
                DefaultPermissionClient client = new DefaultPermissionClient(createHttpRequestFactory());
                PermissionParameters params = PermissionParameters.create().user(each.getName()).component(component).permission(SONAN_PERMISSION_USER);
                client.addPermission(params);
            } catch (Exception e) {
                throw new SonarUserExistException("用户名不存在或项目角色信息有误");
            }
        }
    }

    @Override
    public boolean authenticate() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpHost targetHost = new HttpHost(connectConfig.getHost(), connectConfig.getPort(), connectConfig.getProtocol());
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials(connectConfig.getUsername(), connectConfig.getPassword()));
        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        HttpGet httpget = new HttpGet("/api/authentication/validate");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(
                    targetHost, httpget, context);
            System.out.println(response.getStatusLine().getStatusCode());
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
                    && ("{\"valid\":true}".equals(EntityUtils.toString(response.getEntity())));
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("sonar.authenticateFailure");
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                throw new CISClientBaseRuntimeException("sonar.httpClientCloseFailure");
            }
        }
    }


    protected HttpRequestFactory createHttpRequestFactory() {
        HttpRequestFactory requestFactory = new HttpRequestFactory(connectConfig.getAddress());
        requestFactory.setLogin(connectConfig.getUsername());
        requestFactory.setPassword(connectConfig.getPassword());
        return requestFactory;
    }


}
