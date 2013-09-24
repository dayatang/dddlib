package org.openkoala.opencis.api;

import java.util.List;

/**
 * 项目相关属性的接口
 * @author lingen
 *
 */
public class Project {

	private String artifactId;
	
	private String projectName;
	
	private String projectPath;
	
	private List<Developer> projectDeveloper;

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public List<Developer> getProjectDeveloper() {
		return projectDeveloper;
	}

	public void setProjectDeveloper(List<Developer> projectDeveloper) {
		this.projectDeveloper = projectDeveloper;
	}

	@Override
	public String toString() {
		return "Project [artifactId=" + artifactId + ", projectName="
				+ projectName + ", projectPath=" + projectPath
				+ ", projectDeveloper=" + projectDeveloper + "]";
	}
}
