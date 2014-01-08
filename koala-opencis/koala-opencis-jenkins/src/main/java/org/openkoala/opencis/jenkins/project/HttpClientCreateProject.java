package org.openkoala.opencis.jenkins.project;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.openkoala.opencis.CreateJobFailureException;
import org.openkoala.opencis.api.Project;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 5:40 PM
 */
public class HttpClientCreateProject implements ProjectCreateStrategy {

    private static final int BUFFER_SIZE = 2048;

    private static final String CONFIG_PATH = "ci/jenkins/config.xml";


    private String createProjectUrl;

    private static Logger logger = Logger.getLogger(HttpClientCreateProject.class);


    public HttpClientCreateProject(String createProjectUrl) {
        this.createProjectUrl = createProjectUrl;
    }

    @Override
    public void create(Project project) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost createProjectPost = null;
        try {

            createProjectPost = new HttpPost(createProjectUrl + URLEncoder.encode(project.getArtifactId(), "UTF-8"));
            StringEntity entity = new StringEntity(getConfigFileContent(), "UTF-8");
            createProjectPost.addHeader("Content-Type", "application/xml");
            createProjectPost.setEntity(entity);
            HttpResponse jenkinsResponse = httpClient.execute(createProjectPost);
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
}
