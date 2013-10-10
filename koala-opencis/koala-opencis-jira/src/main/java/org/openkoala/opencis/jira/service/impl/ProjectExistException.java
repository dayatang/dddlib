package org.openkoala.opencis.jira.service.impl;

public class ProjectExistException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public ProjectExistException(){
		super();
	}
	
	public ProjectExistException(String message){
		super(message);
	}
	
	public ProjectExistException(String message, Throwable cause){
		super(message, cause);
	}

}
