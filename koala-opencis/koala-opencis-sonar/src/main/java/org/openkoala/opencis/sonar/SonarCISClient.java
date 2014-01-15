package org.openkoala.opencis.sonar;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openkoala.opencis.PropertyIllegalException;
import org.openkoala.opencis.SonarServerConfigurationNullException;
import org.openkoala.opencis.SonarServiceNotExistException;
import org.openkoala.opencis.SonarUserExistException;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.pojo.SonarServerConfiguration;
import org.sonar.wsclient.internal.HttpRequestFactory;
import org.sonar.wsclient.permissions.PermissionParameters;
import org.sonar.wsclient.permissions.internal.DefaultPermissionClient;
import org.sonar.wsclient.user.UserParameters;
import org.sonar.wsclient.user.internal.DefaultUserClient;

public class SonarCISClient implements CISClient {

    protected static final String SONAN_PERMISSION_USER = "user";

    private SonarServerConfiguration sonarServerConfiguration;

    private String errors;

    public SonarCISClient(SonarServerConfiguration sonarServerConfiguration) {
        this.sonarServerConfiguration = sonarServerConfiguration;
        checkSonarServerNotNull();
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getErrors() {
        return errors;
    }

    @Override
    public boolean createProject(Project project) {
        //由maven的pom文件指定
        // do nothing
        return true;
    }

    @Override
    public boolean createUserIfNecessary(Project project, Developer developer) {
        verifyDeveloperLegal(developer);
        try {
            DefaultUserClient client = new DefaultUserClient(createHttpRequestFactory());
            UserParameters params = UserParameters.create().login(developer.getId())
                    .password(developer.getId()).passwordConfirmation(developer.getId())
                    .name(developer.getName()).email(developer.getEmail());
            client.create(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SonarUserExistException();
        }
        return true;
    }

    private void verifyDeveloperLegal(Developer developer) {
        if (developer == null || StringUtils.isBlank(developer.getId())) {
            throw new PropertyIllegalException("developer must be not null");
        }
        if (developer.getId().length() < 2) {
            throw new PropertyIllegalException("developer's length le 2");
        }
        if (StringUtils.isBlank(developer.getId()) || developer.getId().length() < 4) {
            throw new PropertyIllegalException("password is null ");
        }
        if (StringUtils.isBlank(developer.getName())) {
            throw new PropertyIllegalException("名称为空");
        }
    }

    @Override
    public boolean createRoleIfNecessary(Project project, String roleName) {
        return true;
    }

    @Override
    public boolean assignUserToRole(Project project, String usrId, String role) {
        verifyProjectAndUserIdLegal(project, usrId);
        String component = project.getGroupId() + ":" + project.getArtifactId();
        try {
            DefaultPermissionClient client = new DefaultPermissionClient(createHttpRequestFactory());
            PermissionParameters params = PermissionParameters.create().user(usrId).component(component).permission(SONAN_PERMISSION_USER);
            client.addPermission(params);
        } catch (Exception e) {
            throw new SonarUserExistException("用户名不存在或项目角色信息有误");
        }
        return true;
    }

    protected void verifyProjectAndUserIdLegal(Project project, String usrId) {
        if (usrId == null) {
            throw new PropertyIllegalException("用户名为空");
        }
        if (project == null) {
            throw new PropertyIllegalException("项目对象为空");
        }
        if (StringUtils.isBlank(project.getArtifactId())) {
            throw new PropertyIllegalException("ArtifactId为空");
        }
        if (StringUtils.isBlank(project.getGroupId())) {
            throw new PropertyIllegalException("GroupId为空");
        }
    }

    public boolean canConnect() {
        return connectSonarServer();
    }

    public void setSonarServerConfiguration(SonarServerConfiguration sonarServerConfiguration) {
        this.sonarServerConfiguration = sonarServerConfiguration;
    }

    private void checkSonarServerNotNull() {
        if (sonarServerConfiguration == null) {
            throw new SonarServerConfigurationNullException();
        }
    }

    private boolean connectSonarServer() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(sonarServerConfiguration.getServerAddress() + "/sessions/new");
        try {
            HttpResponse response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return true;
            } else {
                throw new SonarServiceNotExistException();
            }
        } catch (Exception e) {
            throw new SonarServiceNotExistException();
        }
    }

    protected HttpRequestFactory createHttpRequestFactory() {
        canConnect();
        HttpRequestFactory requestFactory = new HttpRequestFactory(sonarServerConfiguration.getServerAddress());
        requestFactory.setLogin(sonarServerConfiguration.getUsername());
        requestFactory.setPassword(sonarServerConfiguration.getPassword());
        return requestFactory;
    }

    @Override
    public boolean assignUsersToRole(Project project, List<String> userName,
                                     String role) {
        // do nothing
        return true;

    }

}
