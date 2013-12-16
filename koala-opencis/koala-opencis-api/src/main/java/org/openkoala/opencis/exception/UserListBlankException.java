package org.openkoala.opencis.exception;

public class UserListBlankException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public UserListBlankException(){
		super();
	}
	
	public UserListBlankException(String message){
		super(message);
	}
	
	public UserListBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
