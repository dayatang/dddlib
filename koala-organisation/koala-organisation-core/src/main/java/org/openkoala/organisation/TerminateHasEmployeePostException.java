package org.openkoala.organisation;


public class TerminateHasEmployeePostException extends OrganisationException {

	private static final long serialVersionUID = 5585954194142804121L;

	public TerminateHasEmployeePostException() {
	}

	public TerminateHasEmployeePostException(String message) {
		super(message);
	}

	public TerminateHasEmployeePostException(Throwable cause) {
		super(cause);
	}

	public TerminateHasEmployeePostException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
