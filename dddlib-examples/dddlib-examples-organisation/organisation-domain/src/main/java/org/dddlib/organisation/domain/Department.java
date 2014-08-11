package org.dddlib.organisation.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("Department")
public class Department extends Organization {

	private static final long serialVersionUID = -7339118476080239701L;

	public Department() {
	}

	public Department(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getName()).build();
	}

}
