package org.openkoala.opencis.jira.service.impl;

public class ServerAddressBlankException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public ServerAddressBlankException(){
		super();
	}
	
	public ServerAddressBlankException(String message){
		super(message);
	}
	
	public ServerAddressBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
