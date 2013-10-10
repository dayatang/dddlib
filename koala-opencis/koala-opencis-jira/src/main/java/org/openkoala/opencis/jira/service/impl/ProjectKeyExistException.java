package org.openkoala.opencis.jira.service.impl;

public class ProjectKeyExistException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public ProjectKeyExistException(){
		super();
	}
	
	public ProjectKeyExistException(String message){
		super(message);
	}
	
	public ProjectKeyExistException(String message, Throwable cause){
		super(message, cause);
	}

}
