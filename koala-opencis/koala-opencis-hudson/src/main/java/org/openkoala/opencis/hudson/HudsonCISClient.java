package org.openkoala.opencis.hudson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

/**
 * Hudson的CISClient实现类
 * 
 * @author mluo
 * 
 */
public class HudsonCISClient implements CISClient {

	private static final String HUDSON_URL = "http://localhost:8080/hudson";
	
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

}
