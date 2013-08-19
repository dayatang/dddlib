package org.openkoala.gqc.core.domain.utils;

public class SystemDataSourceNotExistException extends RuntimeException {

	private static final long serialVersionUID = 4331400917444670599L;
	
	public SystemDataSourceNotExistException(){
		super();
	}
	
	public SystemDataSourceNotExistException(String message){
		super(message);
	}
	
	public SystemDataSourceNotExistException(Throwable cause){
		super(cause);
	}
	
	public SystemDataSourceNotExistException(String message, Throwable cause){
		super(message, cause);
	}

}
