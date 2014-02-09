package org.openkoala.organisation;


public class PostExistException extends OrganisationException {

	private static final long serialVersionUID = -8240795106100407123L;

	public PostExistException() {
	}

	public PostExistException(String message) {
		super(message);
	}

	public PostExistException(Throwable cause) {
		super(cause);
	}

	public PostExistException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
