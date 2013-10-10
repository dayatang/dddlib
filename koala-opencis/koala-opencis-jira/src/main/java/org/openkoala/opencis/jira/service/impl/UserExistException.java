package org.openkoala.opencis.jira.service.impl;

public class UserExistException extends RuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public UserExistException(){
		super();
	}
	
	public UserExistException(String message){
		super(message);
	}
	
	public UserExistException(String message, Throwable cause){
		super(message, cause);
	}

}
