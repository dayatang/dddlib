package org.openkoala.organisation;


public class NameExistException extends OrganisationException {

	private static final long serialVersionUID = 3268108265128574596L;

	public NameExistException() {
	}

	public NameExistException(String message) {
		super(message);
	}

	public NameExistException(Throwable cause) {
		super(cause);
	}

	public NameExistException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
