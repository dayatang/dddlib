package org.openkoala.opencis.exception;

public class RemoveProjectException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public RemoveProjectException(){
		super();
	}
	
	public RemoveProjectException(String message){
		super(message);
	}
	
	public RemoveProjectException(String message, Throwable cause){
		super(message, cause);
	}

}
