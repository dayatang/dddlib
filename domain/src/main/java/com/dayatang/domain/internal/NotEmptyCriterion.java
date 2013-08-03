package com.dayatang.domain.internal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;
import com.dayatang.domain.QueryException;


public class NotEmptyCriterion implements QueryCriterion {

	private String propName;

	public NotEmptyCriterion(String propName) {
		if (StringUtils.isEmpty(propName)) {
			throw new QueryException("Property name is null!");
		}
		this.propName = propName;
	}

	public String getPropName() {
		return propName;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof NotEmptyCriterion)) {
			return false;
		}
		NotEmptyCriterion castOther = (NotEmptyCriterion) other;
		return new EqualsBuilder()
			.append(this.getPropName(), castOther.getPropName())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getPropName()).toHashCode();
	}

	@Override
	public String toString() {
		return getPropName() + " is not empty";
	}
	
}
