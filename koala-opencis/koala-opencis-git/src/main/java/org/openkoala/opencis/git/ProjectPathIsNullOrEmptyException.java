package org.openkoala.opencis.git;

public class ProjectPathIsNullOrEmptyException extends GitCISProjectException {

	private static final long serialVersionUID = 5618184349365129971L;

	public ProjectPathIsNullOrEmptyException() {
		super();
	}
	
	public ProjectPathIsNullOrEmptyException(String message) {
		super(message);
	}
	
	public ProjectPathIsNullOrEmptyException(Throwable cause) {
		super(cause);
	}
	
	public ProjectPathIsNullOrEmptyException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
