package org.openkoala.opencis.jira.service.impl;

public class UserNotExistException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public UserNotExistException(){
		super();
	}
	
	public UserNotExistException(String message){
		super(message);
	}
	
	public UserNotExistException(String message, Throwable cause){
		super(message, cause);
	}

}
