package org.openkoala.opencis.jira.service.impl;

public class AdminUserNameBlankException extends RuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public AdminUserNameBlankException(){
		super();
	}
	
	public AdminUserNameBlankException(String message){
		super(message);
	}
	
	public AdminUserNameBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
