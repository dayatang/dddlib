package org.openkoala.opencis.jenkins;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.openkoala.opencis.*;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.authentication.CISAuthentication;
import org.openkoala.opencis.domain.GlobalPermission;
import org.openkoala.opencis.domain.ProjectPermission;
import org.openkoala.opencis.http.HttpInvoker;
import org.openkoala.opencis.pojo.JenkinsServerConfiguration;

/**
 * Jenkins CIS客户端
 *
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:35:24 AM
 */
public class JenkinsCISClient implements CISClient {

    private static final String CONFIG_PATH = "ci/jenkins/config.xml";

    public static final String CREATE_ITEM_API = "/createItem";

    public static final String CREATE_ITEM_API_PARAM = "name";

    private URL jenkinsUrl;

    private CISAuthentication cisAuthentication;

    private static final int BUFFER_SIZE = 2048;

    private static Logger logger = Logger.getLogger(JenkinsCISClient.class);

    private JenkinsServerConfiguration jenkinsServerConfiguration;

    public JenkinsCISClient(JenkinsServerConfiguration jenkinsServerConfiguration) {
        this.jenkinsServerConfiguration = jenkinsServerConfiguration;
        checkJenkinsServerConfigurationNotNull();
    }

    public JenkinsCISClient(URL jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
    }

    @Override
    public void createProject(Project project) {

        HttpContext context = authenticationAndGetContext();

        AbstractHttpClient httpClient = new DefaultHttpClient();

        HttpPost createProjectPost = new HttpPost(jenkinsUrl.toString() + CREATE_ITEM_API + "?name=" + project.getArtifactId());

        BasicHttpParams paramsName = new BasicHttpParams();

        paramsName.setParameter(CREATE_ITEM_API_PARAM, project.getArtifactId());
        try {
            StringEntity entity = new StringEntity(getConfigFileContent(), "UTF-8");
            createProjectPost.addHeader("Content-Type", "application/xml");
            createProjectPost.setEntity(entity);
            HttpResponse jenkinsResponse = httpClient.execute(createProjectPost, context);


            if (jenkinsResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                logger.info("Create job successful.");
            } else {
                throw new CreateJobFailureException(EntityUtils.toString(jenkinsResponse.getEntity()));
            }
            createProjectPost.abort();
        } catch (Exception e) {
            logger.error("Create job failure:", e);
            throw new CreateJobFailureException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }


    }


    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", developer.getName()));
        params.add(new BasicNameValuePair("password1", developer.getName()));
        params.add(new BasicNameValuePair("password2", developer.getName()));
        params.add(new BasicNameValuePair("fullname", developer.getName()));
        params.add(new BasicNameValuePair("email", developer.getEmail()));

        HttpContext context = authenticationAndGetContext();
        AbstractHttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(jenkinsUrl.toString() + "");

            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpClient.execute(httpPost, context);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                logger.info("Create user account success.");
            } else {
                throw new JenkinsCreateUserFailureException();
            }
        } catch (UnsupportedEncodingException e) {
            throw new JenkinsCreateUserFailureException(e);
        } catch (ClientProtocolException e) {
            throw new JenkinsCreateUserFailureException(e);
        } catch (IOException e) {
            throw new JenkinsCreateUserFailureException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

    }

    public void confirmRemoveJob(String jobName) {
        HttpContext context = authenticationAndGetContext();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(jenkinsUrl.toString() + "/job/" + jobName + "/doDelete");
        try {
            HttpResponse response = httpClient.execute(httpPost, context);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RemoveJobFailureException("Remove job failure.");
            }
        } catch (Exception e) {
            throw new RemoveJobFailureException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }


    @Override
    public void createRoleIfNecessary(Project project, String roleName) {
        GlobalPermission permission = new GlobalPermission(roleName);
        permission.save();
        reloadConfigToMemory();
    }

    @Override
    public void assignUserToRole(Project project, String usrId, String role) {
        ProjectPermission permission = new ProjectPermission(usrId, project.getArtifactId());
        permission.save();
        reloadConfigToMemory();
    }

    private void reloadConfigToMemory() {
        CookieStore cookieStore = null;
        try {
            String requestUrl = jenkinsUrl.toString() + "/reload";
            HttpInvoker httpInvoker = new HttpInvoker(requestUrl);
            ((AbstractHttpClient) httpInvoker.getHttpClient()).setCookieStore(cookieStore);
            httpInvoker.execute();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean canConnect() {
        return false;
    }


    private HttpContext authenticationAndGetContext() {
        if (cisAuthentication != null && !cisAuthentication.authentication()) {
            throw new AuthenticationException("jenkins authentication failure!");
        }
        HttpContext context = new BasicHttpContext();
        if (cisAuthentication != null) {
            context = cisAuthentication.getContext();
        }
        return context;
    }

    private String getCreateAccountUrl() {
        return new StringBuilder(jenkinsServerConfiguration.getServerAddress()).append("/securityRealm/createAccount").toString();
    }

    /**
     * 获取Jenkins配置文件的内容
     *
     * @return
     */
    private String getConfigFileContent() {
        InputStream in = null;
        StringBuilder result = new StringBuilder();
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_PATH);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                result.append(new String(buffer, 0, len));
            }
        } catch (Exception e) {
            logger.error("Ocour error while read file:", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("Could not release IO:", e);
            }
        }
        return result.toString();
    }

    private void checkJenkinsServerConfigurationNotNull() {
        if (jenkinsServerConfiguration == null) {
            throw new JenkinsServerConfigurationNullException();
        }
    }

    @Override
    public void assignUsersToRole(Project project, List<String> userName,
                                  String role) {
        // TODO Auto-generated method stub
    }

    public void addAuthentication(CISAuthentication cisAuthentication) {
        this.cisAuthentication = cisAuthentication;
        this.cisAuthentication.setAppURL(jenkinsUrl);
    }

}
