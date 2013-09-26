package org.openkoala.koala.action.xml;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultAttribute;

/**
 * 移除XML中某些指定的元素，然后删除它 用于删除或移除某些功能中
 * 
 * @author lingen.liu
 * 
 */
public class XPathQueryUtil {

	/**
	 * 查询一个带命名空间的XML
	 * @param xmls
	 * @param xPathString
	 * @param document
	 * @return
	 */
	public static List<Element> query(String xmls, String xPathString, Document document) {
		HashMap xmlMap = new HashMap();
		xmlMap.put("xmlns", xmls);
		XPath xPath = document.createXPath(xPathString);
		xPath.setNamespaceURIs(xmlMap);
		List<Element> selects = xPath.selectNodes(document);
		return selects;
	}
	
	/**
	 * 查询一个带命名空间的XML,返回唯一的元素
	 * @param xmls
	 * @param xPathString
	 * @param document
	 * @return
	 */
	public static Element querySingle(String xmls, String xPathString, Document document){
		HashMap xmlMap = new HashMap();
		xmlMap.put("xmlns", xmls);
		XPath xPath = document.createXPath(xPathString);
		xPath.setNamespaceURIs(xmlMap);
		Element element = (Element)xPath.selectSingleNode(document);
		return element;
	}

	/**
	 * 查询一个不带命名空间的XML的元素
	 * @param xPathString
	 * @param document
	 * @return
	 */
	public static List<Element> query(String xPathString, Document document) {
		List<Element> lists = document.selectNodes(xPathString);
		return lists;
	}

	public static void main(String args[]) throws DocumentException,
			MalformedURLException {
		File file = new File("pom.xml");
		if (file.exists()) {
			SAXReader reader = new SAXReader();
			Document document;
			document = reader.read(file);

			XPathQueryUtil util = new XPathQueryUtil();
			System.out.println(util.querySingle("http://maven.apache.org/POM/4.0.0","/xmlns:project/xmlns:dependencies", document));
		}
	}
	
	public static void addElement(Element parent,Element element,String xmls){
		Element add = parent.addElement(element.getName(), xmls);
		List<Element> childs = (List<Element>)element.elements();
		if(childs==null || childs.size()==0){
		    List<Attribute> atts = element.attributes();
		    for(Attribute att:atts){
		        add.addAttribute(att.getName(), att.getValue());
		    }
			add.setText(element.getText());
		}else{
		for(Element child:childs){
			addElement(add,child,xmls);
		}
		}
	}
}
