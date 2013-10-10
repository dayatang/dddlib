package org.openkoala.opencis.jira.service.impl;

public class ProjectNameExistException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public ProjectNameExistException(){
		super();
	}
	
	public ProjectNameExistException(String message){
		super(message);
	}
	
	public ProjectNameExistException(String message, Throwable cause){
		super(message, cause);
	}

}
