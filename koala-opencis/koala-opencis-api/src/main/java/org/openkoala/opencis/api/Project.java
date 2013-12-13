package org.openkoala.opencis.api;

import java.util.List;
import java.util.Map;

/**
 * 项目相关属性的接口
 * @author lingen
 *
 */
public class Project {

	private String artifactId;
	
	private String projectName;
	
	private String projectKey;
	
	/**
	 * 项目负责人
	 */
	private String projectLead;
	
	private String projectPath;
	
	private String description;
	
	private List<Developer> developers;
	
	private Map<String, Object> nodeElements;

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

	public List<Developer> getDevelopers() {
		return developers;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
	
	public String getProjectLead() {
		return projectLead;
	}

	public void setProjectLead(String projectLead) {
		this.projectLead = projectLead;
	}

	public void setDevelopers(List<Developer> developers) {
		this.developers = developers;
	}
	
	public Map<String, Object> getNodeElements() {
		return nodeElements;
	}

	public void setNodeElements(Map<String, Object> nodeElements) {
		this.nodeElements = nodeElements;
	}

	@Override
	public String toString() {
		return "Project [artifactId=" + artifactId + ", projectName="
				+ projectName + ", projectPath=" + projectPath
				+ ", projectDeveloper=" + developers + "]";
	}
}
