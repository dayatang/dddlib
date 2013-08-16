package org.openkoala.koala.jbpm.jbpmDesigner.application.vo;

import java.util.List;

public class PackageVO {
	
	private String text;
	
	private String uuid;
	
	private String description;
	
	private String[] children = new String[]{};
	
	private List<Bpmn2> bpmn2s;
	
	private String type = "package";
	
	private String isexpand = "false";
	
	

	public String getIsexpand() {
	
		return isexpand;
	}

	public void setIsexpand(String isexpand) {
	
		this.isexpand = isexpand;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public List<Bpmn2> getBpmn2s() {
		return bpmn2s;
	}

	public void setBpmn2s(List<Bpmn2> bpmn2s) {
		this.bpmn2s = bpmn2s;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String[] getChildren() {
		return children;
	}

	public void setChildren(String[] children) {
		this.children = new String[children.length];
		System.arraycopy(children, 0, this.children, 0, children.length);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
