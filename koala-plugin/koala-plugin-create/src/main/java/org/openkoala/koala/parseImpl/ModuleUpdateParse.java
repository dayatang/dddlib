package org.openkoala.koala.parseImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.openkoala.koala.action.xml.DocumentUtil;
import org.openkoala.koala.action.xml.PomXmlWriter;
import org.openkoala.koala.annotation.parse.Parse;
import org.openkoala.koala.annotation.parse.ParseListFunctionCreate;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.parse.XML2ObjectUtil;
import org.openkoala.koala.util.ProjectQueryUtil;
import org.openkoala.koala.widget.Module;
import org.openkoala.koala.widget.ModuleRemove;
import org.openkoala.koala.widget.ModuleUpdate;
import org.openkoala.koala.widget.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对某一个模块进行更新，新增或删除某些功能
 * @author lingen.liu
 *
 */
public class ModuleUpdateParse {

	private static final Logger logger = LoggerFactory.getLogger(ModuleUpdateParse.class);
	
	private Project project;
	
	private ModuleUpdate moduleUpdate;
	
	private List params;
	
	private Module module;
	
	
	public ModuleUpdateParse(ModuleUpdate moduleUpdate) {
		super();
		try {
			project = ProjectQueryUtil.parseProject(moduleUpdate.getProjectPath());
		} catch (KoalaException e) {
			e.printStackTrace();
		}
		this.moduleUpdate = moduleUpdate;
		for(Module module:project.getModule()){
			if(moduleUpdate.getModuleName().equals(module.getModuleName()))
				this.module = module;
		}
		params = new ArrayList();
		params.add(project);
		params.add(module);
		
	}

	/**
	 * 
	 * @param project
	 * @param module
	 * @param t
	 * @throws Exception
	 */
	public void parse() throws Exception {
		List<String> adds = this.moduleUpdate.getAdd();
		for(String remove : this.moduleUpdate.getDelete()){
			removeProcess(remove);
		}
		
		for(String add : adds){
			addProcess(add);
		}
		//刷新Module的依赖
		refreshDependencies(moduleUpdate.getDependencies());
	}
	
	/**
	 * 刷新项目的依赖
	 * @param dependency
	 * @throws KoalaException 
	 */
	private void refreshDependencies(List<String> dependencise) throws KoalaException{
		//读取指定项目的pom.xml
		String pomPath = this.project.getPath() + "/" + this.project.getAppName() + "/" + this.module.getModuleName() + "/pom.xml";
		Document pomDocument = DocumentUtil.readDocument(pomPath);
		//清除以前的依赖
		for(Module module:project.getModule()){
			PomXmlWriter.removeDependencies(project.getGroupId()+"."+project.getArtifactId(), module.getModuleName(), pomDocument);
		}
		//重新加入新的依赖
		for(String dependency:dependencise){
			PomXmlWriter.addDependencies(project.getGroupId()+"."+project.getArtifactId(),dependency, project.getVersion(), pomDocument);
		}
		
		DocumentUtil.document2Xml(pomPath, pomDocument);
	}
	
	/**
	 * 给某个项目新增某个功能
	 * @param addType
	 * @throws Exception
	 */
	private void addProcess(String addType) throws Exception{
		logger.info("新增功能"+addType);
		//优先使用Boolean类型进行解析
		Parse parse = new ParseListFunctionCreate();
		List<String> list = new ArrayList<String>();
		list.add(addType);
		parse.initParms(params, addType, list);
		parse.process();
		
		//读取指定项目的pom.xml
		String pomPath = this.project.getPath() + "/" + this.project.getAppName() + "/" + this.module.getModuleName() + "/pom.xml";
		Document pomDocument = DocumentUtil.readDocument(pomPath);
		//读取当前功能对应的依赖定义 
		Document dependencies = DocumentUtil.readDocument("xml/dependencies/"+addType+"-dependencies.xml");
		List<Element> dependencyList = dependencies.getRootElement().elements("dependency");
		//将当前功能的依赖定义写入到pom.xml中
		PomXmlWriter.addDependencies(dependencyList, pomDocument);
		DocumentUtil.document2Xml(pomPath, pomDocument);
	}
	
	/**
	 * 将项目中的的某些功能删除
	 * @param removeType
	 * @throws Exception
	 */
	private void removeProcess(String removeType) throws Exception{
		logger.info("删除功能"+removeType);
		//读取要删除的文件的定义
		String removePath = "xml/remove/"+removeType+"-remove.xml";
		XML2ObjectUtil util = XML2ObjectUtil.getInstance();
		ModuleRemove remove = (ModuleRemove)util.processParse(removePath);
		//删除定义的文件
		for(String filePath:remove.getFiles()){
			String path = this.project.getPath()+"/"+this.project.getAppName()+"/"+this.module.getModuleName()+"/"+filePath;
			File file = new File(path);
			logger.info("移除文件"+path);
			file.delete();
		}
		//读取指定项目的pom.xml
		String pomPath = this.project.getPath() + "/" + this.project.getAppName() + "/" + this.module.getModuleName() + "/pom.xml";
		Document pomDocument = DocumentUtil.readDocument(pomPath);
		//读取当前功能对应的依赖定义 
		Document dependencies = DocumentUtil.readDocument("xml/dependencies/"+removeType+"-dependencies.xml");
		List<Element> dependencyList = dependencies.getRootElement().elements("dependency");
		
		//将当前功能的依赖定义写入到pom.xml中
		for(Element element:dependencyList){
			String groupId = element.elementText("groupId");
			String artifactId = element.elementText("artifactId");
			PomXmlWriter.removeDependencies(groupId, artifactId, pomDocument);
		}
		DocumentUtil.document2Xml(pomPath, pomDocument);
	}
}