package org.openkoala.koala.deploy.webservice.pojo;

import java.util.List;

/**
 * 值对象，一个值对象代表一个需要XML化的对象
 * @author lingen
 *
 */
public class ValueObj {

	private String name;
	
	private String qualifiedName;
	
	private List<String> properties;

	public ValueObj(String name, String qualifiedName, List<String> properties) {
		super();
		this.name = name;
		this.qualifiedName = qualifiedName;
		this.properties = properties;
	}

	public ValueObj() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	public List<String> getProperties() {
		return properties;
	}

	public void setProperties(List<String> properties) {
		this.properties = properties;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((qualifiedName == null) ? 0 : qualifiedName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValueObj other = (ValueObj) obj;
		if (qualifiedName == null) {
			if (other.qualifiedName != null)
				return false;
		} else if (!qualifiedName.equals(other.qualifiedName))
			return false;
		return true;
	}
	
}
