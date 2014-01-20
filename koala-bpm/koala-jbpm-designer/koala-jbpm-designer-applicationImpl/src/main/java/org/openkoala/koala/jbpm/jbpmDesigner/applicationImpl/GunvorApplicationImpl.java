package org.openkoala.koala.jbpm.jbpmDesigner.applicationImpl;

import com.dayatang.utils.Assert;
import org.apache.commons.net.util.Base64;
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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openkoala.koala.jbpm.jbpmDesigner.application.GunvorApplication;
import org.openkoala.koala.jbpm.jbpmDesigner.application.vo.Bpmn2;
import org.openkoala.koala.jbpm.jbpmDesigner.application.vo.PackageVO;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Named("gunvorApplication")
public class GunvorApplicationImpl implements GunvorApplication {
	
	@Value("${gunvor.server.host}")
	private String gunvorServerHost;
	
	@Value("${gunvor.server.port}")
	private int gunvorServerPort;
	
	@Value("${gunvor.server.user}")
	private String gunvorServerUser;
	
	@Value("${gunvor.server.pwd}")
	private String gunvorServerPwd;
	
	
	private static final String ACCEPT = "Accept";
	
	private static final String XML_HEADER = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	
	private static final String HTTP_PROTOCOL = "http://";
	
	private static final String DROOLS_GUVNOR = "drools-guvnor";
	
