package org.openkoala.organisation;


public class HasPrincipalPostYetException extends OrganisationException {

	private static final long serialVersionUID = -845487376212725504L;

	public HasPrincipalPostYetException() {
	}

	public HasPrincipalPostYetException(String message) {
		super(message);
	}

	public HasPrincipalPostYetException(Throwable cause) {
		super(cause);
	}

	public HasPrincipalPostYetException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
