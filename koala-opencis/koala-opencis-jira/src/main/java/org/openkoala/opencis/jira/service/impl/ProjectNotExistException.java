package org.openkoala.opencis.jira.service.impl;

public class ProjectNotExistException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public ProjectNotExistException(){
		super();
	}
	
	public ProjectNotExistException(String message){
		super(message);
	}
	
	public ProjectNotExistException(String message, Throwable cause){
		super(message, cause);
	}

}
