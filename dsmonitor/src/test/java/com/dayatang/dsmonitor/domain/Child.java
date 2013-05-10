package com.dayatang.dsmonitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.dayatang.domain.AbstractEntity;

@Entity(name = "CommonsTestChild")
@Table(name = "CHILD")
public class Child extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3899776141651715689L;

	@Column(name = "NAME")
	private String name;

	@Override
	public boolean equals(Object other) {
		if (other instanceof Child == false) {
			return false;
		}
		if (this == other) {
			return true;
		}
		Child rhs = (Child) other;
		return new EqualsBuilder().append(name, rhs.name).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(name).toHashCode();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
