package org.openkoala.opencis.sonar;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.openkoala.opencis.PropertyIllegalException;
import org.openkoala.opencis.SonarServerConfigurationNullException;
import org.openkoala.opencis.SonarServiceNotExistException;
import org.openkoala.opencis.SonarUserExistException;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.sonar.wsclient.SonarClient;
import org.sonar.wsclient.internal.HttpRequestFactory;
import org.sonar.wsclient.permissions.PermissionParameters;
import org.sonar.wsclient.permissions.internal.DefaultPermissionClient;
import org.sonar.wsclient.system.SystemClient;
import org.sonar.wsclient.user.UserParameters;
import org.sonar.wsclient.user.internal.DefaultUserClient;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SonarCISClient implements CISClient {

    protected static final String SONAN_PERMISSION_USER = "user";

    private SonarServerConfiguration sonarServerConfiguration;

    public SonarCISClient(SonarServerConfiguration sonarServerConfiguration) {
        this.sonarServerConfiguration = sonarServerConfiguration;
        checkSonarServerNotNull();
    }

    @Override
    public void close() {

    }


    @Override
    public void createProject(Project project) {
        //由maven的pom文件指定
        // do nothing
    }

    @Override
    public void removeProject(Project project) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        verifyDeveloperLegal(developer);
        try {
            DefaultUserClient client = new DefaultUserClient(createHttpRequestFactory());
            UserParameters params = UserParameters.create().login(developer.getName())
                    .password(developer.getPassword()).passwordConfirmation(developer.getPassword())
                    .name(developer.getFullName()).email(developer.getEmail());
            client.create(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SonarUserExistException();
        }
    }

    @Override
    public void removeUser(Project project, Developer developer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void verifyDeveloperLegal(Developer developer) {
        developer.validate();
    }

    @Override
    public void createRoleIfNecessary(Project project, String roleName) {

    }

    @Override
    public void assignUsersToRole(Project project, String role, Developer... developers) {
        for (Developer each : developers) {
            project.validate();
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

    // TODO
    @Override
    public boolean authenticate() {
        String username = "sss";
        String password = "xxx";
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
        HttpRequest request = new HttpGet();
        try {
            request.addHeader(new BasicScheme().authenticate(creds, request));
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        return false;
    }


    public void setSonarServerConfiguration(SonarServerConfiguration sonarServerConfiguration) {
        this.sonarServerConfiguration = sonarServerConfiguration;
    }

    private void checkSonarServerNotNull() {
        if (sonarServerConfiguration == null) {
            throw new SonarServerConfigurationNullException();
        }
    }


    protected HttpRequestFactory createHttpRequestFactory() {
        HttpRequestFactory requestFactory = new HttpRequestFactory(sonarServerConfiguration.getServerAddress());
        requestFactory.setLogin(sonarServerConfiguration.getUsername());
        requestFactory.setPassword(sonarServerConfiguration.getPassword());
        return requestFactory;
    }


}
