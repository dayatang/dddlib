package org.openkoala.opencis.jira.service.impl;

public class UserPermissionNotEnoughException extends RuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public UserPermissionNotEnoughException(){
		super();
	}
	
	public UserPermissionNotEnoughException(String message){
		super(message);
	}
	
	public UserPermissionNotEnoughException(String message, Throwable cause){
		super(message, cause);
	}

}
