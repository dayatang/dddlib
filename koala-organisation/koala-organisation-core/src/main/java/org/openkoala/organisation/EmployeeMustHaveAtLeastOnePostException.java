package org.openkoala.organisation;


public class EmployeeMustHaveAtLeastOnePostException extends OrganisationException {

	private static final long serialVersionUID = 8844932594726433766L;


	public EmployeeMustHaveAtLeastOnePostException() {
	}

	public EmployeeMustHaveAtLeastOnePostException(String message) {
		super(message);
	}

	public EmployeeMustHaveAtLeastOnePostException(Throwable cause) {
		super(cause);
	}

	public EmployeeMustHaveAtLeastOnePostException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
