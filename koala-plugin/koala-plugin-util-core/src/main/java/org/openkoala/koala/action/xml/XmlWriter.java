package org.openkoala.koala.action.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.openkoala.koala.exception.KoalaException;

public class XmlWriter {

	private String xmlns;//命名空间

	private String xmlFile;//XML文件
	
	private Document document;
	
	public static final String POM_XMLS = "http://maven.apache.org/POM/4.0.0";
	
	public static final String WEB_XML_XMLS = "ttp://java.sun.com/xml/ns/javaee";
	
	/**
	 * 传入一个命名空间及XML文件路径，返回一个writer
	 * 此writer允许使用XQL来对XML进行修改
	 * @param xmlns
	 * @param xmlFile
	 * @return
	 * @throws KoalaException
	 */
	public static XmlWriter getInstance(String xmlns,String xmlFile) throws KoalaException{
		XmlWriter writer = new XmlWriter();
		writer.xmlFile = xmlFile;
		writer.xmlns = xmlns;
		writer.document = DocumentUtil.readDocument(xmlFile);
		return writer;
	}
	
	/**
	 * 传入pom.xml的文件路径，使用XQL对XML进行修改
	 * @param xmlFile
	 * @return
	 * @throws KoalaException
	 */
	public static XmlWriter getPomXmlWriter(String xmlFile) throws KoalaException{
		XmlWriter pomWriter = new XmlWriter();
		pomWriter.xmlns = POM_XMLS;
		pomWriter.xmlFile = xmlFile;
		pomWriter.document = DocumentUtil.readDocument(xmlFile);
		return pomWriter;
	}
	
	/**
	 * 传入web.xml所在路径，使用XQL对XML进行修改
	 * @param xmlFile
	 * @return
	 * @throws KoalaException
	 */
	public static XmlWriter getWebXmlWriter(String xmlFile) throws KoalaException{
		XmlWriter webXmlWriter = new XmlWriter();
		webXmlWriter.xmlns = WEB_XML_XMLS;
		webXmlWriter.xmlFile = xmlFile;
		webXmlWriter.document = DocumentUtil.readDocument(xmlFile);
		return webXmlWriter;
	}
	
	public void update(String queryXML,String value) throws KoalaException{
		Element element = XPathQueryUtil.querySingle(xmlns, queryXML, document);
		element.setText(value);
	}
	
	public void saveUpdate() throws KoalaException{
		DocumentUtil.document2Xml(xmlFile, document);
	}
	
}
