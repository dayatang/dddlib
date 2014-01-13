package org.openkoala.cas.casmanagement.ws.api;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.io.InputStream;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.auth.application.vo.UserVO;

/**
 * API测试
 * @author zhuyuanbiao
 * @date 2013年12月12日 下午2:28:13
 *
 */
public class CasUserManagementWebServiceApiTest {
	
	private static final String APPLICATION_JSON = "application/json";

	private static final int BUFFER_SIZE = 1024;

	private static final String API_URL = "http://localhost:8888/api/user";
	
	private DefaultHttpClient httpClient;
	
	private UserVO savedUser;
	
	
	@Before
	public void init() throws Exception {
		httpClient = new DefaultHttpClient();
		savedUser = createUser();
	}
	
	@After
	public void destroy() throws Exception {
		removeUser();
		httpClient.getConnectionManager().shutdown();
	}

	private void removeUser() throws Exception {
		HttpDelete delete = new HttpDelete(MessageFormat.format("{0}/{1}", API_URL, savedUser.getId()));
		HttpResponse response = httpClient.execute(delete);
		response.getEntity().consumeContent();
		assertNull(getUser());
	}

	public UserVO createUser() throws Exception {
		HttpPost post = new HttpPost(API_URL);
		StringEntity entity = new StringEntity(toJson(newUserVO()));
		entity.setContentType(APPLICATION_JSON);
		post.setEntity(entity);
		String json = getResponseContent(httpClient.execute(post));
		post.getEntity().consumeContent();
		return toUser(json);
	}

	private UserVO newUserVO() {
		UserVO user = new UserVO();
		user.setName("zyb");
		user.setUserPassword("zyb");
		user.setEmail("zyb@gmail.com");
		user.setUserAccount("zyb");
		return user;
	}
	
	private String getResponseContent(HttpResponse response) throws Exception {
		byte[] buffer = new byte[BUFFER_SIZE];
		StringBuffer result = new StringBuffer();
		if (response.getEntity() != null) {
			InputStream in = response.getEntity().getContent();
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				result.append(new String(buffer, 0, len));
			}
			in.close();
			response.getEntity().consumeContent();
		}
		return result.toString();
	}
	
	@Test
	public void testGetUser() throws Exception {
		UserVO user = getUser();
		assertNotNull(user);
		assertEquals(savedUser.getUserAccount(), user.getUserAccount());
		assertEquals(savedUser.getName(), user.getName());
	}

	private UserVO getUser() throws Exception {
		HttpGet get = new HttpGet(MessageFormat.format("{0}/{1}", API_URL, savedUser.getId()));
		String json = getResponseContent(httpClient.execute(get));
		if ("".equals(json)) {
			return null;
		}
		return toUser(json);
	}
	
	@Test
	public void testModifyPassword() throws Exception {
		HttpPut put = new HttpPut(MessageFormat.format("{0}/password/{1}", API_URL, savedUser.getId()));
		UserVO user = new UserVO();
		user.setUserPassword("123456");
		user.setOldPassword("zyb");
		StringEntity entity = new StringEntity(toJson(user));
		entity.setContentType(APPLICATION_JSON);
		put.setEntity(entity);
		put.getEntity().consumeContent();
	}
	
	private String toJson(UserVO user) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(writer);
		jsonGenerator.writeObject(user);
		jsonGenerator.close();
		writer.close();
		return writer.toString();
	}
	
	private UserVO toUser(String json) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(writer);
		jsonGenerator.close();
		writer.close();
		return objectMapper.readValue(json, UserVO.class);
	}
	
	
	@Test
	public void testUpdateUser() throws Exception {
		HttpPut put = new HttpPut(MessageFormat.format("{0}/{1}", API_URL, savedUser.getId()));
		UserVO user = new UserVO();
		user.setUserDesc("xxxxxx");
		user.setName("xxxxxx");
		StringEntity entity = new StringEntity(toJson(user));
		entity.setContentType(APPLICATION_JSON);
		put.setEntity(entity);
		HttpResponse response = httpClient.execute(put);
		response.getEntity().consumeContent();
		UserVO result = toUser(getResponseContent(httpClient.execute(put)));
		put.getEntity().consumeContent();
		assertEquals("xxxxxx", result.getUserDesc());
		assertEquals("xxxxxx", result.getName());
	}
	
	@Test
	public void testDisabled() throws Exception {
		HttpPut put = new HttpPut(MessageFormat.format("{0}/disabled/{1}", API_URL, savedUser.getId()));
		HttpResponse response = httpClient.execute(put);
		response.getEntity().consumeContent();
		UserVO result = toUser(getResponseContent(httpClient.execute(put)));
		assertFalse(result.isValid());
	}
	
	@Test
	public void testEnabled() throws Exception {
		HttpPut put = new HttpPut(MessageFormat.format("{0}/enabled/{1}", API_URL, savedUser.getId()));
		HttpResponse response = httpClient.execute(put);
		response.getEntity().consumeContent();
		UserVO result = toUser(getResponseContent(httpClient.execute(put)));
		assertTrue(result.isValid());
	}
	
	@Test
	public void testGetEmail() throws Exception {
		HttpGet get = new HttpGet(MessageFormat.format("{0}/email/{1}", API_URL, savedUser.getUserAccount()));
		HttpResponse response = httpClient.execute(get);
		assertEquals("zyb@gmail.com", getResponseContent(response));
		response.getEntity().consumeContent();
	}
	
	@Test
	public void testIsUserValid() throws Exception {
		HttpPost post = new HttpPost(MessageFormat.format("{0}/isUserValid", API_URL));
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", savedUser.getUserAccount()));
		params.add(new BasicNameValuePair("password", "zyb"));
		post.setEntity(new UrlEncodedFormEntity(params));
		HttpResponse response = httpClient.execute(post);
		assertTrue(Boolean.valueOf(getResponseContent(response)));
		response.getEntity().consumeContent();
	}
	
	
}
