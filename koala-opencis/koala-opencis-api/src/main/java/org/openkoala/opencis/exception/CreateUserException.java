package org.openkoala.opencis.exception;

public class CreateUserException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public CreateUserException(){
		super();
	}
	
	public CreateUserException(String message){
		super(message);
	}
	
	public CreateUserException(String message, Throwable cause){
		super(message, cause);
	}

}
