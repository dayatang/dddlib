package org.openkoala.organisation.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 部门
 * @author xmfang
 *
 */
@Entity
@DiscriminatorValue("Department")
public class Department extends Organization {

	private static final long serialVersionUID = -7339118476080239701L;

	public Department() {
	}

	public Department(String name) {
		super(name);
	}

	public Department(String name, String sn) {
		super(name, sn);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Department)) {
			return false;
		}
		Department that = (Department) other;
		return new EqualsBuilder().append(this.getName(), that.getName())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getName()).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getName()).build();
	}

}
