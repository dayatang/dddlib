package org.openkoala.opencis.jira.service.impl;

public class RoleNotExistException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public RoleNotExistException(){
		super();
	}
	
	public RoleNotExistException(String message){
		super(message);
	}
	
	public RoleNotExistException(String message, Throwable cause){
		super(message, cause);
	}

}
