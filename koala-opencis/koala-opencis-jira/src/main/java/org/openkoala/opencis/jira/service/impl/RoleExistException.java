package org.openkoala.opencis.jira.service.impl;

public class RoleExistException extends RuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public RoleExistException(){
		super();
	}
	
	public RoleExistException(String message){
		super(message);
	}
	
	public RoleExistException(String message, Throwable cause){
		super(message, cause);
	}

}
