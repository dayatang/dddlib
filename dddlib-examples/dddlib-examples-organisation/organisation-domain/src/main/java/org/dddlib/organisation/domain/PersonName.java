package org.dddlib.organisation.domain;

import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import org.dayatang.domain.ValueObject;

@Embeddable
public class PersonName implements ValueObject {

	private static final long serialVersionUID = -5782631381467586227L;
	private String firstName;
	private String lastName;

	public PersonName() {
	}

	public PersonName(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(31, 17).append(firstName).append(lastName)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PersonName)) {
			return false;
		}
		PersonName that = (PersonName) other;
		return new EqualsBuilder().append(this.firstName, that.firstName)
				.append(this.lastName, that.lastName).isEquals();
	}

	@Override
	public String toString() {
		return lastName + firstName;
	}
}
