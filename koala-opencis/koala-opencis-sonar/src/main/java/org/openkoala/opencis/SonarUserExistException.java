package org.openkoala.opencis;

public class SonarUserExistException extends RuntimeException {

	private static final long serialVersionUID = -355495149031268943L;

	public SonarUserExistException() {
		super();
	}

	public SonarUserExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public SonarUserExistException(String message) {
		super(message);
	}

	public SonarUserExistException(Throwable cause) {
		super(cause);
	}
	
}
