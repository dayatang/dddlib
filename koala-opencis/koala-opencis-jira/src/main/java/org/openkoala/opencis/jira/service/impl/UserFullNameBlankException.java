package org.openkoala.opencis.jira.service.impl;

public class UserFullNameBlankException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public UserFullNameBlankException(){
		super();
	}
	
	public UserFullNameBlankException(String message){
		super(message);
	}
	
	public UserFullNameBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
