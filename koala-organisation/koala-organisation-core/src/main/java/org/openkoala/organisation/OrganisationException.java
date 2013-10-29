package org.openkoala.organisation;

public class OrganisationException extends RuntimeException {

	private static final long serialVersionUID = 62105045849226323L;

	public OrganisationException() {
	}

	public OrganisationException(String message) {
		super(message);
	}

	public OrganisationException(Throwable cause) {
		super(cause);
	}

	public OrganisationException(String message, Throwable cause) {
		super(message, cause);
	}

}
