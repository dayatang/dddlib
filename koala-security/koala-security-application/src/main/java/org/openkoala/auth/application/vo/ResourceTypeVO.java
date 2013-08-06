package org.openkoala.auth.application.vo;

import java.io.Serializable;

/**
 * 资源类型VO
 * 
 * @author Ken
 * @since 2013-03-12 10:39
 * 
 */
public class ResourceTypeVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4725611487130702454L;

	private String id;

	private String name;
	
	private String text;

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
