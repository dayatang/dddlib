package org.openkoala.opencis.exception;

import org.openkoala.opencis.CISClientBaseRuntimeException;

public class UserOrPasswordErrorException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public UserOrPasswordErrorException(){
		super();
	}
	
	public UserOrPasswordErrorException(String message){
		super(message);
	}
	
	public UserOrPasswordErrorException(String message, Throwable cause){
		super(message, cause);
	}

}
