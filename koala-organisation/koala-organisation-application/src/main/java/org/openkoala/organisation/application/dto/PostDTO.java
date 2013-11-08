package org.openkoala.organisation.application.dto;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.organisation.domain.Job;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.Post;

public class PostDTO {

	private Long id;
	
	private String name;
	
	private String sn;
	
	private String organizationName;
	
	private String jobName;
	
	private String description;
	
	private boolean organizationPrincipal;
	
	private int version;
	
	private Date createDate;
	
	private Date terminateDate;
	
	private Long employeeCount;
	
	private Long organizationId;
	
	private Long jobId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOrganizationPrincipal() {
		return organizationPrincipal;
	}

	public void setOrganizationPrincipal(boolean organizationPrincipal) {
		this.organizationPrincipal = organizationPrincipal;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getTerminateDate() {
		return terminateDate;
	}

	public void setTerminateDate(Date terminateDate) {
		this.terminateDate = terminateDate;
	}

	public Long getEmployeeCount() {
		return employeeCount;
	}

	public void setEmployeeCount(Long employeeCount) {
		this.employeeCount = employeeCount;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public Post transFormToPost() {
		Post result = new Post(name);
		result.setId(id);
		result.setCreateDate(createDate);
		result.setSn(sn);
		result.setDescription(description);
		result.setOrganizationPrincipal(organizationPrincipal);
		result.setTerminateDate(terminateDate);
		result.setVersion(version);
		
		Job job = Job.get(Job.class, jobId);
		result.setJob(job);
		
		Organization organization = Organization.get(Organization.class, organizationId);
		result.setOrganization(organization);
		
		return result;
	}
	
	public static PostDTO generateDtoBy(Post post) {
		if (post == null) {
			return null;
		}
		
		PostDTO result = new PostDTO();
		result.setId(post.getId());
		result.setName(post.getName());
		result.setSn(post.getSn());
		result.setDescription(post.getDescription());
		result.setOrganizationPrincipal(post.isOrganizationPrincipal());
		result.setVersion(post.getVersion());
		result.setCreateDate(post.getCreateDate());
		result.setTerminateDate(post.getTerminateDate());
		result.setEmployeeCount(post.getEmployeeCount(new Date()));
		
		Job job = post.getJob();
		result.setJobId(job.getId());
		result.setJobName(job.getName());
		
		Organization organization = post.getOrganization();
		result.setOrganizationId(organization.getId());
		result.setOrganizationName(organization.getName());
		
		return result;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof PostDTO)) {
			return false;
		}
		PostDTO that = (PostDTO) other;
		return new EqualsBuilder().append(this.getSn(), that.getSn())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getSn()).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getName()).build();
	}
	
}
