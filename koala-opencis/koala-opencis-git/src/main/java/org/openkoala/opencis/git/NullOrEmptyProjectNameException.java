package org.openkoala.opencis.git;

public class NullOrEmptyProjectNameException extends GitCISProjectException {

	private static final long serialVersionUID = -2269752764676792074L;

	public NullOrEmptyProjectNameException() {
		super();
	}
	
	public NullOrEmptyProjectNameException(String message) {
		super(message);
	}
	
	public NullOrEmptyProjectNameException(Throwable cause) {
		super(cause);
	}
	
	public NullOrEmptyProjectNameException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
