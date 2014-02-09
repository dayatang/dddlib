package org.openkoala.koala.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openkoala.koala.widget.Module;
import org.openkoala.koala.widget.Project;

/**
 * 
 * 功能描述：根据DDD理论，判断某个项目中的某个模块的可依赖关系的工具类。	
 *  
 * 创建日期：2013-2-26上午11:18:09     
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved
 * 
 * 作    者：xmfang(xinmin.fang@gmail.com)
 * 
 */
public class ModuleDependencyUtils {
	
	private Project project;
	
	private Module currentModule;
	
	private String moduleLayer;
	
	public ModuleDependencyUtils(Project project, Module currentModule) {
		if (project == null) {
			throw new RuntimeException();
		}
		if (currentModule == null) {
			throw new RuntimeException();
		}
		
		this.project = project;
		this.currentModule = currentModule;
		this.moduleLayer = currentModule.getModuleType();
	}
	
	public ModuleDependencyUtils(Project project, String moduleLayer) {
		if (project == null) {
			throw new RuntimeException();
		}
		if (moduleLayer == null) {
			throw new RuntimeException();
		}
		
		this.project = project;
		this.moduleLayer = moduleLayer;
	}
	
	/**
	 * 根据DDD思想的理论和依赖关系的约束，获得当前模块可以依赖的模块。
	 * @return
	 */
	public List<Module> getCouldDependencyModules() {
		if (moduleLayer.equals(ModuleLayer.infra.name())) {
			return getCouldDependencyModulesByLayer(ModuleLayer.infra.name());
		}
		
		if (moduleLayer.equals(ModuleLayer.bizModel.name())) {
			return getCouldDependencyModulesByLayer(ModuleLayer.infra.name(), ModuleLayer.bizModel.name());
		}
		
		if (moduleLayer.equals(ModuleLayer.applicationInterface.name())) {
			return getCouldDependencyModulesByLayer(ModuleLayer.bizModel.name());
		}
		
		if (moduleLayer.equals(ModuleLayer.applicationImpl.name())) {
			return getCouldDependencyModulesByLayer(ModuleLayer.infra.name(), ModuleLayer.bizModel.name(), 
					ModuleLayer.applicationInterface.name(), ModuleLayer.applicationImpl.name());
		}
		
		if (moduleLayer.equals(ModuleLayer.war.name())) {
			return getCouldDependencyModulesByLayer(ModuleLayer.infra.name(), ModuleLayer.bizModel.name(), 
					ModuleLayer.applicationInterface.name(), ModuleLayer.applicationImpl.name());
		}
		
		return Collections.emptyList();
	}
	
	private List<Module> getCouldDependencyModulesByLayer(String... layer) {
		List<Module> results = new ArrayList<Module>();
		List<Module> allModules = new ArrayList<Module>(project.getModule());
		Set<String> layers = new HashSet<String>(Arrays.asList(layer));
		if (currentModule != null) {
			allModules.remove(currentModule);
		}
		
		for (Module module : allModules) {
			if (currentModule != null && hasDependency(module)) {
				continue;
			}
			
			if (layers.contains(module.getModuleType())) {
				results.add(module);
			}
		}
		
		return results;
	}
	
	/**
	 * 检查某个模块是否与当前模块存在依赖关系，包括间接依赖。
	 * @param module
	 * @return
	 */
	private boolean hasDependency(Module module) {
		if (hasDirectDependency(module)) {
			return true;
		}
		
		for (String moduleName : module.getDependencies()) {
			if (hasDependency(getModuleByName(moduleName))) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean hasDirectDependency(Module module) {
		return module.getDependencies().contains(currentModule.getModuleName());
	}
	
	private Module getModuleByName(String moduleName) {
		for (Module module : project.getModule()) {
			if (module.getModuleName().equals(moduleName)) {
				return module;
			}
		}
		
		return null;
	}

}
