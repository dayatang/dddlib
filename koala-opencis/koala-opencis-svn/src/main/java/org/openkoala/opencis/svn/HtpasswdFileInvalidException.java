package org.openkoala.opencis.svn;

public class HtpasswdFileInvalidException extends BaseException {

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
