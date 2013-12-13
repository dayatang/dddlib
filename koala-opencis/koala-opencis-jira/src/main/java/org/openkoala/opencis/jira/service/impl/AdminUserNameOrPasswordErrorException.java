package org.openkoala.opencis.jira.service.impl;

public class AdminUserNameOrPasswordErrorException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public AdminUserNameOrPasswordErrorException(){
		super();
	}
	
	public AdminUserNameOrPasswordErrorException(String message){
		super(message);
	}
	
	public AdminUserNameOrPasswordErrorException(String message, Throwable cause){
		super(message, cause);
	}

}
