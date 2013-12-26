package org.openkoala.opencis.http;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openkoala.opencis.PropertyIllegalException;

public class HttpInvoker {

	private String requestUrl;
	
	private List<NameValuePair> params;
	
	private HttpClient httpClient = new DefaultHttpClient();
	
	private HttpPost httpPost;

	public HttpInvoker(String requestUrl, List<NameValuePair> params) {
		this(requestUrl);
		this.params = params;
		verifyNotNull(params);
	}

	public HttpInvoker(String requestUrl) {
		this.requestUrl = requestUrl;
		verifyNotNull(requestUrl);
		initHttpPost();
	}

	private void initHttpPost() {
		httpPost = new HttpPost(requestUrl);
	}

	public HttpResponse execute() throws IOException {
		if (params != null && params.size() > 0) {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
		}
		return httpClient.execute(httpPost);
	}
	
	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	public void shutdown() {
		httpClient.getConnectionManager().shutdown();
	}
	
	public HttpPost getHttpPost() {
		return httpPost;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public void setParams(List<NameValuePair> params) {
		this.params = params;
	}
	
	private void verifyNotNull(Object... params) {
		if (params != null) {
			for (Object each : params) {
				if (each == null) {
					throw new PropertyIllegalException();
				}
			}
		}
	}
	
}
