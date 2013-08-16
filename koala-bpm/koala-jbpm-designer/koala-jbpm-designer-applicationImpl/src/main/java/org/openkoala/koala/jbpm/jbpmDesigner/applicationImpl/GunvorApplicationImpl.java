package org.openkoala.koala.jbpm.jbpmDesigner.applicationImpl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openkoala.jbpm.wsclient.JBPMApplication;
import org.openkoala.jbpm.wsclient.JBPMApplicationImplService;
import org.openkoala.koala.jbpm.jbpmDesigner.application.GunvorApplication;
import org.openkoala.koala.jbpm.jbpmDesigner.application.vo.Bpmn2;
import org.openkoala.koala.jbpm.jbpmDesigner.application.vo.PackageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@Named("gunvorApplication")
public class GunvorApplicationImpl implements GunvorApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(GunvorApplicationImpl.class);

	@Value("${gunvor.server.url}")
	private String gunvorServerUrl;
	@Value("${gunvor.server.user}")
	private String gunvorServerUser;
	@Value("${gunvor.server.pwd}")
	private String gunvorServerPwd;
	
	
	public void publichJBPM(String packageName, String name, String wsdl) {
		try {
			Bpmn2 bpmn = this.getBpmn2(packageName, name);
			URL url = new URL(wsdl);
			JBPMApplication application = new JBPMApplicationImplService(url).getJBPMApplicationImplPort();
			String source = getConnectionString(bpmn.getSource());
			SAXReader reader = new SAXReader();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(source.toString().getBytes("UTF-8"));
			Document document = reader.read(byteArrayInputStream);
			Element root = document.getRootElement();
			Element process = root.element("process");
			String processId = process.attributeValue("id");
			Element bpmnPlane = root.element("BPMNDiagram").element("BPMNPlane");
			bpmnPlane.addAttribute("bpmnElement", processId + "@" + bpmn.getVersion());
			process.addAttribute("id", processId + "@" + bpmn.getVersion());
			String pngURL = gunvorServerUrl + "/rest/packages/" + packageName + "/assets/" + processId + "-image/binary";
			byte[] pngByte = this.getPng(pngURL);
			application.addProcess(packageName,processId, Integer.parseInt(bpmn.getVersion()), document.asXML(), pngByte,true);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public List<Bpmn2> getBpmn2s(String packageName) {
		List<Bpmn2> bpmn2 = new ArrayList<Bpmn2>();
		try {
			String result = getConnectionString(gunvorServerUrl + "/rest/packages/" + packageName);
			SAXReader reader = new SAXReader();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(result.toString().getBytes("UTF-8"));
			Document document = reader.read(byteArrayInputStream);
			Element root = document.getRootElement();

			List<Element> asserts = root.elements("assets");
			for (Element ass : asserts) {
				String assertUrl = ass.getTextTrim();
				String assertResult = null;
				try{
				assertResult = getConnectionString(assertUrl);
				}catch(Exception e){
					continue;
				}
				if(assertResult==null)continue;
				SAXReader assertReader = new SAXReader();
				Document assertDocument = assertReader.read(new ByteArrayInputStream(assertResult.toString().getBytes("UTF-8")));
				if(assertDocument!=null){
					Element assertRoot = assertDocument.getRootElement();
					Element metadata = assertRoot.element("metadata");
					System.out.println(metadata.elementText("title"));
					if ("bpmn2".equals(metadata.elementText("format")) || "bpmn".equals(metadata.elementText("format"))) {
						Bpmn2 bpmn = new Bpmn2();
						bpmn.setCreated(metadata.elementText("created"));
						bpmn.setCreatedby(metadata.elementText("createdBy"));
						bpmn.setDescription(assertRoot.elementText("description"));
						bpmn.setFormat(metadata.elementText("format"));
						bpmn.setText(metadata.elementText("title"));
						bpmn.setPkgname(packageName);
						bpmn.setUuid(metadata.elementText("uuid"));
						bpmn.setVersion(assertRoot.elementText("version"));
						bpmn.setSource(assertRoot.elementText("sourceLink"));
						bpmn.setPackageName(packageName);
						bpmn2.add(bpmn);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bpmn2;
	}

	private Bpmn2 getBpmn2(String packageName, String name) throws DocumentException {
		String bmnName = null;
		try {
			bmnName = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String assertUrl = gunvorServerUrl + "/rest/packages/" + packageName + "/assets/" + bmnName;
		String assertResult = getConnectionString(assertUrl);
		SAXReader assertReader = new SAXReader();
		Document assertDocument = null;
		try {
			assertDocument = assertReader.read(new ByteArrayInputStream(assertResult.toString().getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element assertRoot = assertDocument.getRootElement();
		Element metadata = assertRoot.element("metadata");
		Bpmn2 bpmn = new Bpmn2();
		bpmn.setCreated(metadata.elementText("created"));
		bpmn.setCreatedby(metadata.elementText("createdBy"));
		bpmn.setDescription(assertRoot.elementText("description"));
		bpmn.setFormat(metadata.elementText("format"));
		bpmn.setText(metadata.elementText("title"));
		bpmn.setPkgname(packageName);
		bpmn.setUuid(metadata.elementText("uuid"));
		bpmn.setVersion(assertRoot.elementText("version"));
		bpmn.setSource(assertRoot.elementText("sourceLink"));
		bpmn.setPackageName(packageName);
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
		String url = gunvorServerUrl + "/rest/packages/" + packageName + "/assets/" + deleteBpmnName;
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
			String stringBuilder = getConnectionString(gunvorServerUrl + "/rest/packages");

			SAXReader reader = new SAXReader();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(stringBuilder.toString().getBytes("UTF-8"));
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
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><package><description>" + description + "</description><title>" + packageName + "</title></package>";
		postConnectionString(gunvorServerUrl + "/rest/packages", xml);

	}

	public void createBpmn2(String packageName, String name, String description) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><entry xmlns=\"http://purl.org/atom/ns#\"><description></description><name>AB</name><categoryName></categoryName><format>bpmn2</format></entry>";
		postConnectionString(gunvorServerUrl + "/rest/packages/" + packageName + "/assets", xml);
		// String urlString =gunvorServerUrl+"/org.drools.guvnor.Guvnor/standaloneEditorServlet?packageName="
		// + packageName + "&categoryName=mycategory" +
		// "&createNewAsset=true&description="+ description + "&assetName=" +
		// name + "&assetFormat=bpmn" +"&client=oryx";
		// this.getConnectionString(urlString);

	}

	private void postConnectionString(String urlString, String xmlContent) {
		// String xml =
		// "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><package><description>The default rule package</description><title>demo</title></package>";
		InputStream inputString = null;
		byte[] data;

		HttpURLConnection connection = null;
		URL url;
		try {
			inputString = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
			data = new byte[inputString.available()];
			inputString.read(data);
			url = new URL(urlString);
			String str = new String(data);
			System.out.println(str);
			byte[] bb = str.getBytes();
			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.16) Gecko/20110319 Firefox/3.6.16");
			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
			connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			connection.setRequestProperty("charset", "UTF-8");
			connection.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
			connection.setRequestProperty("Content-Length", String.valueOf(bb.length));
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);

			connection.setReadTimeout(5 * 1000);
			applyAuth(connection);
			connection.connect();

			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(str); // 写入请求的字符串
			out.flush();
			out.close();
			System.out.println(connection.getContent());
			if (connection.getResponseCode() != 200) {
				throw new RuntimeException("Parse URL Error:" + connection.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private void deleteConnectionString(String urlString) {
		HttpURLConnection connection = null;
		URL url;
		try {
			url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("DELETE");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.16) Gecko/20110319 Firefox/3.6.16");
			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
			connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			connection.setRequestProperty("charset", "UTF-8");
			connection.setReadTimeout(5 * 1000);
			applyAuth(connection);
			connection.connect();
			System.out.println(connection.getContent());
			if (connection.getResponseCode() != 200) {
				throw new RuntimeException("Parse URL Error:" + connection.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

	}

	private byte[] getPng(String urlString) {
		HttpURLConnection connection = null;
		URL url;
		try {
			url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.16) Gecko/20110319 Firefox/3.6.16");
			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
			connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			connection.setRequestProperty("charset", "UTF-8");
			connection.setReadTimeout(5 * 1000);
			applyAuth(connection);
			connection.connect();

			InputStream inputStream = connection.getInputStream();
			if (connection.getResponseCode() != 200) {
				throw new RuntimeException("Parse URL Error:" + connection.getResponseMessage());
			}
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
			int rc = 0;
			while ((rc = inputStream.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			byte[] in_b = swapStream.toByteArray();
			return in_b;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

	public String getConnectionString(String urlString) {
		HttpURLConnection connection = null;
		URL url;
		try {
			url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.16) Gecko/20110319 Firefox/3.6.16");
			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
			connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			connection.setRequestProperty("charset", "UTF-8");
			connection.setReadTimeout(5 * 1000);
			applyAuth(connection);
			connection.connect();

			BufferedReader sreader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

			StringBuilder stringBuilder = new StringBuilder();

			String line = null;
			while ((line = sreader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}

			System.out.println(stringBuilder);
			return stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

	public void applyAuth(HttpURLConnection connection) {
		sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
		String userpassword = gunvorServerUser + ":" + gunvorServerPwd;
		String encodedAuthorization = enc.encode(userpassword.getBytes());
		connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
	}

	public void main(String args[]) throws UnsupportedEncodingException {
		GunvorApplicationImpl impl = new GunvorApplicationImpl();
		String process = impl.getConnectionString(gunvorServerUrl + "/rest/packages");
		System.out.println(process.toString());
	}

}