	private static final int HTTP_OK = 200;
	
	
	public void publichJBPM(String packageName, String name, String url) {
		Assert.isTrue(url.endsWith("?_wadl"));
		String publisURL = url.substring(0, url.indexOf("?_wadl")) + "/process";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		try {
			Bpmn2 bpmn = this.getBpmn2(packageName, name);
			String source = getHttpGetRequestStringResponse(bpmn.getSource());
			SAXReader reader = new SAXReader();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					source.getBytes("UTF-8"));
			Document document = reader.read(byteArrayInputStream);
			Element root = document.getRootElement();
			Element process = root.element("process");
			String processId = process.attributeValue("id");
			Element bpmnPlane = root.element("BPMNDiagram")
					.element("BPMNPlane");
			bpmnPlane.addAttribute("bpmnElement",
					processId + "@" + bpmn.getVersion());
			process.addAttribute("id", processId + "@" + bpmn.getVersion());
			String pngURL = getGunvorServerUrl() + "/rest/packages/" + packageName
					+ "/assets/" + processId + "-image/binary";
			byte[] pngByte = this.getPng(pngURL);

			
			HttpPost httpPost = new HttpPost(publisURL);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("packageName", packageName));
			nvps.add(new BasicNameValuePair("processId", processId));
			nvps.add(new BasicNameValuePair("version", bpmn.getVersion()));
			nvps.add(new BasicNameValuePair("data", document.asXML()));
			nvps.add(new BasicNameValuePair("png",Base64.encodeBase64String(pngByte)));
			nvps.add(new BasicNameValuePair("isActive", "true"));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpPost);

			try {
				response.getStatusLine();
			} finally {
				response.close();
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String createBpm(String packageName,String bpmName){
		String url = this.getGunvorServerUrl()+"/org.drools.guvnor.Guvnor/assetExtend/?packageName=" +packageName+ "&assetName=" + bpmName;
		return getHttpGetRequestStringResponse(url);
	}

	private Bpmn2 getBpmnFromDocument(String packageName,
			Document assertDocument) {
		Bpmn2 bpmn = null;
		if (assertDocument != null) {
			Element assertRoot = assertDocument.getRootElement();
			Element metadata = assertRoot.element("metadata");
			if ("bpmn2".equals(metadata.elementText("format"))
					|| "bpmn".equals(metadata.elementText("format"))) {
				bpmn = new Bpmn2();
				bpmn.setCreated(metadata.elementText("created"));
				bpmn.setCreatedby(metadata.elementText("createdBy"));
				bpmn.setDescription(assertRoot.elementText("description"));
				bpmn.setFormat(metadata.elementText("format"));
				bpmn.setText(assertRoot.elementText("title"));
				bpmn.setPkgname(packageName);
				bpmn.setUuid(metadata.elementText("uuid"));
				bpmn.setVersion(metadata.elementText("versionNumber"));
				bpmn.setSource(assertRoot.elementText("sourceLink"));
				bpmn.setPackageName(packageName);
			}
		}
		return bpmn;
	}

	public List<Bpmn2> getBpmn2s(String packageName) {
		List<Bpmn2> bpmn2 = new ArrayList<Bpmn2>();
		try {
			String result = getHttpGetRequestStringResponse(getGunvorServerUrl()
					+ "/rest/packages/" + packageName);
			SAXReader reader = new SAXReader();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					result.toString().getBytes("UTF-8"));
			Document document = reader.read(byteArrayInputStream);
			Element root = document.getRootElement();

			List<Element> asserts = root.elements("assets");
			for (Element ass : asserts) {
				String assertUrl = ass.getTextTrim();
				String assertResult = null;
				try {
					assertResult = getHttpGetRequestStringResponse(assertUrl);
				} catch (Exception e) {
					continue;
				}
				if (assertResult == null)
					continue;
				SAXReader assertReader = new SAXReader();
				Document assertDocument = assertReader
						.read(new ByteArrayInputStream(assertResult.toString()
								.getBytes("UTF-8")));
				Bpmn2 bpmn = getBpmnFromDocument(packageName, assertDocument);

				if (bpmn != null) {
					bpmn2.add(bpmn);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bpmn2;
	}

	private Bpmn2 getBpmn2(String packageName, String name)
			throws DocumentException {
		String bmnName = null;
		try {
			bmnName = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String assertUrl = getGunvorServerUrl() + "/rest/packages/" + packageName
				+ "/assets/" + bmnName;
		String assertResult = getHttpGetRequestStringResponse(assertUrl);
		SAXReader assertReader = new SAXReader();
		Document assertDocument = null;
		try {
			assertDocument = assertReader.read(new ByteArrayInputStream(
					assertResult.toString().getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Bpmn2 bpmn = getBpmnFromDocument(packageName, assertDocument);
		return bpmn;
	}

	public void deleteBpmn(String packageName, String bpmnName) {
		try {
			String url = getGunvorServerUrl() + "/rest/packages/" + packageName
					+ "/assets/" + URLEncoder.encode(bpmnName, "UTF-8");
			doHttpDeleteRequest(url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void deletePackage(String packageName) {
		String url = getGunvorServerUrl() + "/rest/packages/" + packageName;
		doHttpDeleteRequest(url);
	}

	public List<PackageVO> getPackages() {
		InputStream is = null;
		try {
			List<PackageVO> packages = new ArrayList<PackageVO>();
			String stringBuilder = getHttpGetRequestStringResponse(getGunvorServerUrl()
					+ "/rest/packages");

			SAXReader reader = new SAXReader();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					stringBuilder.toString().getBytes("UTF-8"));
			Document document = reader.read(byteArrayInputStream);
			Element root = document.getRootElement();
			List<Element> packageElements = root.elements("package");// .element("package");
			for (Element packageElement : packageElements) {
				PackageVO pack = new PackageVO();
				pack.setText(packageElement.elementText("title"));
				pack.setDescription(packageElement.elementText("description"));
				pack.setUuid(packageElement.elementText("uuid"));
				packages.add(pack);
			}
			return packages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public void createPackage(String packageName, String description) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><package><description>"
				+ description
				+ "</description><title>"
				+ packageName
				+ "</title></package>";
		doHttpPostRequest(getGunvorServerUrl() + "/rest/packages", xml);

	}

	public void createBpmn2(String packageName, String name, String description) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><entry xmlns=\"http://purl.org/atom/ns#\"><description></description><name>AB</name><categoryName></categoryName><format>bpmn2</format></entry>";
		doHttpPostRequest(getGunvorServerUrl() + "/rest/packages/" + packageName
				+ "/assets", xml);
	}

	
	/**
	 * 查询一个流程的PNG
	 * 
	 * @param urlString
	 * @return
	 */
	private byte[] getPng(String urlString) {
		return getHttpGetRequestByteArrayResponse(urlString);
	}

	
	public byte[] getHttpGetRequestByteArrayResponse(String urlString){
		HttpEntity entity = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpGet httpGet = this.getXmlHttpGet(urlString);
		try {;
			response = httpclient.execute(httpGet,getCredentialContext());
			entity = response.getEntity();
			
			if(response.getStatusLine().getStatusCode()!=HTTP_OK){
				throw new RuntimeException("HTTP GET请求失败，URL为"+urlString+";返回状态码为:"+response.getStatusLine().getStatusCode());
			}
			return EntityUtils.toByteArray(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpclient);
		}
		return null;
	}
	
	public String getHttpGetRequestStringResponse(String urlString) {
		HttpEntity entity = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpGet httpGet = this.getXmlHttpGet(urlString);
		try {;
			response = httpclient.execute(httpGet,getCredentialContext());
			entity = response.getEntity();
			
			if(response.getStatusLine().getStatusCode()!=HTTP_OK){
				throw new RuntimeException("HTTP GET请求失败，URL为"+urlString+";返回状态码为:"+response.getStatusLine().getStatusCode());
			}
			return EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpclient);
		}
		return null;
	}

	private void doHttpDeleteRequest(String url){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpDelete httpDelete = new HttpDelete();
		try {
			response = httpclient.execute(httpDelete);
			if(response.getStatusLine().getStatusCode()!=HTTP_OK){
				throw new RuntimeException("HTTP DELETE请求失败，URL为:"+url+";返回状态码为:"+response.getStatusLine().getStatusCode());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void doHttpPostRequest(String urlString, String xmlContent) {
		CloseableHttpResponse response = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			InputStream inputString = null;
			byte[] data;
			inputString = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
			data = new byte[inputString.available()];
			inputString.read(data);
			String str = new String(data);
			byte[] bb = str.getBytes();
			HttpPost httpPost = new HttpPost(urlString);
			httpPost.addHeader("Content-Type", "application/xml");
			httpPost.setEntity(new ByteArrayEntity(bb));
			response = httpclient.execute(httpPost);
			if(response.getStatusLine().getStatusCode()!=HTTP_OK){
				throw new RuntimeException("HTTP POST请求失败，URL为:"+urlString+";返回状态码为:"+response.getStatusLine().getStatusCode());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
	
	private HttpGet getXmlHttpGet(String url){
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader(ACCEPT,XML_HEADER);
		return httpGet;
	}

	
	public String getGunvorServerUrl(){
		return HTTP_PROTOCOL+this.gunvorServerHost+":"+this.gunvorServerPort+"/"+DROOLS_GUVNOR;
	}
	
	
}