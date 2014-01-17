package org.openkoala.opencis.exception;

import org.openkoala.opencis.CISClientBaseRuntimeException;

public class UserBlankException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public UserBlankException(){
		super();
	}
	
	public UserBlankException(String message){
		super(message);
	}
	
	public UserBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
