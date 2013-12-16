package org.openkoala.opencis.exception;

public class CreateUserGroupException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public CreateUserGroupException(){
		super();
	}
	
	public CreateUserGroupException(String message){
		super(message);
	}
	
	public CreateUserGroupException(String message, Throwable cause){
		super(message, cause);
	}

}
