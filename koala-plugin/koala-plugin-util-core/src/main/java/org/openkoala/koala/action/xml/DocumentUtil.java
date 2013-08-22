package org.openkoala.koala.action.xml;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.util.EclipseUrlParseUtils;
import org.slf4j.LoggerFactory;

/**
 * 将一个Document更新到xml中，完成修改工作
 * 
 * @author lingen.liu
 * 
 */
public class DocumentUtil {

	private static final String ENCODING = "UTF-8";
	
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DocumentUtil.class);

	private DocumentUtil() {
	}
	
	/**
	 * 将一个修改后的Document写入到指定的XMLPATH中去
	 * 
	 * @param xmlPath
	 * @param document
	 * @throws IOException
	 */
	public static void document2Xml(String xmlPath, Document document)
			throws KoalaException {
		XMLWriter writer = null;
		try {
			// 设置XMLWriter输出漂亮的XML文档格式
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 设置输出编码
			format.setEncoding(ENCODING);
			// 设置缩进空格数
			format.setIndentSize(4);
			writer = new XMLWriter(new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(xmlPath), ENCODING)), format);
			writer.write(document);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new KoalaException(e.getMessage());
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取指定位置中的Document
	 * 
	 * @param xmlPath
	 * @return
	 * @throws DocumentException
	 */
	@SuppressWarnings("rawtypes")
	public static Document readDocument(String xmlPath) throws KoalaException {
		File file = null;
		SAXReader reader = new SAXReader();

		// 处理在jar包中读取不到文件的情况
		URL url = Thread.currentThread().getContextClassLoader().getResource(xmlPath);
		if (url == null) {
			file = new File(xmlPath);
		} else {
			if ("bundleresource".equals(url.getProtocol())) {
				url = EclipseUrlParseUtils.parseUrl(url);
			}
			if ("jar".equals(url.getProtocol())) {
				InputStream in = null;
				JarFile jarFile = null;
				try {
					JarURLConnection conn = (JarURLConnection) url
							.openConnection();
					jarFile = conn.getJarFile();
					for (Enumeration entries = jarFile.entries(); entries
							.hasMoreElements();) {
						JarEntry entry = (JarEntry) entries.nextElement();
						String entryName = entry.getName();

						if (entryName.endsWith("xml")
								&& entryName.equals(xmlPath)) {
							in = Thread.currentThread().getContextClassLoader()
									.getResourceAsStream(entryName);
							
							break;
						}
					}
					InputStreamReader sr = new InputStreamReader(in, ENCODING); 
					return reader.read(sr);
				} catch (Exception e) {
					logger.error(xmlPath + ":解析出错");
					e.printStackTrace();
				}
			}
			if ("file".equals(url.getProtocol())) {
				file = new File(url.getFile());
			}
		}

		if (!file.exists()) {
			throw new KoalaException("文件不存在:" + xmlPath);
		}

		try {
			return reader.read(file);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从已有的输入流中读取XML文档
	 * 
	 * @param in
	 * @return
	 */
	public static Document readDocument(InputStream in) {
		SAXReader reader = new SAXReader();
		reader.setEncoding(ENCODING);
		Document document = null;
		try {
			document = reader.read(in);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	public static Document readDocumentFromString(String src) {
		SAXReader reader = new SAXReader();
		reader.setEncoding(ENCODING);
		Document document = null;
		try {
			document = reader.read(new ByteArrayInputStream(src.getBytes()));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
}
