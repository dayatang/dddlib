package org.openkoala.opencis.jira.service.impl;

public class RoleNameBlankException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public RoleNameBlankException(){
		super();
	}
	
	public RoleNameBlankException(String message){
		super(message);
	}
	
	public RoleNameBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
