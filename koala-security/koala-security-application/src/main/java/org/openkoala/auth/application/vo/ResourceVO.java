package org.openkoala.auth.application.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResourceVO extends PartyVO implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = 1248753171821130218L;
	private String identifier;
	private String level;
	private String icon;
	private String typeId;
	private String typeName;
	private String name;
	private String text;
	private String parentId;
	private boolean ischecked;
	private boolean isleaf;
	private boolean isvalid;
	private String desc;
	private String menuType;
	private List<ResourceVO> children = new ArrayList<ResourceVO>();

	public ResourceVO() {

	}
	
	public ResourceVO(String identifier, String typeId, String name, String desc) {
		this.identifier = identifier;
		this.typeId = typeId;
		this.name = name;
		this.desc = desc;
	}



	public boolean isIschecked() {
		return ischecked;
	}

	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}

	public boolean isIsvalid() {
		return isvalid;
	}

	public void setIsvalid(boolean isvalid) {
		this.isvalid = isvalid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public boolean isIsleaf() {
		return isleaf;
	}

	public void setIsleaf(boolean isleaf) {
		this.isleaf = isleaf;
	}

	public List<ResourceVO> getChildren() {
		return children;
	}

	public void setChildren(List<ResourceVO> children) {
		this.children = children;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

}
