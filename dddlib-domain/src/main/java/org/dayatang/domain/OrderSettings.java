package org.dayatang.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.utils.Assert;

import java.util.ArrayList;
import java.util.List;

public class OrderSettings {
	
	private List<KeyValue<String, Boolean>> orderBy = new ArrayList<KeyValue<String,Boolean>>();

	public List<KeyValue<String, Boolean>> getOrderBy() {
		return orderBy;
	}

	public void asc(String propName) {
		Assert.notBlank(propName, "Property name must set!");
		orderBy.add(new KeyValue<String, Boolean>(propName, true));
	}

	public void desc(String propName) {
		Assert.notBlank(propName, "Property name must set!");
		orderBy.add(new KeyValue<String, Boolean>(propName, false));
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof OrderSettings)) {
			return false;
		}
		OrderSettings that = (OrderSettings) other;
		return new EqualsBuilder().append(this.orderBy, that.orderBy).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(orderBy).toHashCode();
	}

}
