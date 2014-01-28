package org.openkoala.organisation.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.organisation.NameExistException;
import org.openkoala.organisation.OrganisationException;
import org.openkoala.organisation.TerminateNotEmptyOrganizationException;
import org.openkoala.organisation.TerminateRootOrganizationException;

/**
 * 机构
 * @author xmfang
 *
 */
@Entity
public abstract class Organization extends Party {

	private static final long serialVersionUID = -8953682430610195006L;
	
	/*
	 * 描述
	 */
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Organization() {
	}

	public Organization(String name) {
		super(name);
	}

	public Organization(String name, String sn) {
		super(name, sn);
	}

	/**
	 * 获取机构的全名
	 * @return
	 */
	public String getFullName() {
		String separator = "/";
		Organization parent = getParent(new Date());
		if (parent == null) {
			return getName();
		}
		
		return parent.getFullName().concat(separator).concat(getName());
	}
	
	/**
	 * 获取机构的父机构
	 * @param date
	 * @return
	 */
	public Organization getParent(Date date) {
		return OrganizationLineManagement.getParentOfOrganization(this, date);
	}

	/**
	 * 获取机构的子机构
	 * @param date
	 * @return
	 */
	public List<Organization> getChildren(Date date) {
		return OrganizationLineManagement.findChildrenOfOrganization(this, date);
	}

	/**
	 * 获得机构下的所有下属机构
	 * @param date
	 * @return
	 */
	public Set<Organization> getAllChildren(Date date) {
		Set<Organization> results = new HashSet<Organization>();
		for (Organization child : getChildren(date)) {
			results.add(child);
			results.addAll(child.getAllChildren(date));
		}
		return results;
	}
	
	/**
	 * 获得顶级机构
	 * @return
	 */
	public static Organization getTopOrganization() {
		return OrganizationLineManagement.getTopOrganization(new Date());
	}
	
	/**
	 * 创建顶级机构
	 */
	public void createAsTopOrganization() {
		if (getTopOrganization() != null) {
			throw new OrganisationException("Has top organization yet!");
		}
		save();
		new OrganizationLineManagement(null, this, new Date()).save();
	}
	
	/**
	 * 在某个机构下创建子机构
	 * @param parent
	 */
	public void createUnder(Organization parent) {
		if (parent == null) {
			throw new OrganisationException("Parent organization is not null!");
		}
		
		Date now = new Date();
		for (Organization each : parent.getChildren(now)) {
			if (getName().equals(each.getName())) {
				throw new NameExistException();
			}
		}
		
		save();
		new OrganizationLineManagement(parent, this, now).save();
	}

	/**
	 * 获得该机构相关的岗位
	 * @param date
	 * @return
	 */
	public Set<Post> getPosts(Date date) {
		return new HashSet<Post>(Post.findByOrganization(this, date));
	}
	
	/**
	 * 撤销机构
	 */
	@Override
	public void terminate(Date date) {
		if (getTopOrganization().equals(this)) {
			throw new TerminateRootOrganizationException();
		}
		if (hasEmployees(date)) {
			throw new TerminateNotEmptyOrganizationException();
		}
		for (Post post : getPosts(date)) {
			post.terminate(date);
		}
		for (Organization child : getChildren(date)) {
			child.terminate(date);
		}
		super.terminate(date);
	}

	private boolean hasEmployees(Date date) {
		for (Post post : getPosts(date)) {
			if (!(post.getEmployees(date).isEmpty())) {
				return true;
			}
		}
		for (Organization child : getChildren(date)) {
			if (child.hasEmployees(date)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获得机构负责人
	 * @param queryDate
	 * @return
	 */
	public List<Employee> getPrincipal(Date date) {
		return EmployeePostHolding.getManagerOfOrganization(this, date);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Organization)) {
			return false;
		}
		Organization that = (Organization) other;
		return new EqualsBuilder().append(this.getName(), that.getName())
				.append(this.getSn(), that.getSn())
				.append(this.getCreateDate(), that.getCreateDate())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getName())
				.append(getSn())
				.append(getCreateDate())
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getName()).build();
	}

}
