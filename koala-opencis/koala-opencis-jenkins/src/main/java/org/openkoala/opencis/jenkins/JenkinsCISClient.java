package org.openkoala.opencis.jenkins;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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

/**
 * Jenkins CIS客户端
 *
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:35:24 AM
 */
public class JenkinsCISClient implements CISClient {

    private static final String CONFIG_PATH = "ci/jenkins/config.xml";

    public static final String CREATE_ITEM_API = "/createItem?name=";

    private URL jenkinsUrl;

    private CISAuthentication cisAuthentication;

    private static final int BUFFER_SIZE = 2048;

    private static Logger logger = Logger.getLogger(JenkinsCISClient.class);


    public JenkinsCISClient(URL jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
    }

    @Override
    public void createProject(Project project) {
        HttpContext context = authenticationAndGetContext();

        AbstractHttpClient httpClient = new DefaultHttpClient();
        HttpPost createProjectPost = null;
        try {

            createProjectPost = new HttpPost(jenkinsUrl.toString()
                    + CREATE_ITEM_API + URLEncoder.encode(project.getArtifactId(), "UTF-8"));
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
        } catch (UnsupportedEncodingException e) {
            throw new CreateJobFailureException(MessageFormat.format("{0} is UnsupportedEncoding", project.getArtifactId()));
        } catch (Exception e) {
            logger.error("Create job failure:", e);
            throw new CreateJobFailureException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }


    }


    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        HttpContext context = authenticationAndGetContext();
        AbstractHttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(jenkinsUrl.toString() + "/scriptText");
            List<NameValuePair> result = new ArrayList<NameValuePair>();
            String script = readFileAsString(this.getClass().getClassLoader().getResource("ci/jenkins/scripts/createUser.groovy").getFile());
            result.add(new BasicNameValuePair("script", MessageFormat.format(script, "\""  + developer.getId() + "\"")));
            httpPost.setEntity(new UrlEncodedFormEntity(result));
            HttpResponse response = httpClient.execute(httpPost, context);
            String responseText = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && StringUtils.isNotBlank(responseText)) {
                logger.info("Create user account success.");
            } else {
                throw new JenkinsCreateUserFailureException(responseText);
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

    private String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    public void confirmRemoveJob(String jobName) {
        HttpContext context = authenticationAndGetContext();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(jenkinsUrl.toString() + "/job/" + jobName + "/doDelete");
        try {
            HttpResponse response = httpClient.execute(httpPost, context);
            //jenkins返回的是302的码
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_MOVED_TEMPORARILY) {
                throw new RemoveJobFailureException("Remove job failure.");
            }
        } catch (Exception e) {
            throw new RemoveJobFailureException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private List<NameValuePair> createDeveloperNameValuePair(Developer developer) {
        List<NameValuePair> result = new ArrayList<NameValuePair>();
        result.add(new BasicNameValuePair("username", developer.getName()));
        result.add(new BasicNameValuePair("password1", developer.getName()));
        result.add(new BasicNameValuePair("password2", developer.getName()));
        result.add(new BasicNameValuePair("fullname", developer.getName()));
        result.add(new BasicNameValuePair("email", developer.getEmail()));
        return result;
    }


    @Override
    public void createRoleIfNecessary(Project project, String roleName) {
        GlobalPermission permission = new GlobalPermission(roleName);
        permission.save();
        reloadConfigToMemory();
    }

    @Override
    public void assignUserToRole(Project project, String userId, String role) {
        ProjectPermission permission = new ProjectPermission(userId, project.getArtifactId());
        permission.save();
        reloadConfigToMemory();
    }

    private void reloadConfigToMemory() {
        HttpContext context = authenticationAndGetContext();
        HttpClient httpClient = new DefaultHttpClient();
        try {

            String requestUrl = jenkinsUrl.toString() + "/reload";
            HttpPost httpPost = new HttpPost(requestUrl);

            HttpResponse response = httpClient.execute(httpPost, context);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                logger.info("jenkins config reload success");
                return;
            }
            httpPost.abort();
            throw new JenkinsReloadConfigException("reload the config failure!");
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error(e.getMessage() + " may be the config file is not exist!");
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
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

    @Override
    public void assignUsersToRole(Project project, List<String> userName,
                                  String role) {
        if (null == userName || userName.size() == 0) {
            return;
        }
        for (String each : userName) {
            assignUserToRole(project, each, role);
        }
    }

    public void addAuthentication(CISAuthentication cisAuthentication) {
        this.cisAuthentication = cisAuthentication;
        this.cisAuthentication.setAppURL(jenkinsUrl);
    }

}
