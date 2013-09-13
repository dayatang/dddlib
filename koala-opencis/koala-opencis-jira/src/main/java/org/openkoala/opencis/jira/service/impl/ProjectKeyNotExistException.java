package org.openkoala.opencis.jira.service.impl;

public class ProjectKeyNotExistException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public ProjectKeyNotExistException(){
		super();
	}
	
	public ProjectKeyNotExistException(String message){
		super(message);
	}
	
	public ProjectKeyNotExistException(String message, Throwable cause){
		super(message, cause);
	}

}
