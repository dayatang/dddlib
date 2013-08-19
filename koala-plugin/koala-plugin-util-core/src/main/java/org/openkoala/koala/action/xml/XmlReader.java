package org.openkoala.koala.action.xml;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.openkoala.koala.exception.KoalaException;


/**
 * 读取XML中元素的值的帮助类
 * @author lingen
 *
 */
public class XmlReader {

	private String xmlns;//命名空间

	private String xmlFile;//XML文件
	
	private Document document;
	
	public static final String POM_XMLS = "http://maven.apache.org/POM/4.0.0";
	
	public static final String WEB_XML_XMLS = "http://java.sun.com/xml/ns/javaee";
	
	private XmlReader()  {
		super();	
	}
	
	
	public static XmlReader getPomReader(String xmlFile) throws KoalaException{
		XmlReader pomReader = new XmlReader();
		pomReader.xmlns = POM_XMLS;
		pomReader.xmlFile = xmlFile;
		pomReader.document = DocumentUtil.readDocument(xmlFile);
		return pomReader;
	}
	
	public static XmlReader getWebXmlReader(String xmlFile) throws KoalaException{
		XmlReader webXmlReader = new XmlReader();
		webXmlReader.xmlns = WEB_XML_XMLS;
		webXmlReader.xmlFile = xmlFile;
		webXmlReader.document = DocumentUtil.readDocument(xmlFile);
		return webXmlReader;
	}
	
	/**
	 * 传入命名空间及XML所在的路径，返回一个Reader对象
	 * @param xmlns
	 * @param xmlFile
	 * @return
	 * @throws KoalaException
	 */
	public static XmlReader getReader(String xmlns,String xmlFile) throws KoalaException{
		XmlReader reader = new XmlReader();
		reader.xmlns = xmlns;
		reader.xmlFile = xmlFile;
		reader.document = DocumentUtil.readDocument(xmlFile);
		return reader;
	}

	/**
	 * 查询某一个元素的值
	 * @param queryXML
	 * @return
	 * @throws KoalaException
	 */
	public String queryString(String queryXML) throws KoalaException{
		String returnString = null;
		Element element = XPathQueryUtil.querySingle(xmlns, queryXML, document);
		if(element!=null)returnString = element.getTextTrim();
		return returnString;
	}
	
	/**
	 * 查询某多个相同元素的值
	 * @param queryXML
	 * @return
	 * @throws KoalaException
	 */
	public List<String> queryListString(String queryXML) throws KoalaException{
        List<String> list = new ArrayList<String>();
		List<Element> elements = XPathQueryUtil.query(xmlns, queryXML, document);
		for(Element element:elements){
			list.add(element.getTextTrim());
		}
		return list;
	}
}
