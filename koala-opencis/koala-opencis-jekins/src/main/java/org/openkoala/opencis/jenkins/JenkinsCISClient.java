package org.openkoala.opencis.jenkins;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

/**
 * Jenkis CIS客户端
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:35:24 AM
 */
public class JenkinsCISClient implements CISClient {
	
	private String jenkinsHost;
	
	private static Logger logger = Logger.getLogger(JenkinsCISClient.class);

	@Override
	public void createProject(Project project) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(getCreateJobUrl());
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", project.getArtifactId()));
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				logger.info("Create job successful.");
			} else {
				throw new CreateJobFailureException(response.getStatusLine().toString());
			}
			
		} catch (ClientProtocolException e) {
			logger.error("Create job failure:", e);
		} catch (UnsupportedEncodingException e) {
			logger.error("Create job failure:", e);
		} catch (IOException e) {
			logger.error("Create job failure:", e);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	@Override
	public void createUserIfNecessary(Project project, Developer developer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createRoleIfNessceary(Project project, String roleName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void assignUserToRole(Project project, String usrId, String role) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canConnect() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private String getCreateJobUrl() {
		return jenkinsHost + "/createItem";
	}

	public String getJenkinsHost() {
		return jenkinsHost;
	}

	public void setJenkinsHost(String jenkinsHost) {
		this.jenkinsHost = jenkinsHost;
	}

}
