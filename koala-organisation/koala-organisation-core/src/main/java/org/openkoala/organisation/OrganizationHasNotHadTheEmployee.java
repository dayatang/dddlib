package org.openkoala.organisation;


public class OrganizationHasNotHadTheEmployee extends OrganisationException {

	private static final long serialVersionUID = 2595825061226137390L;

	public OrganizationHasNotHadTheEmployee() {
	}

	public OrganizationHasNotHadTheEmployee(String message) {
		super(message);
	}

	public OrganizationHasNotHadTheEmployee(Throwable cause) {
		super(cause);
	}

	public OrganizationHasNotHadTheEmployee(String message,
			Throwable cause) {
		super(message, cause);
	}

}
