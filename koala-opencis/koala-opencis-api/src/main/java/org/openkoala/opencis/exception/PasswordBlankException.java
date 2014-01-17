package org.openkoala.opencis.exception;

public class PasswordBlankException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public PasswordBlankException(){
		super();
	}
	
	public PasswordBlankException(String message){
		super(message);
	}
	
	public PasswordBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
