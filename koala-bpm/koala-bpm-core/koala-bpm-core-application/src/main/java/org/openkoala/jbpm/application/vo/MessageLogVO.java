package org.openkoala.jbpm.application.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement()
public class MessageLogVO implements Serializable{

	private static final long serialVersionUID = -8042331232641061353L;

	private long id;
	
	private String text;

	private String user;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
