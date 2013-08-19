package org.openkoala.koala.createvo;

import java.io.Serializable;
import java.util.List;
/**
 * 某个功能定义
 * @author lingen
 *
 */
public class Function implements Serializable {
	
	private String name;
	
	private String searchGroupId;
	
	private String searchArtifactId;
	
	private String description;
	
	private List<String> modules;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSearchGroupId() {
		return searchGroupId;
	}

	public void setSearchGroupId(String searchGroupId) {
		this.searchGroupId = searchGroupId;
	}

	public String getSearchArtifactId() {
		return searchArtifactId;
	}

	public void setSearchArtifactId(String searchArtifactId) {
		this.searchArtifactId = searchArtifactId;
	}

	public List<String> getModules() {
		return modules;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
