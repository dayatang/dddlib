package org.openkoala.bpm.application.dto;

import java.io.Serializable;

public class SelectOptions implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String text;
	
	private String value;
	
	

	public SelectOptions() {}

	public SelectOptions(String text, String value) {
		super();
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

}
