package org.openkoala.opencis.exception;

public class CreateUserFailedException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public CreateUserFailedException(){
		super();
	}
	
	public CreateUserFailedException(String message){
		super(message);
	}
	
	public CreateUserFailedException(String message, Throwable cause){
		super(message, cause);
	}

}
