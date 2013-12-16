package org.openkoala.opencis.exception;

public class UserExistenceException extends BaseException {

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
