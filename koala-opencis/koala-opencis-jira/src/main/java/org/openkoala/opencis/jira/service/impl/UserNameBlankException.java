package org.openkoala.opencis.jira.service.impl;

public class UserNameBlankException extends RuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public UserNameBlankException(){
		super();
	}
	
	public UserNameBlankException(String message){
		super(message);
	}
	
	public UserNameBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
