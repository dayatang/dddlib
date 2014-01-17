package org.openkoala.opencis.exception;

import org.openkoala.opencis.CISClientBaseRuntimeException;

public class HostCannotConnectException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public HostCannotConnectException(){
		super();
	}
	
	public HostCannotConnectException(String message){
		super(message);
	}
	
	public HostCannotConnectException(String message, Throwable cause){
		super(message, cause);
	}

}
