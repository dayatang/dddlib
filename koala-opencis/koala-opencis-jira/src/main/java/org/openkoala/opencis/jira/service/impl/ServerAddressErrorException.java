package org.openkoala.opencis.jira.service.impl;

public class ServerAddressErrorException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public ServerAddressErrorException(){
		super();
	}
	
	public ServerAddressErrorException(String message){
		super(message);
	}
	
	public ServerAddressErrorException(String message, Throwable cause){
		super(message, cause);
	}

}
