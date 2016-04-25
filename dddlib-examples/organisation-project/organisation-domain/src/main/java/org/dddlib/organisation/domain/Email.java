package org.dddlib.organisation.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.ValueObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email implements ValueObject {
	
	private static final long serialVersionUID = -734927401730610904L;
	
	@Column(name = "email")
	private String address;

	protected Email() {
	}

	public Email(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(31, 17).append(address).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Email)) {
			return false;
		}
		Email that = (Email) other;
		return new EqualsBuilder().append(this.address, that.address).isEquals();
	}

	@Override
	public String toString() {
		return address;
	}
}
