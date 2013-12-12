package org.openkoala.organisation.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 公司
 * @author xmfang
 *
 */
@Entity
@DiscriminatorValue("Comapny")
public class Company extends Organization {

	private static final long serialVersionUID = -7339118476080239701L;

	public Company() {
	}

	public Company(String name) {
		super(name);
	}

	public Company(String name, String sn) {
		super(name, sn);
	}
	
	public Company(String name, String sn,String description) {
		super(name, sn);
		this.setDescription(description);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Company)) {
			return false;
		}
		Company that = (Company) other;
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
