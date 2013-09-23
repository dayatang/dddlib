package org.openkoala.opencis.api;

import java.util.List;

/**
 * 项目相关属性的接口
 * @author lingen
 *
 */
public interface Project {

	/**
	 *  返回项目的artifactId
	 * @return
	 */
	public String getArtifactId();
	
	/**
	 * 返回项目的名称
	 * @return
	 */
	public String getProjectName();
	
	/**
	 * 返回项目的路径
	 * @return
	 */
	public String getProjectPath();
	
	/**
	 * 返回项目的开发者列表
	 * @return
	 */
	public List<Developer> getProjectDeveloper();
}
