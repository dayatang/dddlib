package org.openkoala.opencis.exception;

import org.openkoala.opencis.CISClientBaseRuntimeException;

public class ProjectBlankException extends CISClientBaseRuntimeException {

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
