package org.openkoala.koala.widget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 更新一个项目对应的POJO
 * @author lingen.liu
 * 分为--新增一个或多个模块
 *       删除一个或多个模块
 */
@SuppressWarnings("unchecked")
public class ModuleUpdate implements Serializable{
	
	private static final long serialVersionUID = -8319040556887749226L;

	/**
     * 指定要修改的项目路径
     */
	private String projectPath;
	
	/**
	 * 指定要修改的模块名称
	 */
	private String moduleName;
	
	/**
	 * 指定要新增加或功能
	 */
	private List<String> add;
	
	/**
	 * 指定要删除的功能
	 */
	private List<String> delete;
	
	/**
	 * 项目依赖
	 * @return
	 */
	private List<String> dependencies;

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public List<String> getAdd() {
		if(add == null) {
			return Collections.EMPTY_LIST;
		}
		return add;
	}

	public void setAdd(List<String> add) {
		this.add = add;
	}

	public List<String> getDelete() {
		if(delete == null) {
			return Collections.EMPTY_LIST;
		}
		return delete;
	}

	public void setDelete(List<String> delete) {
		this.delete = delete;
	}
	
	public List<String> getDependencies() {
		if(dependencies==null)dependencies = new ArrayList<String>();
		return dependencies;
	}

	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}
	
}
