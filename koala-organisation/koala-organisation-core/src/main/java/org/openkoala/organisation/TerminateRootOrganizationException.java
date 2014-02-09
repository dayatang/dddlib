package org.openkoala.organisation;


public class TerminateRootOrganizationException extends OrganisationException {

	private static final long serialVersionUID = -608309855377256441L;

	public TerminateRootOrganizationException() {
	}

	public TerminateRootOrganizationException(String message) {
		super(message);
	}

	public TerminateRootOrganizationException(Throwable cause) {
		super(cause);
	}

	public TerminateRootOrganizationException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
