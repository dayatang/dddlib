package org.openkoala.organisation;


public class SnIsExistException extends OrganisationException {

	private static final long serialVersionUID = 4958512667168828130L;

	public SnIsExistException() {
	}

	public SnIsExistException(String message) {
		super(message);
	}

	public SnIsExistException(Throwable cause) {
		super(cause);
	}

	public SnIsExistException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
