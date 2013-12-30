package org.openkoala.opencis.jenkins;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.JenkinsServerConfigurationNullException;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.authentication.CISAuthentication;
import org.openkoala.opencis.pojo.JenkinsServerConfiguration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:56:28 AM
 */
@Ignore
public class JenkinsCISClientTest {


    private static URL JENKINS_URL;

    private static URL CAS_URL;

    private JenkinsCISClient jenkinsCISClient;

    static {
        try {
            JENKINS_URL = new URL("http", "10.108.1.138", 8080, "/jenkins");
            CAS_URL = new URL("http", "10.108.1.138", 8080, "/cas/v1/tickets/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws Exception {
        jenkinsCISClient = new JenkinsCISClient(JENKINS_URL);
        HttpCASAuthentication authentication = new HttpCASAuthentication(CAS_URL, "admin", "admin");
        authentication.setJenkinsAuthenticationUrl(JENKINS_URL);
        jenkinsCISClient.addAuthentication(authentication);
    }

    @Test
    public void testOperationProject() throws MalformedURLException {


        for (int i = 135; i < 140; i++) {
            Project project = new Project();
            project.setProjectName("projectName" + new Random(2100).nextInt());
            String jobName = "ArtifactIdasdfasdf" + i;
            project.setArtifactId(jobName);
            jenkinsCISClient.createProject(project);

            jenkinsCISClient.confirmRemoveJob(jobName);
        }


    }

    @Test
    public void testCreateUserIfNecessary() {
        Project project = new Project();
        project.setArtifactId("ArtifactIdasdfasdfssdd");
        Developer developer = new Developer();
        developer.setName("www");
        developer.setEmail("admin@gmail.com");
        jenkinsCISClient.createProject(project);




    }


}
