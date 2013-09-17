package org.openkoala.opencis.jira.service.impl;

public class PasswordDifferentException extends RuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public PasswordDifferentException(){
		super();
	}
	
	public PasswordDifferentException(String message){
		super(message);
	}
	
	public PasswordDifferentException(String message, Throwable cause){
		super(message, cause);
	}

}
