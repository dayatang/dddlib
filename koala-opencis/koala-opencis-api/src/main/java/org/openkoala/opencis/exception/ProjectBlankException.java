package org.openkoala.opencis.exception;

public class ProjectBlankException extends BaseException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public ProjectBlankException(){
		super();
	}
	
	public ProjectBlankException(String message){
		super(message);
	}
	
	public ProjectBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
