package org.openkoala.opencis.git;

public class GitCISUserException extends GitCISException {

	private static final long serialVersionUID = -8615330773247686623L;

	public GitCISUserException() {
		super();
	}
	
	public GitCISUserException(String message) {
		super(message);
	}
	
	public GitCISUserException(Throwable cause) {
		super(cause);
	}
	
	public GitCISUserException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
