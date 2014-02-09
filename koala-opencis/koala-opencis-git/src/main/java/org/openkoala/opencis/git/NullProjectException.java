package org.openkoala.opencis.git;

public class NullProjectException extends GitCISProjectException {

	private static final long serialVersionUID = 4323483932371922430L;

	public NullProjectException() {
		super();
	}
	
	public NullProjectException(String message) {
		super(message);
	}
	
	public NullProjectException(Throwable cause) {
		super(cause);
	}
	
	public NullProjectException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
