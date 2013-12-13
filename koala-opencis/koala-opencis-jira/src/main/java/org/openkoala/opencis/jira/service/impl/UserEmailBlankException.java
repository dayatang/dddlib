package org.openkoala.opencis.jira.service.impl;

public class UserEmailBlankException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public UserEmailBlankException(){
		super();
	}
	
	public UserEmailBlankException(String message){
		super(message);
	}
	
	public UserEmailBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
