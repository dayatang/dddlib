package org.openkoala.koala.widget;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.annotation.ObjectFunctionCreate;
import org.openkoala.koala.util.StringUtils;


@ObjectFunctionCreate
public class Project implements Serializable {

	private static final long serialVersionUID = -3534741692550364936L;

	private String path;

	private String groupId = "org.openkoala"; 

	private String artifactId;
	
	private String packaging = "jar";

	private String appName;
	
	private String version = "1.0-SNAPSHOT";

	private List<Module> module;
	
	/**
	 * //数据库实现协议，支持JPA以及Mybatis两种协议
	 * 默认为JPA实现
	 */
	private String dbProtocol = "JPA";
	
	private String description ="Auto Created Project";
	
	/**
	 * 支持SpringMVC  以及 Struts2MVC 两种模式
	 */
	private String mvcProtocol = "SpringMVC";

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public List<Module> getModule() {
		if(module==null)module =  new ArrayList<Module>();
		return module;
	}

	public void setModule(List<Module> module) {
		this.module = module;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void initModulePrefix(String modulePrefix) {
		renameModulesName(modulePrefix);
	}

	private void renameModulesName(String modulePrefix) {
		for (Module theModule : getModule()) {
			String oldModuleName = theModule.getModuleName();
			String newModuleName = modulePrefix + "-" + oldModuleName;
			theModule.setModuleName(newModuleName);
			
			refreshModulesDependency(oldModuleName, newModuleName);
		}
	}

	private void refreshModulesDependency (String oldModuleName, String newModuleName) {
		for (Module theModule : getModule()) {
			List<String> dependencies = new ArrayList<String>(theModule.getDependencies());
			for (String dependencyModuleName : dependencies) {
				if (dependencyModuleName.equals(oldModuleName)) {
					theModule.getDependencies().remove(oldModuleName);
					theModule.getDependencies().add(newModuleName);
				}
			}
		}
	}
	
	/**
	 * 删除模块
	 * @param module
	 */
	public void removeModule(final Module moduleForRemove) {
		module.remove(moduleForRemove);
		removeDependency(moduleForRemove);
	}
	
	public String getDbProtocol() {
		return dbProtocol;
	}

	public void setDbProtocol(String dbProtocol) {
		this.dbProtocol = dbProtocol;
	}

	/**
	 * 当模块被删除时，删除其他模块对其的依赖关系
	 * @param moduleForRemove
	 */
	private void removeDependency(final Module moduleForRemove) {
		for (Module theModule : module) {
			theModule.getDependencies().remove(moduleForRemove.getModuleName());
		}
	}
	
	public String getGroupPackage(){
		return this.groupId.replaceAll("\\.", "/");
	}
	
	public String getMvcProtocol() {
		return mvcProtocol;
	}

	public void setMvcProtocol(String mvcProtocol) {
		this.mvcProtocol = mvcProtocol;
	}

	public void initSSJProject(){
		getModule().clear();
		//初始化一个infra层
		Module infra = new Module();
		infra.setModuleType("infra");
		infra.setModuleName("infra");
		infra.setProjectName(appName);
		infra.setBasePackage(getGroupId() + "." + getPackageName() + ".infra");
		getModule().add(infra);
		
		//初始化一个领域层
		Module bizModel = new Module();
		bizModel.setModuleType("bizModel");
		bizModel.setModuleName("core");
		bizModel.setProjectName(appName);
		bizModel.setBasePackage(getGroupId() + "." + getPackageName() + ".core");
		bizModel.getDependencies().add("infra");
		getModule().add(bizModel);
		
		//初始化一个接口层
		Module applicationInterface = new Module();
		applicationInterface.setModuleType("applicationInterface");
		applicationInterface.setModuleName("application");
		applicationInterface.setProjectName(appName);
		applicationInterface.setBasePackage(getGroupId() + "." + getPackageName() + ".application");
		applicationInterface.getDependencies().add("core");
		getModule().add(applicationInterface);
		
		//初始化一个实现层
		Module applicationImpl = new Module();
		applicationImpl.setModuleName("applicationImpl");
		applicationImpl.setProjectName(appName);
		applicationImpl.setModuleType("applicationImpl");
		applicationImpl.setBasePackage(getGroupId() + "." + getPackageName() + ".application.impl");
		applicationImpl.getDependencies().add("application");
		applicationImpl.getDependencies().add("core");
		applicationImpl.getDependencies().add("infra");
		getModule().add(applicationImpl);
		
		//初始化一个WEB层
		Module war = new Module();
		war.setModuleName("web");
		war.setProjectName(appName);
		war.setBasePackage(getGroupId() + "." + getPackageName() + ".web");
		war.setModuleType("war");
		//war.getFunctions().add("");
		war.getDependencies().add("applicationImpl");
		war.getDependencies().add("application");
		war.getDependencies().add("infra");
		getModule().add(war);
	}
	
	public String getPackageName() {
		return StringUtils.handleSpecialCharactersInName(artifactId);
	}
	
	/**
	 * Spring 扫描包列表
	 * @return
	 */
	public List<String> getScanPackages(){
		List<String> packages = new ArrayList<String>();
//		packages.add(groupId + "."+artifactId+".*");
		if(module != null){
			for (Module mod : module) {
				if (mod.getModuleType().equals("bizModel")) {
					packages.add(mod.getBasePackage());
				}
				
				if(mod.getSecurity() != null){
					packages.add("org.openkoala.koala.auth.core.*");
				}
				if(mod.getMonitor() != null && "all".equals(mod.getMonitor().getInstallType())){
					packages.add("org.openkoala.koala.monitor.domain");
					packages.add("org.openkoala.koala.config.domain");
				}
				if(mod.getGeneralQuery() != null){
					packages.add("org.openkoala.gqc.core.domain");
				}
			}
		}
		return packages;
	}

}
