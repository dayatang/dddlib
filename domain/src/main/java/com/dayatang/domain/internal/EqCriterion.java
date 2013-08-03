package com.dayatang.domain.internal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;
import com.dayatang.domain.QueryException;


public class EqCriterion implements QueryCriterion {
	
	private String propName;

	private Object value;

	public EqCriterion(String propName, Object value) {
		if (StringUtils.isEmpty(propName)) {
			throw new QueryException("Property name is null!");
		}
		this.propName = propName;
		this.value = value;
	}

	public String getPropName() {
		return propName;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EqCriterion)) {
			return false;
		}
		EqCriterion castOther = (EqCriterion) other;
		return new EqualsBuilder()
			.append(this.getPropName(), castOther.getPropName())
			.append(value, castOther.value).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getPropName()).append(value).toHashCode();
	}

	@Override
	public String toString() {
		return getPropName() + " = " + value;
	}
	
}
