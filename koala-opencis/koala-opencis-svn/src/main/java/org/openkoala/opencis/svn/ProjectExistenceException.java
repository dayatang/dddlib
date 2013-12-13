package org.openkoala.opencis.svn;

public class ProjectExistenceException extends BaseException {

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
