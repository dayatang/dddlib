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
 * 
 * 类    名：XmlRemove.java
 *   
 * 功能描述：删除指定XML文件中的指定元素	
 *  
 * 创建日期：2013-1-25上午10:50:38     
 * 
 * 版本信息：
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved
 * 
 * 作    者：lingen(lingen.liu@gmail.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class XmlRemove {

	private String express;//执行此操作的表达式
	
	private String xmlFile;//指定需要修改的XML文件
	
	private String uri;//如果有的话，指定此XML的空间名
	
	private String parentSearch;//指定查找父元素的字符串
	
	private String selfSearch;//指定查找本身需要的字符串
	
	public void process(VelocityContext context) throws KoalaException{
		if(express!=null && VelocityUtil.isExpressTrue(express, context)==false)return;
		xmlFile = VelocityUtil.evaluateEl(xmlFile, context);
		
		Document document = DocumentUtil.readDocument(xmlFile);
		
		List<Element> selfElements  = XPathQueryUtil.query(uri, selfSearch, document);
		if(selfElements!=null){
		    for(Element selfElement:selfElements){
		    selfElement.getParent().remove(selfElement);
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
	
}
