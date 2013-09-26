package org.openkoala.koala.widget;

import java.io.Serializable;

/**
 * 对存在的项目，新增加一个模块
 * 
 * @author lingen.liu
 * 
 */
public class ModuleAdd implements Serializable {

	private static final long serialVersionUID = 1966938740747709627L;

	/**
	 * 指定要新增加的projectPath
	 */
	private String projectPath;

	/**
	 * 指定新增加的模块
	 */
	private Module module;

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

}
