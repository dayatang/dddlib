package org.openkoala.opencis;

public class JenkinsHomeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7273327892714359892L;

	public JenkinsHomeNotFoundException() {
		super("jenkins home not found!");
	}

	public JenkinsHomeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public JenkinsHomeNotFoundException(String message) {
		super(message);
	}

	public JenkinsHomeNotFoundException(Throwable cause) {
		super(cause);
	}

}
