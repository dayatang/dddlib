package org.openkoala.opencis.jira.service.impl;

public class ProjectKeyBlankException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public ProjectKeyBlankException(){
		super();
	}
	
	public ProjectKeyBlankException(String message){
		super(message);
	}
	
	public ProjectKeyBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
