package org.openkoala.opencis.exception;

import org.openkoala.opencis.CISClientBaseRuntimeException;

public class RoleBlankException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public RoleBlankException(){
		super();
	}
	
	public RoleBlankException(String message){
		super(message);
	}
	
	public RoleBlankException(String message, Throwable cause){
		super(message, cause);
	}

}
