package org.openkoala.opencis.jenkins;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

/**
 * Jenkins CIS客户端
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:35:24 AM
 */
public class JenkinsCISClient implements CISClient {
	
	private static final int BUFFER_SIZE = 2048;

	private String jenkinsHost;
	
	private static Logger logger = Logger.getLogger(JenkinsCISClient.class);

	@Override
	public void createProject(Project project) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(getCreateJobUrl(project.getArtifactId()));
		try {
			StringEntity entity = new StringEntity(getConfigFileContent(), "UTF-8");
			httpPost.addHeader("Content-Type", "application/xml");
			httpPost.setEntity(entity);
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
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		HttpPost post = new HttpPost(getCreateAccountUrl());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", developer.getName()));
		params.add(new BasicNameValuePair("password1", developer.getName()));
		params.add(new BasicNameValuePair("password2", developer.getName()));
		params.add(new BasicNameValuePair("fullname", developer.getName()));
		params.add(new BasicNameValuePair("email", developer.getEmail()));
		try {
			post.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				logger.info("Create user account success.");
			} else {
				logger.info("Create user account failure.");
			}
			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			logger.error("Create user account occurs error.", e);
		}
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
	
	private String getCreateJobUrl(String jobName) {
		return new StringBuilder(jenkinsHost).append("/createItem?name=").append(jobName).toString();
	}
	
	private String getCreateAccountUrl() {
		return new StringBuilder(jenkinsHost).append("/securityRealm/createAccount").toString();
	}
	
	/**
	 * 获取Jenkins配置文件的内容 
	 * @return
	 */
	private String getConfigFileContent() {
		InputStream in = null;
		StringBuilder result = new StringBuilder();
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("ci/jenkins/config.xml");
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

	public String getJenkinsHost() {
		return jenkinsHost;
	}

	public void setJenkinsHost(String jenkinsHost) {
		this.jenkinsHost = jenkinsHost;
	}
	
}
