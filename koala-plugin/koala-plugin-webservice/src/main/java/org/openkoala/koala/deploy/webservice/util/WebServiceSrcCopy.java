package org.openkoala.koala.deploy.webservice.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.openkoala.koala.action.XmlParseUtil;
import org.openkoala.koala.action.file.FileCopyAction;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.action.xml.DocumentUtil;
import org.openkoala.koala.action.xml.PomXmlWriter;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.maven.MavenExcuter;
import org.openkoala.koala.maven.MavenFileFilter;
import org.openkoala.koala.pojo.Dependency;
import org.openkoala.koala.pojo.MavenProject;
import org.openkoala.koala.pojo.ModuleType;

/**
 * WebService项目的复制产生
 * @author lingen
 *
 */
public class WebServiceSrcCopy {
	
	private static final String WS = "-WS";
	
	public static MavenProject copySrc(MavenProject project) throws KoalaException{
		
		MavenProject destProject = project.clone();
		String parentPath = project.getPath()+"/target/"+project.getName()+WS+"/";
		
		//文件的复制工作
				FileCopyAction.clearDir(parentPath);
				FileCopyAction.clearDir(project.getPath()+"/target/ws-client");
				FileCopyAction.copyFile(project.getPath()+"/pom.xml",parentPath+"/pom.xml");

		//获取需要修改的依赖关系
		List<Dependency> changes = new ArrayList<Dependency>();
		
		//获取需要复制生成的子项目
		List<MavenProject> changesProject = new ArrayList<MavenProject>();
		for(MavenProject childProject:project.getChilds()){
			if(childProject.getType().equals(ModuleType.Impl) ||
				childProject.getType().equals(ModuleType.BizModel) ||
				childProject.getType().equals(ModuleType.Infra) ||
				childProject.getType().equals(ModuleType.Application) ||
				childProject.getType().equals(ModuleType.Other) ||
				childProject.getType().equals(ModuleType.conf)
					)
			{
				changesProject.add(childProject);
				Dependency dependency = new Dependency(childProject.getGroupId(),childProject.getArtifactId(),childProject.getVersion());
				changes.add(dependency);
			}
		}
		
		//新子项目名称
		List<String> newModules = new ArrayList<String>();
		String confModulePath = "";
		
		for(MavenProject change:changesProject){
			newModules.add(change.getName()+WS);
			//进行复制
			FileCopyAction.copyDir(change.getPath(), parentPath+change.getName()+WS,MavenFileFilter.newInstance());
			if (change.getType().equals(ModuleType.conf)) {
				confModulePath = parentPath+change.getName()+WS;
			}
		}
		
		if (!confModulePath.isEmpty()) {
			String confModuleRootXmlPath = confModulePath + "/src/main/resources/META-INF/spring/root.xml";
			Document confModuleRootXml = DocumentUtil.readDocument(confModuleRootXmlPath);
			Element root = confModuleRootXml.getRootElement();

			for (Iterator i = root.elementIterator("import"); i.hasNext();) {
				Element element = (Element) i.next();
				if (element.attributeValue("resource").equals("classpath*:META-INF/spring/security-persistence-context.xml")) {
					root.remove(element);
				}
			}
			
			root.addElement("import").addAttribute("resource","classpath*:META-INF/spring/security-application.xml");
			DocumentUtil.document2Xml(confModuleRootXmlPath, confModuleRootXml);
		}
		
		newModules.add("war-ws");
		
		//开始修改文件
		 //第一步，修改父项目的pom.xml，需要修改artifactId以及module
		Document parentPomXmlDocument = DocumentUtil.readDocument(parentPath+"/pom.xml");
		PomXmlWriter.updatePomArtifactId(parentPomXmlDocument, project.getArtifactId()+WS);
		PomXmlWriter.updateModules(parentPomXmlDocument, newModules);
		DocumentUtil.document2Xml(parentPath+"/pom.xml", parentPomXmlDocument);
		destProject.setPath(parentPath);
		destProject.setArtifactId(project.getArtifactId()+WS);
		destProject.getChilds().clear();
		
		//第二步，开始修改子项目
		for(MavenProject change:changesProject){
			String implPomXml = parentPath+"/"+change.getName()+WS+"/pom.xml";
			Document modulePomXmlDocument = DocumentUtil.readDocument(implPomXml);
			PomXmlWriter.updatePomArtifactId(modulePomXmlDocument, change.getArtifactId()+WS);
			PomXmlWriter.updatePomParentArtifactId(modulePomXmlDocument, project.getArtifactId()+WS);
			
			for(Dependency depen:changes){
					PomXmlWriter.updateDependency(modulePomXmlDocument, depen, WS);
			}
			
			PomXmlWriter.addDependencies("org.apache.cxf", "cxf-api", "2.6.2", modulePomXmlDocument);
			PomXmlWriter.addDependencies("org.apache.cxf", "cxf-rt-transports-http", "2.6.2", modulePomXmlDocument);
			PomXmlWriter.addDependencies("org.apache.cxf", "cxf-rt-frontend-jaxrs", "2.6.2", modulePomXmlDocument);
			PomXmlWriter.addDependencies("wsdl4j", "wsdl4j", "1.6.2", modulePomXmlDocument);
			PomXmlWriter.addDependencies("javax.ws.rs", "jsr311-api", "1.0", modulePomXmlDocument);
			PomXmlWriter.addDependencies("javax.jws", "jsr181-api", "1.0-MR1", modulePomXmlDocument);
			
			DocumentUtil.document2Xml(implPomXml, modulePomXmlDocument);
					
			MavenProject destImpl = change.clone();
			
			destImpl.setPath(parentPath+"/"+change.getName()+WS);
			destImpl.setArtifactId(change.getArtifactId()+WS);
			destImpl.setParent(destProject);
			destProject.getChilds().add(destImpl);
		}
		
		//生成war-ws项目
		Map params = new HashMap();
		params.put("path", parentPath);
		params.put("Project", project);
		params.put("Dependencys", changesProject);
//		VelocityUtil.velocityDirParse("vm/ws/war-ws", parentPath+"war-ws", VelocityUtil.getVelocityContext(params));
		XmlParseUtil.parseXmlAction("xml/ws-security-copy.xml", VelocityUtil.getVelocityContext(params));		
		return destProject;
	}
}
