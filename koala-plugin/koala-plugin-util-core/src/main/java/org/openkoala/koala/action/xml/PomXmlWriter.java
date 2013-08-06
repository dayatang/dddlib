package org.openkoala.koala.action.xml;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.openkoala.koala.pojo.Dependency;

/**
 * 提供对POM.XML进行修改的功能
 * @author lingen
 *
 */
public class PomXmlWriter {
	
	public static final String POM_XMLS = "http://maven.apache.org/POM/4.0.0";
	
	private static final String DEPENDENCIES_XPATH = "/xmlns:project/xmlns:dependencies";
	
	private static final String MODULES_XPAH="/xmlns:project/xmlns:modules";
	/**
	 * 将指定pom中的artifactId进行修改
	 * @param document
	 * @param newArtifactId
	 */
	public static void updatePomArtifactId(Document document,String newArtifactId){
		Element element = XPathQueryUtil.querySingle(POM_XMLS, "/xmlns:project/xmlns:artifactId", document);
		element.setText(newArtifactId);
	}
	
	/**
	 * 将指定的pom.xml中的parent的pom.xml进行修改
	 * @param document
	 * @param newParentArtifactId
	 */
	public static void updatePomParentArtifactId(Document document,String newParentArtifactId){
		Element element = XPathQueryUtil.querySingle(POM_XMLS, "/xmlns:project/xmlns:parent/xmlns:artifactId", document);
		element.setText(newParentArtifactId);
	}
	
	/**
	 * 将pom.xml中指定的依赖修改成新的依赖
	 * @param document
	 * @param oldDependency
	 * @param newDependency
	 */
	public static void updateDependency(Document document,Dependency oldDependency,String suffer){
		String xPathString = "/xmlns:project/xmlns:dependencies/xmlns:dependency[xmlns:groupId='"+oldDependency.getGroupId()+"' and xmlns:artifactId='"+oldDependency.getArtifactId()+"']";
		Element element = XPathQueryUtil.querySingle(POM_XMLS,xPathString, document);
		if(element==null)return;
		Element artifactId = element.element("artifactId");
		artifactId.setText(artifactId.getText()+suffer);
	}
	
	public static void updateDependencyRemoveScope(Document document,Dependency oldDependency,String suffer){
		String xPathString = "/xmlns:project/xmlns:dependencies/xmlns:dependency[xmlns:groupId='"+oldDependency.getGroupId()+"' and xmlns:artifactId='"+oldDependency.getArtifactId()+"']";
		Element element = XPathQueryUtil.querySingle(POM_XMLS,xPathString, document);
		if(element==null)return;
		Element artifactId = element.element("artifactId");
		artifactId.setText(artifactId.getText()+suffer);
		if(element.element("scope")!=null){
			element.remove(element.element("scope"));
		}
		if(element.element("type")!=null){
			element.remove(element.element("type"));
		}
	}
	
	/**
	 * 更新一个项目的子项目信息
	 * @param document
	 * @param newModule
	 */
	public static void updateModules(Document document,List<String> newModules){
		String xPathString = "/xmlns:project/xmlns:modules";
		Element element= XPathQueryUtil.querySingle(POM_XMLS,xPathString, document);
		List<Element> modules = element.elements("module");
		for(Element module:modules){
			element.remove(module);
		}
		for(String module:newModules){
			Element child = element.addElement("module", POM_XMLS);
			child.setText(module);
		}
	}
	
	/**
	 * 向pom.xml中新增一条
	 * @param groupId
	 * @param artifactId
	 * @param version
	 * @param document
	 */
	public static void addDependencies(String groupId,String artifactId,String version,Document document){
		Element depenciesElement = XPathQueryUtil.querySingle(POM_XMLS, DEPENDENCIES_XPATH, document);
		if(depenciesElement==null)return;
		if(!PomXmlReader.isExists(groupId, artifactId, document)){
			Element depency = depenciesElement.addElement("dependency", POM_XMLS);
			  depency.addElement("groupId",POM_XMLS).setText(groupId);
			  depency.addElement("artifactId",POM_XMLS).setText(artifactId);
			  depency.addElement("version",POM_XMLS).setText(version);
			}
	}
	
	/**
	 * 向pom.xml中加入指定的依赖
	 * @param elements
	 * @param document
	 */
	public static void addDependencies(List<Element> elements,Document document){
		Element depenciesElement = XPathQueryUtil.querySingle(POM_XMLS, DEPENDENCIES_XPATH, document);
		for(Element elemnet:elements){
			if(!PomXmlReader.isExists(elemnet.elementText("groupId"), elemnet.elementText("artifactId"), document)){
			  Element depency = depenciesElement.addElement(elemnet.getName(), POM_XMLS);
			  depency.addElement("groupId",POM_XMLS).setText(elemnet.elementText("groupId"));
			  depency.addElement("artifactId",POM_XMLS).setText(elemnet.elementText("artifactId"));
			  //depency.addElement("version",POM_XMLS).setText(elemnet.elementText("version"));
			  if(elemnet.elementText("scope")!=null)
				  depency.addElement("scope",POM_XMLS).setText(elemnet.elementText("scope"));
			  
			  if(elemnet.element("exclusions")!=null){
				  Element exclusions = elemnet.element("exclusions");
				  Element depenexclusions = depency.addElement("exclusions",POM_XMLS);
				  List<Element> depencyExclusion = exclusions.elements("exclusion");
				  for(Element element:depencyExclusion){
					  Element  exclusion =  depenexclusions.addElement("exclusion",POM_XMLS);
					  exclusion.addElement("groupId",POM_XMLS).setText(element.elementText("groupId"));
					  exclusion.addElement("artifactId",POM_XMLS).setText(element.elementText("artifactId"));
				  }
			  }
			}
		}
	}
	
	/**
	 * 删除指定groupID的Dependencies
	 * @param groupId
	 * @param document
	 */
	public static void removeDependencies(String groupId,Document document){
		String xPathString = "/xmlns:project/xmlns:dependencies/xmlns:dependency[xmlns:groupId='"+groupId+"']";
		List<Element> elements = XPathQueryUtil.query(POM_XMLS, xPathString, document);
		for(Element element:elements){
			element.getParent().remove(element);
		}
	}
	
	/**
	 * 
	 * @param groupId
	 * @param artifactId
	 * @param document
	 */
	public static void removeDependencies(String groupId,String artifactId,Document document){
		String xPathString = "/xmlns:project/xmlns:dependencies/xmlns:dependency[xmlns:groupId='"+groupId+"' and xmlns:artifactId='"+artifactId+"']";
		List<Element> elements = XPathQueryUtil.query(POM_XMLS, xPathString, document);
		for(Element element:elements){
			element.getParent().remove(element);
		}
	}
	
	
	/**
	 * 向pom.xml中加入一个module
	 * @param moduleName
	 * @param document
	 */
	public static void addModule(String moduleName,Document document){
		
		String modulePath="/xmlns:project/xmlns:modules[xmlns:module='"+moduleName+"']";
		List<Element> moduleElements = XPathQueryUtil.query(POM_XMLS, modulePath, document);
		if(moduleElements==null || moduleElements.size()==0){
		   Element modulesElement = XPathQueryUtil.querySingle(POM_XMLS, MODULES_XPAH, document);
		   Element module = modulesElement.addElement("module",POM_XMLS);
		   module.setText(moduleName);
		}
	}
}
 