package com.dayatang.domain.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;
import com.dayatang.domain.QueryException;


public class InCriterion implements QueryCriterion {
	
	private String propName;

	@SuppressWarnings("unchecked")
	private Collection<? extends Object> value = Collections.EMPTY_SET;

	public InCriterion(String propName, Collection<? extends Object> value) {
		if (StringUtils.isEmpty(propName)) {
			throw new QueryException("Property name is null!");
		}
		this.propName = propName;
		if (value != null) {
			this.value = value;
		}
	}
	
	public InCriterion(String propName, Object[] value) {
		if (StringUtils.isEmpty(propName)) {
			throw new QueryException("Property name is null!");
		}
		this.propName = propName;
		if (value != null && value.length > 0) {
			this.value = Arrays.asList(value);
		}
	}

	public String getPropName() {
		return propName;
	}

	public Collection<? extends Object> getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof InCriterion)) {
			return false;
		}
		InCriterion castOther = (InCriterion) other;
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
		return getPropName() + " in collection [" + collectionToString(value) + "]";
	}

	private String collectionToString(Collection<? extends Object> value) {
		return StringUtils.join(value, ",");
	}
	
}
