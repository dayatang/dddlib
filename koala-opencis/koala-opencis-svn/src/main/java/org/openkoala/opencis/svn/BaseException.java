package org.openkoala.opencis.svn;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public BaseException(){
		super();
	}
	
	public BaseException(String message){
		super(message);
	}
	
	public BaseException(String message, Throwable cause){
		super(message, cause);
	}

}
