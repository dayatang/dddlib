package org.openkoala.opencis.exception;

import org.openkoala.opencis.CISClientBaseRuntimeException;

public class HtpasswdFileInvalidException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public HtpasswdFileInvalidException(){
		super();
	}
	
	public HtpasswdFileInvalidException(String message){
		super(message);
	}
	
	public HtpasswdFileInvalidException(String message, Throwable cause){
		super(message, cause);
	}

}
