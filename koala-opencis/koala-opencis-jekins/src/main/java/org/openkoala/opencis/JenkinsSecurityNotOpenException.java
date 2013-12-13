package org.openkoala.opencis;

public class JenkinsSecurityNotOpenException extends RuntimeException {

	private static final long serialVersionUID = -4494475234320091464L;

	public JenkinsSecurityNotOpenException() {
		super();
	}

	public JenkinsSecurityNotOpenException(String message, Throwable cause) {
		super(message, cause);
	}

	public JenkinsSecurityNotOpenException(String message) {
		super(message);
	}

	public JenkinsSecurityNotOpenException(Throwable cause) {
		super(cause);
	}
	
}
