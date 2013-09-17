package org.openkoala.opencis.jira.service.impl;

public class PasswordBlankException extends RuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public PasswordBlankException(){
		super();
	}
	
	public PasswordBlankException(String message){
		super(message);
	}
	
	public PasswordBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
