package org.openkoala.koala.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.openkoala.koala.action.xml.DocumentUtil;
import org.openkoala.koala.action.xml.PomXmlReader;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.exception.ProjectParseException;
import org.openkoala.koala.pojo.MavenProject;
import org.openkoala.koala.pojo.ModuleType;

/**
 * 
 * 
 * @description 项目分析工具
 *  
 * @date：      2013-8-26   
 * 
 * @version     Copyright (c) 2013 Koala All Rights Reserved
 * 
 * @author      lingen.liu  <a href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a>
 */
public class ProjectParseUtil {
	
	private final static String SRC_MAIN_JAVA="src/main/java/";
	
	private final static String SRC_MAIN_RESOURCES="src/main/resources/";
	
	private final static String SRC_TEST_JAVA="src/test/java/";
	
	private final static String SRC_TEST_RESOURCES="src/test/resources/";
	
	public static MavenProject parseProject(String path) throws KoalaException{
		String pomXml= path+"/pom.xml";
		if(new File(pomXml).exists()==false)throw new ProjectParseException(path+"不是一个有效的maven项目，pom.xml未找到");
		MavenProject project  = new MavenProject();
		project.setPath(path);
		parseProject(path,project,null);
		return project;
	}
	
	static void parseProject(String path,MavenProject project,MavenProject parent) throws KoalaException{
		String pomXml= path+"/pom.xml";
		if(new File(pomXml).exists()==false)throw new ProjectParseException(path+"不是一个有效的maven项目，pom.xml未找到");
		project.setPath(path);
		if(parent!=null){
			parent.getChilds().add(project);
			project.setParent(parent);
		}
		
		Document document = DocumentUtil.readDocument(pomXml);
		project.setName(PomXmlReader.queryText("/xmlns:project/xmlns:name", document));
		project.setGroupId(PomXmlReader.queryText("/xmlns:project/xmlns:groupId", document));
		project.setArtifactId(PomXmlReader.queryText("/xmlns:project/xmlns:artifactId", document));
		project.setVersion(PomXmlReader.queryText("/xmlns:project/xmlns:version", document));
		project.setPackaging(PomXmlReader.queryText("/xmlns:project/xmlns:packaging", document));
		project.setProperties(PomXmlReader.queryProperties(document));
		project.setDependencies(PomXmlReader.queryDependency(document));

		if(project.getName()==null)project.setName(project.getArtifactId());
		if(project.getGroupId()==null)project.setGroupId(project.getParent().getGroupId());
		
		//分析每个项目的类型
		String moduleType = project.getProperties().get("project.type");
		if(moduleType!=null && !moduleType.trim().equals("")){
			if("ear".equals(moduleType))project.setType(ModuleType.Ear);
			else if("bizModel".equals(moduleType))project.setType(ModuleType.BizModel);
			else if("applicationInterface".equals(moduleType))project.setType(ModuleType.Application);
			else if("applicationImpl".equals(moduleType))project.setType(ModuleType.Impl);
			else if("infra".equals(moduleType))project.setType(ModuleType.Infra);
			else if("conf".equals(moduleType))project.setType(ModuleType.conf);
		}
		
		if(project.getType()==null){
			if("war".equals(project.getPackaging()))project.setType(ModuleType.War);
			else if(PomXmlReader.isBizModel(document))project.setType(ModuleType.BizModel);
			else if(PomXmlReader.isInterface(project))project.setType(ModuleType.Application);
			else if(PomXmlReader.isImpl(document))project.setType(ModuleType.Impl);
			else if(PomXmlReader.isEar(document))project.setType(ModuleType.Ear);
			else project.setType(ModuleType.Infra);
		}
		
		project.setSrcMainJavas(getSrcMainJava(project.getPath()+"/src/main/java/"));
		project.setSrcMainResources(getSrcMainResources(project.getPath()+"/src/main/resources"));
		project.setSrcTestJavas(getSrcTestJava(project.getPath()+"/src/test/java/"));
		project.setSrcTestResources(getSrcTestResources(project.getPath()+"/src/test/resources"));
		List<String> modules = PomXmlReader.queryModules(document);
		for(String module:modules){
			parseProject(path+"/"+module,new MavenProject(),project);
		}
	}
	
	static List<String> getSrcMainJava(String path){
		return getSrcFiles(path,new String[]{".java"},SRC_MAIN_JAVA);
	}
	
	static List<String> getSrcMainResources(String path){
		return getSrcFiles(path,new String[]{".xml",".properties"},SRC_MAIN_RESOURCES);
	}
	
	static List<String> getSrcTestJava(String path){
		return getSrcFiles(path,new String[]{".java"},SRC_TEST_JAVA);
	}
	
	static List<String> getSrcTestResources(String path){
		return getSrcFiles(path,new String[]{".xml",".properties"},SRC_TEST_RESOURCES);
	}
	
	static List<String> getSrcFiles(String path,String[] suffixs,String dot){
		List<String> files = new ArrayList<String>();
		 File file = new File(path);
		 File[] childFiles = file.listFiles();
		 if(file.exists()==false)return files;
		 for(File childFile:childFiles){
			 if(childFile.isFile()){
				boolean add = false;
				for(String suffix:suffixs){
					if(childFile.getName().endsWith(suffix))add=true;
				}
				if(add){
					String childPath = childFile.getPath().replaceAll("\\\\", "/");
					if(childPath.indexOf(dot)!=-1){
						files.add(childPath.substring(childPath.indexOf(dot)+dot.length()));
					}
				}
			 }else{
				 files.addAll(getSrcFiles(path+"/"+childFile.getName(),suffixs,dot));
			 }
		 }
		return files;
	}
	
}
