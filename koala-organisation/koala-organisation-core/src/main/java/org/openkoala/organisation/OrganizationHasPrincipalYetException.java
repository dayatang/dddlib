package org.openkoala.organisation;


public class OrganizationHasPrincipalYetException extends OrganisationException {

	private static final long serialVersionUID = 8825492614354652485L;

	public OrganizationHasPrincipalYetException() {
	}

	public OrganizationHasPrincipalYetException(String message) {
		super(message);
	}

	public OrganizationHasPrincipalYetException(Throwable cause) {
		super(cause);
	}

	public OrganizationHasPrincipalYetException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
