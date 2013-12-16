package org.openkoala.opencis.exception;

public class UserBlankException extends BaseException {

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
