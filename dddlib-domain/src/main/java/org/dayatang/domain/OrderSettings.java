package org.dayatang.domain;

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
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof OrderSettings)) {
			return false;
		}

		OrderSettings that = (OrderSettings) other;

		return this.getOrderBy().equals(that.getOrderBy());
	}

	@Override
	public int hashCode() {
		return getOrderBy().hashCode();
	}
}
