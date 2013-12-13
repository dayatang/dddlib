package org.openkoala.cas.casmanagement.ws.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * API测试
 * @author zhuyuanbiao
 * @date 2013年12月12日 下午2:28:13
 *
 */
public class CasUserManagementWebServiceApiTest {
	
	private static final String API_URL = "http://localhost:8080/api/user";
	
	private HttpClient httpClient;
	
	@Before 
	public void setUp() {
		httpClient = new DefaultHttpClient();
	}
	
	@After
	public void tearDown() {
		httpClient.getConnectionManager().shutdown();
	}

	@Test
	public void testCreateUser() throws Exception {
		HttpPost post = new HttpPost(API_URL);
		StringEntity entity = new StringEntity("{\"userAccount\":\"test\",\"username\":\"test\", \"password\":\"111111\", \"email\":\"zz@126.com\", \"tel\":\"12345678\", \"userDesc\":\"zzzzzzz\"}");
		entity.setContentType("application/json");
		post.setEntity(entity);
		httpClient.execute(post);
	}
	
	@Test
	public void testModifyPassword() throws Exception {
		HttpPut put = new HttpPut(API_URL + "/password/4");
		StringEntity entity = new StringEntity("{\"password\":\"mm\", \"oldPassword\":\"111111\"}");
		entity.setContentType("application/json");
		put.setEntity(entity);
		httpClient.execute(put);
	}
	
}
