package org.openkoala.opencis.exception;

import org.openkoala.opencis.CISClientBaseRuntimeException;

public class HostBlankException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public HostBlankException(){
		super();
	}
	
	public HostBlankException(String message){
		super(message);
	}
	
	public HostBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
