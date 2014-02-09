package org.openkoala.organisation;


public class OrganizationHasEmployee extends OrganisationException {

	private static final long serialVersionUID = 2813449496205272197L;

	public OrganizationHasEmployee() {
	}

	public OrganizationHasEmployee(String message) {
		super(message);
	}

	public OrganizationHasEmployee(Throwable cause) {
		super(cause);
	}

	public OrganizationHasEmployee(String message,
			Throwable cause) {
		super(message, cause);
	}

}
