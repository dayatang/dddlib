package org.openkoala.opencis.exception;


public class CISClientBaseRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public CISClientBaseRuntimeException(){
		super();
	}
	
	public CISClientBaseRuntimeException(String message){
		super(message);
	}
	
	public CISClientBaseRuntimeException(String message, Throwable cause){
		super(message, cause);
	}

}
