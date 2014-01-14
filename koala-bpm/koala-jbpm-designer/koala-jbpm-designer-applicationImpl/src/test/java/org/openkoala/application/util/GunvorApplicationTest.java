package org.openkoala.application.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
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
}
