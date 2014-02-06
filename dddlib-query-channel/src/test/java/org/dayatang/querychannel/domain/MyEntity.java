package org.dayatang.querychannel.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.dayatang.domain.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "pay_test_myentity")
@NamedQueries( { @NamedQuery(name = "MyEntity.findByName", query = "select o from MyEntity o where o.name like ?") })
public class MyEntity extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2791539145767570307L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MyEntity == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		MyEntity rhs = (MyEntity) obj;
		return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
