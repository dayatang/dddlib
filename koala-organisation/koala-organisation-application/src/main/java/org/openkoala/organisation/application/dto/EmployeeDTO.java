package org.openkoala.organisation.application.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Gender;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.Person;
import org.openkoala.organisation.domain.Post;

public class EmployeeDTO implements Serializable {

	private static final long serialVersionUID = -7358868162940087039L;

	private Long id;
	
	private Long personId;
	
	private int personVersion;
	
	private String name;
	
	private String sn;
	
	private Gender gender;
	
	private String idNumber;

	private String mobilePhone;

	private String familyPhone;
	
	private String email;

	private Date entryDate;

	private String organizationName;
	
	private String postName;
	
	private String additionalPostNames;
	
	private int version;
	
	private Date createDate;
	
	private Date terminateDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public int getPersonVersion() {
		return personVersion;
	}

	public void setPersonVersion(int personVersion) {
		this.personVersion = personVersion;
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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getFamilyPhone() {
		return familyPhone;
	}

	public void setFamilyPhone(String familyPhone) {
		this.familyPhone = familyPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getAdditionalPostNames() {
		return additionalPostNames;
	}

	public void setAdditionalPostNames(String additionalPostNames) {
		this.additionalPostNames = additionalPostNames;
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

	public Employee transFormToEmployee() {
		Person person = new Person(name);
		person.setId(personId);
		person.setVersion(personVersion);
		person.setEmail(email);
		person.setFamilyPhone(familyPhone);
		person.setGender(gender);
		person.setIdNumber(idNumber);
		person.setMobilePhone(mobilePhone);
		
		Employee result = new Employee(person, entryDate);
		result.setId(id);
		result.setCreateDate(createDate);
		result.setSn(sn);
		result.setTerminateDate(terminateDate);
		result.setVersion(version);
		
		return result;
	}
	
	public static EmployeeDTO generateDtoBy(Employee employee) {
		if (employee == null) {
			return null;
		}
		Date now = new Date();
		
		EmployeeDTO result = new EmployeeDTO();
		result.setId(employee.getId());
		result.setPersonId(employee.getPerson().getId());
		result.setPersonVersion(employee.getPerson().getVersion());
		result.setName(employee.getName());
		
		if (employee.getPerson().getGender() != null) {
			result.setGender(employee.getPerson().getGender());
		}
		
		Post post = employee.getPrincipalPost(now);
		if (post != null) {
			result.setPostName(post.getName());
		}
		
		List<Post> additionalPosts = employee.getAdditionalPosts(now);
		if (!additionalPosts.isEmpty()) {
			StringBuilder additionalPostNames = new StringBuilder();
			String separator = ", ";
			for (Post additionalPost : additionalPosts) {
				additionalPostNames.append(additionalPost.getName());
				additionalPostNames.append(separator);
			}
			result.setAdditionalPostNames(additionalPostNames.substring(0, additionalPostNames.length() - separator.length()));
		}
		
		Organization organization = employee.getOrganization(new Date());
		if (organization != null) {
			result.setOrganizationName(organization.getFullName());
		}
		
		result.setEmail(employee.getPerson().getEmail());
		result.setEntryDate(employee.getEntryDate());
		result.setFamilyPhone(employee.getPerson().getFamilyPhone());
		result.setIdNumber(employee.getPerson().getIdNumber());
		result.setMobilePhone(employee.getPerson().getMobilePhone());
		
		result.setSn(employee.getSn());
		result.setVersion(employee.getVersion());
		result.setCreateDate(employee.getCreateDate());
		result.setTerminateDate(employee.getTerminateDate());
		
		return result;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof EmployeeDTO)) {
			return false;
		}
		EmployeeDTO that = (EmployeeDTO) other;
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
