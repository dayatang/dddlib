package org.openkoala.application.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.dayatang.utils.Assert;

public class GunvorApplicationTest {
	
	@Test
	@Ignore
	public void testPublihBPM() throws ClientProtocolException, IOException{
		
		String packageName = "defaultPackage";

		String processId = "defaultPackage.Trade";

		int version = 1;

		String data = FileUtils.readFileToString(new File(
				GunvorApplicationTest.class.getClassLoader()
						.getResource("json.txt").getPath()));

		byte[] png = FileUtils.readFileToByteArray(new File(
				GunvorApplicationTest.class.getClassLoader()
						.getResource("defaultPackage.Trade-image.png")
						.getPath()));
		
		String pngString = new String(png);
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://localhost:8180/ws/jbpmService/process");
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("packageName", packageName));
		nvps.add(new BasicNameValuePair("processId",processId));
		nvps.add(new BasicNameValuePair("version", String.valueOf(version)));
		nvps.add(new BasicNameValuePair("data", data));
		nvps.add(new BasicNameValuePair("png", pngString));
		nvps.add(new BasicNameValuePair("isActive", "true"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
		CloseableHttpResponse response2 = httpclient.execute(httpPost);

		try {
		    Assert.isTrue(response2.getStatusLine().getStatusCode()==204);
		} finally {
		    response2.close();
		}
	}
	
	@Test
	@Ignore
	public void testGetPackage(){
	
		System.out.println(getConnectionString("http://localhost:8080/drools-guvnor/rest/packages/defaultPackage"));
	}
	
	/**
	 * 传入REST请求的URL，返回String
	 */
	public String getConnectionString(String urlString) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = this.getXmlHttpGet(urlString);
		try {;
			CloseableHttpResponse response = httpclient.execute(httpGet,getCredentialContext());
			String result = EntityUtils.toString(response.getEntity());
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private String gunvorServerHost  = "localhost" ;
	private int gunvorServerPort = 8080;
	private String gunvorServerUser = "admin";
	private String gunvorServerPwd = "admin";
	
	private HttpClientContext getCredentialContext(){
		HttpHost targetHost = new HttpHost(this.gunvorServerHost, this.gunvorServerPort,"http");
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
		        new AuthScope(targetHost.getHostName(), targetHost.getPort()),
		        new UsernamePasswordCredentials(this.gunvorServerUser, this.gunvorServerPwd));
		
		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);

		HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);
		return context;
	}
	
	private static final String ACCEPT = "Accept";
	
	private static final String XML_HEADER = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	
	private static final String HTTP_PROTOCOL = "http://";
	
	private static final String DROOLS_GUVNOR = "drools-guvnor";
	
	private HttpGet getXmlHttpGet(String url){
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader(ACCEPT,XML_HEADER);
		return httpGet;
	}
}
