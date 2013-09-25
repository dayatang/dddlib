package org.openkoala.opencis.git;

public class GitCISProjectException extends GitCISException {

	private static final long serialVersionUID = 4323483932371922430L;

	public GitCISProjectException() {
		super();
	}
	
	public GitCISProjectException(String message) {
		super(message);
	}
	
	public GitCISProjectException(Throwable cause) {
		super(cause);
	}
	
	public GitCISProjectException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
