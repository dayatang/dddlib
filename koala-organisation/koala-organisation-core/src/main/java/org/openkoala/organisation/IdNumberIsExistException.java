package org.openkoala.organisation;


public class IdNumberIsExistException extends OrganisationException {

	private static final long serialVersionUID = 1666932076361262359L;

	public IdNumberIsExistException() {
	}

	public IdNumberIsExistException(String message) {
		super(message);
	}

	public IdNumberIsExistException(Throwable cause) {
		super(cause);
	}

	public IdNumberIsExistException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
