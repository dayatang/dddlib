package org.openkoala.organisation.application.dto;

import org.openkoala.organisation.domain.Organization;

public class TransformOrganizationDTO {
	
	private Long organizationId;
	
	private String organizationSn;
	
	private String organizationName;
	
	private String organizationDescription;
	
	private boolean principal;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationSn() {
		return organizationSn;
	}

	public void setOrganizationSn(String organizationSn) {
		this.organizationSn = organizationSn;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationDescription() {
		return organizationDescription;
	}

	public void setOrganizationDescription(String organizationDescription) {
		this.organizationDescription = organizationDescription;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
	
	public static TransformOrganizationDTO generateByOrganization(Organization organization, boolean principal) {
		TransformOrganizationDTO result = new TransformOrganizationDTO();
		result.setPrincipal(principal);
		result.setOrganizationId(organization.getId());
		result.setOrganizationName(organization.getName());
		result.setOrganizationSn(organization.getSn());
		result.setOrganizationDescription(organization.getDescription());
		
		return result;
	}
	
}
