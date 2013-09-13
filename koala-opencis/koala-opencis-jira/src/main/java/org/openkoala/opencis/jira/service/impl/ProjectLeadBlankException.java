package org.openkoala.opencis.jira.service.impl;

public class ProjectLeadBlankException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public ProjectLeadBlankException(){
		super();
	}
	
	public ProjectLeadBlankException(String message){
		super(message);
	}
	
	public ProjectLeadBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
