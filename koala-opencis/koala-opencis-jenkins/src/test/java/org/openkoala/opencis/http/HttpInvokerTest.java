package org.openkoala.opencis.http;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
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
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class HttpInvokerTest {
	
	private static final String REQUEST_URL = "http://openkoala.org/display/koala/Home";
	private static final String REQUEST_URL2 = "http://localhost:8080/jenkins";

	@Test
	public void testHttpRequest() {
		HttpInvoker httpInvoker = new HttpInvoker(REQUEST_URL);
		try {
			HttpResponse httpResponse = httpInvoker.execute();
			assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void testHttpRequestIfSetHttpPost() throws UnsupportedEncodingException {
		HttpInvoker httpInvoker = new HttpInvoker(REQUEST_URL2 + "/createItem?name=test");
		HttpPost httpPost = httpInvoker.getHttpPost();
		StringEntity entity = new StringEntity(getConfigFileContent(), "UTF-8");
		httpPost.addHeader("Content-Type", "application/xml");
		httpPost.setEntity(entity);
		try {
			HttpResponse httpResponse = httpInvoker.execute();
			assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void testHttpRequestIfSetCookies() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("j_username", "test"));
		params.add(new BasicNameValuePair("j_password", "test"));
		HttpInvoker httpInvoker = new HttpInvoker(REQUEST_URL2 + "/j_acegi_security_check", params);
		try {
			// 登录
			HttpResponse httpResponse = httpInvoker.execute();
			assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, httpResponse.getStatusLine().getStatusCode());
			CookieStore cookieStore = ((AbstractHttpClient) httpInvoker.getHttpClient()).getCookieStore();
			
			// 重载配置
			httpInvoker = new HttpInvoker(REQUEST_URL2 + "/reload");
			((AbstractHttpClient) httpInvoker.getHttpClient()).setCookieStore(cookieStore);
			httpResponse = httpInvoker.execute();
			assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, httpResponse.getStatusLine().getStatusCode());
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getConfigFileContent() {
		InputStream in = null;
		StringBuilder result = new StringBuilder();
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("ci/jenkins/config.xml");
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				result.append(new String(buffer, 0, len));
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
		return result.toString();
	}
}
