package org.openkoala.organisation.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.organisation.OrganizationHasPrincipalYetException;
import org.openkoala.organisation.TerminateHasEmployeePostException;

import com.dayatang.domain.QuerySettings;

@Entity
@DiscriminatorValue("Post")
@NamedQueries({
	@NamedQuery(name = "findByOrganization", query = "select o from Post o where o.organization = :organization and o.createDate <= :date and o.terminateDate > :date"),
	@NamedQuery(name = "findByJob", query = "select o from Post o where o.job = :job and o.createDate <= :date and o.terminateDate > :date")})
public class Post extends Party {

	private static final long serialVersionUID = -2205967098970951498L;

	@ManyToOne
	@JoinColumn(name = "org_id")
	private Organization organization;

	@ManyToOne
	@JoinColumn(name = "job_id")
	private Job job;

	@Column(name = "description")
	private String description;

	@Column(name = "org_principal")
	private boolean organizationPrincipal;
	
	public Post() {
		super();
	}

	public Post(String name) {
		super(name);
	}

	public Post(String name, String sn) {
		super(name, sn);
	}

	public Post(String name, String sn, Job job, Organization organization) {
		super(name, sn);
		this.job = job;
		this.organization = organization;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
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

	public Long getEmployeeCount(Date date) {
		return EmployeePostHolding.getEmployeeCountOfPost(this, date);
	}
	
	/**
	 * 查找某个职务的相关岗位
	 * @param job
	 * @param date
	 * @return
	 */
	public static List<Post> findByJob(Job job, Date date) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("job", job);
		params.put("date", date);
		return getRepository().findByNamedQuery("findByJob", params, Post.class);
	}
	
	/**
	 * 查找某个机构的相关岗位
	 * @param organization
	 * @param date
	 * @return
	 */
	public static List<Post> findByOrganization(Organization organization, Date date) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("organization", organization);
		params.put("date", date);
		return getRepository().findByNamedQuery("findByOrganization", params, Post.class);
	}

	/**
	 * 获得岗位下的员工
	 * @param date
	 * @return
	 */
	public Set<Employee> getEmployees(Date date) {
		return new HashSet<Employee>(EmployeePostHolding.findEmployeesOfPost(this, date));
	}

	/**
	 * 查询某个机构是否已经有主负责岗位
	 * @param organization
	 * @param date
	 * @return
	 */
	public static boolean hasPrincipalPostOfOrganization(Organization organization, Date date) {
		List<Post> posts = getRepository().find(QuerySettings.create(Post.class)
				.eq("organization", organization)
				.eq("organizationPrincipal", true)
				.le("createDate", date)
				.gt("terminateDate", date));
		return posts.isEmpty() ? false : true;
	}
	
	@Override
	public void save() {
		if (organizationPrincipal && hasPrincipalPostOfOrganization(organization, new Date())) {
			if (getId() != null) {
				if (!isOrganizationPrincipalBefore()) {
					throw new OrganizationHasPrincipalYetException();
				}
			} else {
				throw new OrganizationHasPrincipalYetException();
			}
		}
		super.save();
	}
	
	private boolean isOrganizationPrincipalBefore() {
		return getRepository().get(Post.class, getId()).isOrganizationPrincipal();
	}
	
	@Override
	public void terminate(Date date) {
		if (hasEmployee(date)) {
			throw new TerminateHasEmployeePostException();
		}
		super.terminate(date);
	}
	
	private boolean hasEmployee(Date date) {
		List<EmployeePostHolding> holdings = EmployeePostHolding.getByPost(this, date);
		return holdings.isEmpty() ? false : true;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Post)) {
			return false;
		}
		Post that = (Post) other;
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
