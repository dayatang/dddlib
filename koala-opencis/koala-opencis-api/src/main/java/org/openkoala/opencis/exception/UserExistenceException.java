package org.openkoala.opencis.exception;

import org.openkoala.opencis.CISClientBaseRuntimeException;

public class UserExistenceException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public UserExistenceException(){
		super();
	}
	
	public UserExistenceException(String message){
		super(message);
	}
	
	public UserExistenceException(String message, Throwable cause){
		super(message, cause);
	}

}
