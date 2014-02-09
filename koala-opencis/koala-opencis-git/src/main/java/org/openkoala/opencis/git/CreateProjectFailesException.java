package org.openkoala.opencis.git;

public class CreateProjectFailesException extends GitCISProjectException {

	private static final long serialVersionUID = 4323483932371922430L;

	public CreateProjectFailesException() {
		super();
	}
	
	public CreateProjectFailesException(String message) {
		super(message);
	}
	
	public CreateProjectFailesException(Throwable cause) {
		super(cause);
	}
	
	public CreateProjectFailesException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
