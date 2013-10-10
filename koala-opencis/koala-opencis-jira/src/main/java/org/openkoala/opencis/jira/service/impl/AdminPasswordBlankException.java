package org.openkoala.opencis.jira.service.impl;

public class AdminPasswordBlankException extends RuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public AdminPasswordBlankException(){
		super();
	}
	
	public AdminPasswordBlankException(String message){
		super(message);
	}
	
	public AdminPasswordBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
