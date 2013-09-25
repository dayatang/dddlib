package org.openkoala.opencis.git.impl;

import java.util.List;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

public class GitCISProject implements Project {

	private String artifactId;
	
	private String projectName;
	
	private String projectPath;
	
	private String description;
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Developer> getProjectDeveloper() {
		return projectDeveloper;
	}

	public void setProjectDeveloper(List<Developer> projectDeveloper) {
		this.projectDeveloper = projectDeveloper;
	}

}
