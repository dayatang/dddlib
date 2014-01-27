package org.openkoala.koala.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.openkoala.koala.action.xml.DocumentUtil;
import org.openkoala.koala.action.xml.PomXmlReader;
import org.openkoala.koala.action.xml.PomXmlWriter;
import org.openkoala.koala.action.xml.XPathQueryUtil;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.queryvo.Function;
import org.openkoala.koala.queryvo.TypeDef;
import org.openkoala.koala.widget.Module;
import org.openkoala.koala.widget.Project;

/**
 * 本类用来分析产生Project
 * @author lingen.liu
 *
 */
public class ProjectQueryUtil {
	
	
	//private static final String POM_XMLS = "http://maven.apache.org/POM/4.0.0";
	
	
	/**
	 * 查询某个模块是否是子模块
	 * @param path
	 * @return
	 * @throws KoalaException 
	 * @throws DocumentException 
	 */
	public static Module getModule(String path,Project project) throws KoalaException{
		path=path.replaceAll("\\\\", "/");
		//如果有parent元素，则证明是子模块
		//return false;
		String modulePom = path+"/pom.xml";
		Document document = DocumentUtil.readDocument(modulePom);
		Element parent = XPathQueryUtil.querySingle(PomXmlWriter.POM_XMLS, "/xmlns:project/xmlns:parent", document);
		if(parent==null)throw new KoalaException("这不是一个子项目");
		else{
			String name = PomXmlReader.queryText("/xmlns:project/xmlns:name",document);
			for(Module module:project.getModule()){
				if(module.getModuleName().equals(name))return module;
			}
		}
		return null;
	}
	
	/**
	 * 传入一个路径，判断这个路径代表的是否是MAVEN是最父级项目
	 * @param path
	 * @return
	 * @throws KoalaException
	 */
	public static boolean isParentProject(String path) throws KoalaException{
		path=path.replaceAll("\\\\", "/");
		if(path.endsWith("/"))path=path.substring(0,-1);
		String projectPom = path+"/pom.xml";
		Document document = DocumentUtil.readDocument(projectPom);
		
		List<Element> parents = XPathQueryUtil.query(PomXmlReader.POM_XMLS, "/xmlns:project/xmlns:parent", document);
		
		if(parents==null || parents.size()==0)return true;
		return false;
	}
	/**
	 * 查询某个模块是否是父项目
	 * @param path
	 * @return
	 */
	public static Project getProject(String path) throws KoalaException{
		//return false;
		path=path.replaceAll("\\\\", "/");
		if(path.endsWith("/"))path=path.substring(0,-1);
		String projectPom = path+"/pom.xml";
		Document document = DocumentUtil.readDocument(projectPom);
		
		List<Element> parents = XPathQueryUtil.query(PomXmlReader.POM_XMLS, "/xmlns:project/xmlns:parent", document);
		
		while(parents!=null && parents.size()>0){
			path = path.substring(0,path.lastIndexOf("/"));
			projectPom = path+"/pom.xml";
			document = DocumentUtil.readDocument(projectPom);
			parents = XPathQueryUtil.query(PomXmlReader.POM_XMLS, "/xmlns:project/xmlns:parent", document);
		}
		return parseProject(path);
	}
	
	
	/**
	 * 传入一个项目PATH，分析获得Project
	 * @param path
	 * @return
	 * @throws KoalaException 
	 * @throws DocumentException 
	 */
	public static Project parseProject(String path) throws KoalaException{
		path=path.replaceAll("\\\\", "/");
		Project project = new Project();
		project.setPath(new File(path).getParent());
		//读取project下的pom.xml
		String projectPom = path+"/pom.xml";
		Document projectDocument = DocumentUtil.readDocument(projectPom);
		
		//设置groupId
		project.setGroupId(PomXmlReader.queryText("/xmlns:project/xmlns:groupId",projectDocument));
		project.setAppName(PomXmlReader.queryText("/xmlns:project/xmlns:name",projectDocument));
		project.setArtifactId(PomXmlReader.queryText("/xmlns:project/xmlns:artifactId",projectDocument));
		project.setDescription(PomXmlReader.queryText("/xmlns:project/xmlns:description",projectDocument));
		project.setPackaging("pom");
		project.setVersion(PomXmlReader.queryText("/xmlns:project/xmlns:version",projectDocument));
		
		List<String> modules = PomXmlReader.queryListText("/xmlns:project/xmlns:modules/xmlns:module", projectDocument);
		List<Module> moduleList = new ArrayList<Module>();
		
		TypeDef def = TypeDef.getInstance();
		List<Function> functions = def.getFunction();
		
		for(String moduleString:modules){
			Module module = new Module();
			module.setModuleName(moduleString);
			//读取module的pom
			String modulePath = path+"/"+moduleString+"/pom.xml";
			Document moduleDocument = DocumentUtil.readDocument(modulePath);
			//分析MODULE的所属的层
			//WEB层的依据 packaging为WAR
			//<artifactId>dayatang-commons-domain</artifactId>  core
			//TODO 待完善
			String moduleType = PomXmlReader.queryText("/xmlns:project/xmlns:properties/xmlns:project.type",moduleDocument);
			module.setModuleType(moduleType);
			String basePackage = PomXmlReader.queryText("/xmlns:project/xmlns:properties/xmlns:base.package",moduleDocument);
			module.setBasePackage(basePackage);
			for(Function function:functions){
				if(PomXmlReader.isDependencyExists(function.getSearchGroupId(), function.getSearchArtifactId(), moduleDocument))
					module.getFunctions().add(function.getName());
			}
			moduleList.add(module);
		}
		//遍历每个Module,获取它们之间的依赖关第
		for(Module module:moduleList){
			List<String> dependencies = new ArrayList<String>();
			String modulePath = path+"/"+module.getModuleName()+"/pom.xml";
			Document moduleDocument = DocumentUtil.readDocument(modulePath);
			for(Module depenModule:moduleList){
				if(PomXmlReader.isDependencyExists(project.getGroupId()+"."+project.getAppName(),depenModule.getModuleName(), moduleDocument)){
					dependencies.add(depenModule.getModuleName());
				}
			}
			module.setDependencies(dependencies);
		}
		project.setModule(moduleList);
		return project;
	}
	
}
