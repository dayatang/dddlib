package org.openkoala.koala.actionvo;

import java.util.List;

import org.apache.velocity.VelocityContext;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.action.xml.DocumentUtil;
import org.openkoala.koala.action.xml.XPathQueryUtil;
import org.openkoala.koala.exception.KoalaException;
/**
 * 查找更新XML文件
 * @author lingen
 *
 */
public class XmlAdd {

	private String express;//执行此操作的表达式
	
	private String xmlFile;//指定需要修改的XML文件
	
	private String uri;//如果有的话，指定此XML的空间名
	
	private String parentSearch;//指定查找父元素的字符串
	
	private String selfSearch;//指定查找本身需要的字符串
	
	private String xmlContent;//指定插入的内容
	
	public void process(VelocityContext context) throws KoalaException{
		if(express!=null && VelocityUtil.isExpressTrue(express, context)==false)return;
		xmlFile = VelocityUtil.evaluateEl(xmlFile, context);
		
		Document document = DocumentUtil.readDocument(xmlFile);
		
		Element selfElement  = XPathQueryUtil.querySingle(uri, selfSearch, document);
		if(selfElement==null){
			Document selfDocument = DocumentUtil.readDocumentFromString(xmlContent);
			selfElement = selfDocument.getRootElement();
			Element parent = XPathQueryUtil.querySingle(uri, parentSearch, document);
			List<Element> childs = selfElement.elements();
			for(Element child:childs){
				XPathQueryUtil.addElement(parent, child, uri);
			}
			DocumentUtil.document2Xml(xmlFile, document);
		}
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getXmlFile() {
		return xmlFile;
	}

	public void setXmlFile(String xmlFile) {
		this.xmlFile = xmlFile;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getParentSearch() {
		return parentSearch;
	}

	public void setParentSearch(String parentSearch) {
		this.parentSearch = parentSearch;
	}

	public String getSelfSearch() {
		return selfSearch;
	}

	public void setSelfSearch(String selfSearch) {
		this.selfSearch = selfSearch;
	}

	public String getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}
	
}
