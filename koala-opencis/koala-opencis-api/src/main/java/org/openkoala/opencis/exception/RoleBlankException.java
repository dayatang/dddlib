package org.openkoala.opencis.exception;

public class RoleBlankException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public RoleBlankException(){
		super();
	}
	
	public RoleBlankException(String message){
		super(message);
	}
	
	public RoleBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
