package org.openkoala.opencis.hudson;

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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.hudson.util.HttpClientUtils;

/**
 * Hudson的CISClient实现类
 * 
 * @author mluo
 * 
 */
public class HudsonCISClient implements CISClient {
	
	private static final Logger LOGGER = Logger.getLogger(HudsonCISClient.class);

	private static final String HUDSON_URL = "http://localhost:8888/hudson";
	
	@Override
	public void createProject(Project project) {
		
		HudsonConfiguration configuration = new HudsonConfiguration(HUDSON_URL, project.getArtifactId());
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(configuration.getCreateJobUrl());

		StringEntity entity = null;
		try {
			entity = new StringEntity(configuration.getConfigXml(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		httpPost.addHeader("Content-Type", "application/xml");
		httpPost.setEntity(entity);

		try {
			HttpResponse response = httpClient.execute(httpPost);
			System.out.println(response.getStatusLine().getStatusCode());
			System.out.println(response.getEntity().getContent().toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void createUserIfNecessary(Project project, Developer developer) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		HttpPost post = new HttpPost("http://localhost:8888/hudson/securityRealm/createAccount");
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
				LOGGER.info("Create user account success.");
			} else {
				LOGGER.info("Create user account failure.");
			}
			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			LOGGER.error("Create user account occurs error.", e);
		}
	}

	@Override
	public void createRoleIfNessceary(Project project, String roleName) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		HttpPost post = new HttpPost("http://127.0.0.1:8080/hudson/securityManager/configSubmit");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("use_security", "on"));
		params.add(new BasicNameValuePair("stapler-class", "hudson.markup.RawHtmlMarkupFormatter"));
		params.add(new BasicNameValuePair("stapler-class", "hudson.security.LegacySecurityRealm"));
		params.add(new BasicNameValuePair("stapler-class", "hudson.security.LDAPSecurityRealm"));
		params.add(new BasicNameValuePair("ldap.server", ""));
		params.add(new BasicNameValuePair("ldap.rootDN", ""));
		params.add(new BasicNameValuePair("ldap.userSearchBase", ""));
		params.add(new BasicNameValuePair("ldap.userSearch", ""));
		params.add(new BasicNameValuePair("ldap.groupSearchBase", ""));
		params.add(new BasicNameValuePair("ldap.managerDN", ""));
		params.add(new BasicNameValuePair("ldap.managerPassword", ""));
		params.add(new BasicNameValuePair("realm", "2"));
		params.add(new BasicNameValuePair("stapler-class", "hudson.security.HudsonPrivateSecurityRealm"));
		params.add(new BasicNameValuePair("privateRealm.allowsSignup", "on"));
		params.add(new BasicNameValuePair("privateRealm.notifyUser", "on"));
		params.add(new BasicNameValuePair("stapler-class", "edu.hawaii.its.hudson.security.Cas1SecurityRealm"));
		params.add(new BasicNameValuePair("_.casServerUrl", ""));
		params.add(new BasicNameValuePair("_.hudsonHostName", "127.0.0.1:8888"));
		params.add(new BasicNameValuePair("_.rolesValidationScript", ""));
		params.add(new BasicNameValuePair("_.testValidationResponse", ""));
		params.add(new BasicNameValuePair("stapler-class", "hudson.security.FullControlOnceLoggedInAuthorizationStrategy"));
		params.add(new BasicNameValuePair("stapler-class", "hudson.security.GlobalMatrixAuthorizationStrategy"));
		params.add(new BasicNameValuePair("stapler-class", "hudson.security.AuthorizationStrategy$Unsecured"));
		params.add(new BasicNameValuePair("authorization", "3"));
		params.add(new BasicNameValuePair("stapler-class", "hudson.security.ProjectMatrixAuthorizationStrategy"));
		params.add(new BasicNameValuePair("[hudson.model.Hudson.Administer]", "on"));
		params.add(new BasicNameValuePair("[hudson.model.Hudson.Read]", "on"));
		params.add(new BasicNameValuePair("Submit", "Save"));
		params.add(new BasicNameValuePair("json", "{'use_security':{'slaveAgentPort':'','':'0','markupFormatter':{'stapler-class':'hudson.markup.RawHtmlMarkupFormatter'},'realm':{'value':'2','stapler-class':'hudson.security.HudsonPrivateSecurityRealm','allowsSignup':true,'notifyUser':true},'authorization':{'value':'3','stapler-class':'hudson.security.ProjectMatrixAuthorizationStrategy','data':{'admin':{'hudson.model.Hudson.Administer':true,'hudson.model.Hudson.Read':true,'hudson.model.Computer.Configure':true,'hudson.model.Computer.Delete':true,'hudson.model.Item.Create':true,'hudson.model.Item.Delete':true,'hudson.model.Item.Configure':true,'hudson.model.Item.Read':true,'hudson.model.Item.Build':true,'hudson.model.Item.Workspace':true,'hudson.model.Run.Delete':true,'hudson.model.Run.Update':true,'hudson.model.View.Create':true,'hudson.model.View.Delete':true,'hudson.model.View.Configure':true,'hudson.scm.SCM.Tag':true},'ss':{'hudson.model.Hudson.Administer':true,'hudson.model.Hudson.Read':true,'hudson.model.Computer.Configure':true,'hudson.model.Computer.Delete':true,'hudson.model.Item.Create':true,'hudson.model.Item.Delete':true,'hudson.model.Item.Configure':true,'hudson.model.Item.Read':true,'hudson.model.Item.Build':true,'hudson.model.Item.Workspace':true,'hudson.model.Run.Delete':true,'hudson.model.Run.Update':true,'hudson.model.View.Create':true,'hudson.model.View.Delete':true,'hudson.model.View.Configure':true,'hudson.scm.SCM.Tag':true},'tt':{'hudson.model.Hudson.Administer':true,'hudson.model.Hudson.Read':true,'hudson.model.Computer.Configure':true,'hudson.model.Computer.Delete':true,'hudson.model.Item.Create':true,'hudson.model.Item.Delete':true,'hudson.model.Item.Configure':true,'hudson.model.Item.Read':true,'hudson.model.Item.Build':true,'hudson.model.Item.Workspace':true,'hudson.model.Run.Delete':true,'hudson.model.Run.Update':true,'hudson.model.View.Create':true,'hudson.model.View.Delete':true,'hudson.model.View.Configure':true,'hudson.scm.SCM.Tag':true},'anonymous':{'hudson.model.Hudson.Administer':false,'hudson.model.Hudson.Read':true,'hudson.model.Computer.Configure':false,'hudson.model.Computer.Delete':false,'hudson.model.Item.Create':false,'hudson.model.Item.Delete':false,'hudson.model.Item.Configure':false,'hudson.model.Item.Read':true,'hudson.model.Item.Build':false,'hudson.model.Item.Workspace':false,'hudson.model.Run.Delete':false,'hudson.model.Run.Update':false,'hudson.model.View.Create':false,'hudson.model.View.Delete':false,'hudson.model.View.Configure':false,'hudson.scm.SCM.Tag':false},'jjkk':{'hudson.model.Hudson.Administer':true,'hudson.model.Hudson.Read':true,'hudson.model.Computer.Configure':true,'hudson.model.Computer.Delete':true,'hudson.model.Item.Create':true,'hudson.model.Item.Delete':true,'hudson.model.Item.Configure':true,'hudson.model.Item.Read':true,'hudson.model.Item.Build':true,'hudson.model.Item.Workspace':true,'hudson.model.Run.Delete':true,'hudson.model.Run.Update':true,'hudson.model.View.Create':true,'hudson.model.View.Delete':true,'hudson.model.View.Configure':true,'hudson.scm.SCM.Tag':true}},'':'yyyyyyyy'}}}"));
		try {
			post.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(post);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void authenticate(String username, String password) {
		HttpClient httpClient = HttpClientUtils.getInstance();
		HttpResponse response = null;
		HttpPost post = new HttpPost("http://127.0.0.1:8080/hudson/j_spring_security_check");
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("j_username", username));
//		params.add(new BasicNameValuePair("j_password", password));
//		params.add(new BasicNameValuePair("from", "/hudson/"));
//		params.add(new BasicNameValuePair("json", "init"));
		try {
			post.setHeader("X-Requested-With", "XMLHttpRequest");
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setHeader("Referer", "http://127.0.0.1:8080/hudson/");
//			post.setEntity(new UrlEncodedFormEntity(params));
			post.setEntity(new StringEntity("j_username=admin&j_password=admin"));
			response = httpClient.execute(post);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

}
