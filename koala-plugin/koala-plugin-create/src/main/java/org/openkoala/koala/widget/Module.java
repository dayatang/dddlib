package org.openkoala.koala.widget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.annotation.ListFunctionCreate;
import org.openkoala.koala.annotation.MappedFunctionCreate;
import org.openkoala.koala.annotation.ObjectFunctionCreate;
import org.openkoala.koala.exception.KoalaException;

@ObjectFunctionCreate
public class Module implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String moduleName;
	
	@MappedFunctionCreate
	private String moduleType;

	@ListFunctionCreate
	private List<String> functions;

	private List<String> dependencies;
	
	private Security security;
	
	private String projectName;
	
	private String basePackage;
	
	private Monitor monitor;
	
	private GeneralQuery generalQuery;
	
	public String getModuleName() {
		return moduleName;
	}

	public List<String> getFunctions() {
		if(functions==null)functions = new ArrayList<String>();
		if(functions.contains("log4j"))functions.add("log4j");
		return functions;
	}

	public void setFunctions(List<String> functions) {
		this.functions = functions;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public List<String> getDependencies() {
		if (dependencies == null)
			dependencies = new ArrayList<String>();
		return dependencies;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}

	public Security getSecurity() {
		return security;
	}
	
	public String modueNamePackage(){
		return "";
	}

	public void setSecurity(Security security) throws KoalaException {
		if(moduleType.equals("war")==false)throw new KoalaException("只有WEB项目才能配置");
		this.security = security;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getBasePackagePath() {
		return basePackage.replaceAll("\\.", "/");
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	public GeneralQuery getGeneralQuery() {
		return generalQuery;
	}

	public void setGeneralQuery(GeneralQuery generalQuery) {
		this.generalQuery = generalQuery;
	}
}
