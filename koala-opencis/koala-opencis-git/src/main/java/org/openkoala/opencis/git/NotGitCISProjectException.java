package org.openkoala.opencis.git;

public class NotGitCISProjectException extends GitCISProjectException {

	private static final long serialVersionUID = 8181631265134760038L;

	public NotGitCISProjectException() {
		super();
	}
	
	public NotGitCISProjectException(String message) {
		super(message);
	}
	
	public NotGitCISProjectException(Throwable cause) {
		super(cause);
	}
	
	public NotGitCISProjectException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
