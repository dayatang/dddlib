package org.openkoala.organisation.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.organisation.HasPrincipalPostYetException;

/**
 * 员工
 * @author xmfang
 *
 */
@Entity
@DiscriminatorValue("Employee")
public class Employee extends Party {

	private static final long serialVersionUID = -7339118476080239701L;

	/*
	 * 作为员工的人
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "person_id")
	private Person person;

	/*
	 * 入职日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "entry_date")
	private Date entryDate;
	
	Employee() {
	}

	public Employee(Person person, Date entryDate) {
		super(person.getName());
		this.person = person;
		this.entryDate = entryDate;
	}

	public Employee(Person person, String sn, Date entryDate) {
		super(person.getName());
		this.person = person;
		this.entryDate = entryDate;
		setSn(sn);
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	@Override
	public void setName(String name) {
		super.setName(name);
		if (person != null) {
			person.setName(name);
		}
	}
	
	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

//	/**
//	 * 获得员工的所有任职职务
//	 * @param date
//	 * @return
//	 */
//	public Set<Job> getJobs(Date date) {
//		return new HashSet<Job>(EmployeePostHolding.findJobsOfEmployee(this, date));
//	}
	
	/**
	 * 获得员工的所有任职岗位
	 * @param date
	 * @return
	 */
	public Set<Post> getPosts(Date date) {
		return new HashSet<Post>(EmployeePostHolding.findPostsOfEmployee(this, date));
	}

	/**
	 * 获得员工的主任职岗位
	 * @param date
	 * @return
	 */
	public Post getPrincipalPost(Date date) {
		return EmployeePostHolding.getPrincipalPostByEmployee(this, date);
	}
	
	/**
	 * 获得员工的兼任职务
	 * @param queryDate
	 * @return
	 */
	public List<Post> getAdditionalPosts(Date date) {
		return EmployeePostHolding.findAdditionalPostsByEmployee(this, date);
	}

	/**
	 * 获得员工的所属机构
	 * @param date
	 * @return
	 */
	public Organization getOrganization(Date date) {
		return EmployeePostHolding.getOrganizationOfEmployee(this, date);
	}
	
	/**
	 * 分配岗位
	 * @param post
	 * @param principal 是否主要任职职务
	 */
	public void assignPost(Post post, boolean principal) {
		Date now = new Date();
		if (principal && EmployeePostHolding.getPrincipalPostByEmployee(this, now) != null) {
			throw new HasPrincipalPostYetException("The employee has principal post yet!");
		}
		
		new EmployeePostHolding(post, this, principal, now).save();
	}

	/**
	 * 同时分配多个职务
	 * @param posts
	 */
	public void assignPosts(Set<Post> posts) {
		for (Post post : posts) {
			assignPost(post, false);
		}
	}
	
	/**
	 * 卸任岗位
	 * @param posts
	 */
	public void outgoingPosts(Set<Post> posts) {
		Date now = new Date();
		
//		Set<Post> currentPosts = new HashSet<Post>(EmployeePostHolding.findPostsOfEmployee(this, now));
//		if (currentPosts.equals(posts)) {
//			throw new EmployeeMustHaveAtLeastOnePostException();
//		}
		
		for (Post post : posts) {
			EmployeePostHolding holding = EmployeePostHolding.getByPostAndEmployee(post, this, now);
			if (holding != null) {
				holding.terminate(now);
			}
		}
	}
	
	/**
	 * 保存员工及其任职信息和所属机构关系
	 * @param post
	 * @param organization
	 */
	public void saveWithPost(Post post) {
		save();
		
		if (post != null) {
			assignPost(post, true);
		}
	}
	
	@Override
	public void save() {
		if (getId() == null) {
			person.checkIdNumberExist();
		} else {
			Person thePerson = get(Person.class, person.getId());
			if (thePerson.getIdNumber() != null && !thePerson.getIdNumber().equals(person.getIdNumber())) {
				person.checkIdNumberExist();
			}
		}
		
		super.save();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Employee)) {
			return false;
		}
		Employee that = (Employee) other;
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
