package org.openkoala.jbpm.application.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
public class ProcessVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3537019144264106661L;

	private String id;//流程ID
	
	private String name;//流程名
	
	private String version;//流程版本号
	
	private boolean isActive;//流程是否为激活状态
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
