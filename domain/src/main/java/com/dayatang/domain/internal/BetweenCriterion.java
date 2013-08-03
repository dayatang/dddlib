package com.dayatang.domain.internal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;
import com.dayatang.domain.QueryException;


public class BetweenCriterion implements QueryCriterion {
	
	private String propName;

	private Comparable<?> from;
	
	private Comparable<?> to;

	public BetweenCriterion(String propName, Comparable<?> from, Comparable<?> to) {
		if (StringUtils.isEmpty(propName)) {
			throw new QueryException("Property name is null!");
		}
		if (from == null) {
			throw new QueryException("From value is null!");
		}
		if (to == null) {
			throw new QueryException("To value is null!");
		}
		this.propName = propName;
		this.from = from;
		this.to = to;
	}

	public String getPropName() {
		return propName;
	}

	public Comparable<?> getFrom() {
		return from;
	}

	public Comparable<?> getTo() {
		return to;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BetweenCriterion)) {
			return false;
		}
		BetweenCriterion castOther = (BetweenCriterion) other;
		return new EqualsBuilder()
			.append(this.getPropName(), castOther.getPropName())
			.append(from, castOther.from)
			.append(this.to, castOther.to)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getPropName())
		.append(from).append(to).toHashCode();
	}

	@Override
	public String toString() {
		return getPropName() + " between " + from + " and " + to;
	}

	
}
