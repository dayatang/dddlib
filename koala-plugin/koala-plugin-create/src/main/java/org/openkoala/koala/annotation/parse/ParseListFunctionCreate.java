package org.openkoala.koala.annotation.parse;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.openkoala.koala.action.XmlParse;
import org.openkoala.koala.action.XmlParseUtil;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.action.xml.DocumentUtil;
import org.openkoala.koala.action.xml.PomXmlWriter;
import org.openkoala.koala.parse.XML2ObjectUtil;
import org.openkoala.koala.widget.Module;
import org.openkoala.koala.widget.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @description 项目创建中的list属性的解析
 *  
 * @date：      2013-8-26   
 * 
 * @version    Copyright (c) 2013 Koala All Rights Reserved
 * 
 * @author     lingen.liu  <a href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a>
 */
public class ParseListFunctionCreate extends AbstractXmlParse implements Parse {

	private static final Logger logger = LoggerFactory.getLogger(ParseListFunctionCreate.class);
	
	//参数值，每个Parse都会有的
	private List params;
	
	private String name;
	
	private Object fieldVal;

	public void initParms(List params,String name,Object fieldVal) {
		this.params = params;
		this.name = name;
		this.fieldVal = fieldVal;
	}

	public void process() throws Exception {
		List<String> values = (List<String>)fieldVal;
		if(values==null)return;
		for(String value:values){
			  logger.info("解析function属性【"+value+"】");
			  String xmlPath = "xml/ListFunctionCreate/"+value+".xml";
			  XmlParseUtil.parseXml(xmlPath, params);
			  logger.info("function属性【"+name+"】解析成功");
		}
		
		Project project  = null;
		Module module =null;
		for(Object obj:params){
			if(obj instanceof Project)project = (Project)obj;
			if(obj instanceof Module)module = (Module)obj;
		}
		//读取指定项目的pom.xml
		String pomPath = project.getPath() + "/" + project.getAppName() + "/" + module.getModuleName() + "/pom.xml";
		Document pomDocument = DocumentUtil.readDocument(pomPath);
		for(String value:values){
			//读取当前功能对应的依赖定义 
			Document dependencies = DocumentUtil.readDocument("xml/dependencies/"+value+"-dependencies.xml");
			List<Element> dependencyList = dependencies.getRootElement().elements("dependency");
			//将当前功能的依赖定义写入到pom.xml中
			PomXmlWriter.addDependencies(dependencyList, pomDocument);
		}
		DocumentUtil.document2Xml(pomPath, pomDocument);
	}
	
}
