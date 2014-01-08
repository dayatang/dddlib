package org.openkoala.koala.jbpm.jbpmDesigner.applicationImpl;

import com.dayatang.utils.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openkoala.jbpm.application.JBPMApplication;
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

	@Value("${gunvor.server.url}")
	private String gunvorServerUrl;
	@Value("${gunvor.server.user}")
	private String gunvorServerUser;
	@Value("${gunvor.server.pwd}")
	private String gunvorServerPwd;

	
	public void publichJBPM(String packageName, String name, String url) {
		Assert.isTrue(url.endsWith("?_wadl"));
		String publisURL = url.substring(0,url.indexOf("?_wadl"))+"/process";
		try {
			Bpmn2 bpmn = this.getBpmn2(packageName, name);
			String source = getConnectionString(bpmn.getSource());
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
			String pngURL = gunvorServerUrl + "/rest/packages/" + packageName
					+ "/assets/" + processId + "-image/binary";
			byte[] pngByte = this.getPng(pngURL);
			
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(publisURL);
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("packageName", packageName));
			nvps.add(new BasicNameValuePair("processId",processId));
			nvps.add(new BasicNameValuePair("version", bpmn.getVersion()));
			nvps.add(new BasicNameValuePair("data", document.asXML()));
			nvps.add(new BasicNameValuePair("png", new String(pngByte)));
			nvps.add(new BasicNameValuePair("isActive", "true"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			CloseableHttpResponse response = httpclient.execute(httpPost);

			try {
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode()==204){
					
				}
			} finally {
			    response.close();
			}
		    
			
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {   
    }  
	}
	
	private Bpmn2 getBpmnFromDocument(String packageName,Document assertDocument){
		Bpmn2 bpmn = null;
		if (assertDocument != null) {
			Element assertRoot = assertDocument.getRootElement();
			Element metadata = assertRoot.element("metadata");
			if ("bpmn2".equals(metadata.elementText("format"))
					|| "bpmn".equals(metadata.elementText("format"))) {
				bpmn = new Bpmn2();
				bpmn.setCreated(metadata.elementText("created"));
				bpmn.setCreatedby(metadata.elementText("createdBy"));
				bpmn.setDescription(assertRoot
						.elementText("description"));
				bpmn.setFormat(metadata.elementText("format"));
				bpmn.setText(assertRoot.elementText("title"));
				bpmn.setPkgname(packageName);
				bpmn.setUuid(metadata.elementText("uuid"));
				bpmn.setVersion(metadata.elementText("version"));
				bpmn.setSource(assertRoot.elementText("sourceLink"));
				bpmn.setPackageName(packageName);
			}
		}
		return bpmn;
	}

	public List<Bpmn2> getBpmn2s(String packageName) {
		List<Bpmn2> bpmn2 = new ArrayList<Bpmn2>();
		try {
			String result = getConnectionString(gunvorServerUrl
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
					assertResult = getConnectionString(assertUrl);
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
				
				if(bpmn!=null){
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
		String assertUrl = gunvorServerUrl + "/rest/packages/" + packageName
				+ "/assets/" + bmnName;
		String assertResult = getConnectionString(assertUrl);
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
		// /packages/{packageName}
		String deleteBpmnName = null;
		try {
			deleteBpmnName = URLEncoder.encode(bpmnName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = gunvorServerUrl + "/rest/packages/" + packageName
				+ "/assets/" + deleteBpmnName;
		deleteConnectionString(url);
	}

	public void deletePackage(String packageName) {
		String url = gunvorServerUrl + "/rest/packages/" + packageName;
		deleteConnectionString(url);
	}

	public List<PackageVO> getPackages() {
		InputStream is = null;
		try {
			List<PackageVO> packages = new ArrayList<PackageVO>();
			String stringBuilder = getConnectionString(gunvorServerUrl
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
			;
		}
	}

	public void createPackage(String packageName, String description) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><package><description>"
				+ description
				+ "</description><title>"
				+ packageName
				+ "</title></package>";
		postConnectionString(gunvorServerUrl + "/rest/packages", xml);

	}

	public void createBpmn2(String packageName, String name, String description) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><entry xmlns=\"http://purl.org/atom/ns#\"><description></description><name>AB</name><categoryName></categoryName><format>bpmn2</format></entry>";
		postConnectionString(gunvorServerUrl + "/rest/packages/" + packageName
				+ "/assets", xml);
	}

	private void postConnectionString(String urlString, String xmlContent) {

		// String xml =
		// "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><package><description>The default rule package</description><title>demo</title></package>";
		
		HttpClient httpclient = null;
		try {
			InputStream inputString = null;
			byte[] data;
			inputString = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
			data = new byte[inputString.available()];
			inputString.read(data);
			String str = new String(data);
			byte[] bb = str.getBytes();
			httpclient = getDefaultHttpClient();
			HttpPost httpPost = new HttpPost(urlString);
			httpPost.addHeader("Content-Type", "application/xml");
			httpPost.setEntity(new ByteArrayEntity(bb));
			httpclient.execute(httpPost);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除一个流程
	 * 
	 * @param urlString
	 */
	private void deleteConnectionString(String urlString) {
		DefaultHttpClient httpclient = getDefaultHttpClient();
		HttpDelete httpDelete = new HttpDelete(urlString);
		try {
			httpclient.execute(httpDelete);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpDelete.releaseConnection();
		}

	}

	/**
	 * 查询一个流程的PNG
	 * 
	 * @param urlString
	 * @return
	 */
	private byte[] getPng(String urlString) {
		DefaultHttpClient httpclient = getDefaultHttpClient();
		HttpGet httpGet = new HttpGet(urlString);
		httpGet.setHeader("Accept", 
	             "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		try {
			HttpResponse response = httpclient.execute(httpGet);
			InputStream inputStream = response.getEntity().getContent();
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
			int rc = 0;
			while ((rc = inputStream.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			byte[] in_b = swapStream.toByteArray();
			return in_b;
//			return convertToByteArray(in_b);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}
		return null;
	}
	
	private Byte[] convertToByteArray(byte[] pngs){
		Byte[] pngByte = new Byte[pngs.length];
		for(int i=0; i<pngs.length; i++){
			pngByte[i] = Byte.valueOf(pngs[i]);
		}
		return pngByte;
	}

	/**
	 * 传入REST请求的URL，返回String
	 */
	public String getConnectionString(String urlString) {
		DefaultHttpClient httpclient = getDefaultHttpClient();
		HttpGet httpGet = new HttpGet(urlString);
		httpGet.setHeader("Accept", 
	             "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		try {
			HttpResponse response = httpclient.execute(httpGet);
			String result = EntityUtils.toString(response.getEntity());
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}
		return null;
	}

	/**
	 * 返回一个带验证的默认HTTP CLIENT，用于和Gunonor进行交互
	 * @return
	 */
	private DefaultHttpClient getDefaultHttpClient() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getCredentialsProvider().setCredentials(
				new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
				new UsernamePasswordCredentials(gunvorServerUser,
						gunvorServerPwd));
		
		return httpclient;
	}
}