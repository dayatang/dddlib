package org.openkoala.opencis.exception;

public class CreateProjectException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public CreateProjectException(){
		super();
	}
	
	public CreateProjectException(String message){
		super(message);
	}
	
	public CreateProjectException(String message, Throwable cause){
		super(message, cause);
	}

}
