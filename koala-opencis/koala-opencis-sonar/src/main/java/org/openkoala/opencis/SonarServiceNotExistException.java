package org.openkoala.opencis;

public class SonarServiceNotExistException extends RuntimeException {

	private static final long serialVersionUID = 9039670277674578781L;

	public SonarServiceNotExistException() {
		super();
	}

	public SonarServiceNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public SonarServiceNotExistException(String message) {
		super(message);
	}

	public SonarServiceNotExistException(Throwable cause) {
		super(cause);
	}

}
