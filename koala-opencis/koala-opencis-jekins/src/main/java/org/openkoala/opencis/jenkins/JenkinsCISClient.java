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
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.openkoala.opencis.JenkinsAdminLoginException;
import org.openkoala.opencis.JenkinsCreateUserFailureException;
import org.openkoala.opencis.JenkinsServerConfigurationNullException;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.domain.GlobalPermission;
import org.openkoala.opencis.domain.ProjectPermission;
import org.openkoala.opencis.http.HttpInvoker;
import org.openkoala.opencis.pojo.JenkinsServerConfiguration;

/**
 * Jenkins CIS客户端
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:35:24 AM
 */
public class JenkinsCISClient implements CISClient {
	
	private static final int BUFFER_SIZE = 2048;
	
	private static Logger logger = Logger.getLogger(JenkinsCISClient.class);
	
	private JenkinsServerConfiguration jenkinsServerConfiguration;

	public JenkinsCISClient(JenkinsServerConfiguration jenkinsServerConfiguration) {
		this.jenkinsServerConfiguration = jenkinsServerConfiguration;
		checkJenkinsServerConfigurationNotNull();
	}
	
	@Override
	public void createProject(Project project) {
		HttpInvoker httpInvoker = new HttpInvoker(getCreateJobUrl(project.getArtifactId()));
		HttpPost httpPost = httpInvoker.getHttpPost();
		
		try {
			StringEntity entity = new StringEntity(getConfigFileContent(), "UTF-8");
			httpPost.addHeader("Content-Type", "application/xml");
			httpPost.setEntity(entity);
			HttpResponse response = httpInvoker.execute();
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
			httpInvoker.shutdown();
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
		HttpInvoker httpInvoker = new HttpInvoker(getCreateAccountUrl(), params);
		
		try {
			HttpResponse response = httpInvoker.execute();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				logger.info("Create user account success.");
			} else {
				throw new JenkinsCreateUserFailureException();
			}
		} catch (Exception e) {
			logger.error("Create user account occurs error.", e);
		} finally {
			httpInvoker.shutdown();
		}
	}

	@Override
	public void createRoleIfNessceary(Project project, String roleName) {
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
		CookieStore cookieStore = getAdminLoginJenkinsCookie();
		try {
			String requestUrl = jenkinsServerConfiguration.getServerAddress() + "/reload";
			HttpInvoker httpInvoker = new HttpInvoker(requestUrl);
			((AbstractHttpClient) httpInvoker.getHttpClient()).setCookieStore(cookieStore);
			httpInvoker.execute();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private CookieStore getAdminLoginJenkinsCookie() {
		String requestUrl = jenkinsServerConfiguration.getServerAddress() + "/j_acegi_security_check";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("j_username", jenkinsServerConfiguration.getUsername()));
		params.add(new BasicNameValuePair("j_password", jenkinsServerConfiguration.getPassword()));
		HttpInvoker httpInvoker = new HttpInvoker(requestUrl, params);
		try {
			HttpResponse httpResponse = httpInvoker.execute();
			if (HttpStatus.SC_MOVED_TEMPORARILY == httpResponse.getStatusLine().getStatusCode()) {
				return ((AbstractHttpClient) httpInvoker.getHttpClient()).getCookieStore();
			}
			throw new JenkinsAdminLoginException();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean canConnect() {
		return false;
	}
	
	private String getCreateJobUrl(String jobName) {
		return new StringBuilder(jenkinsServerConfiguration.getServerAddress()).append("/createItem?name=").append(jobName).toString();
	}
	
	private String getCreateAccountUrl() {
		return new StringBuilder(jenkinsServerConfiguration.getServerAddress()).append("/securityRealm/createAccount").toString();
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
	
	private void checkJenkinsServerConfigurationNotNull() {
		if (jenkinsServerConfiguration == null) {
			throw new JenkinsServerConfigurationNullException();
		}
	}
}
