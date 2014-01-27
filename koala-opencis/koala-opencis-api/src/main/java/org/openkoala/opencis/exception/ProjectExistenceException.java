package org.openkoala.opencis.exception;

import org.openkoala.opencis.CISClientBaseRuntimeException;

public class ProjectExistenceException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public ProjectExistenceException(){
		super();
	}
	
	public ProjectExistenceException(String message){
		super(message);
	}
	
	public ProjectExistenceException(String message, Throwable cause){
		super(message, cause);
	}

}
