package org.dayatang.domain.internal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.dayatang.domain.Entity;
import org.dayatang.utils.Assert;

public class NotInCriterion extends AbstractCriterion {

    private final String propName;

    @SuppressWarnings("unchecked")
    private Collection<? extends Object> value = Collections.EMPTY_SET;

    public NotInCriterion(String propName, Collection<? extends Object> value) {
        Assert.notBlank(propName, "Property name is null or blank!");
        this.propName = propName;
        if (value != null) {
            this.value = value;
        }
    }

    public NotInCriterion(String propName, Object[] value) {
        Assert.notBlank(propName, "Property name is null or blank!");
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
	public String toQueryString() {
		if (value == null || value.isEmpty()) {
			return "";
		}
		return ROOT_ALIAS + "." + getPropName() + " not in (" + createInString(value) + ")";
	}

	private String createInString(Collection<? extends Object> value) {
		Set<Object> elements = new HashSet<Object>();
		for (Object item : value) {
			Object element;
			if (item instanceof Entity) {
				element = ((Entity)item).getId();
			} else {
				element = item;
			}
			if (element instanceof String || element instanceof Date) {
				element = "'" + element + "'";
			}
			elements.add(element);
		}
		return StringUtils.join(elements, ",");
	}

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NotInCriterion)) {
            return false;
        }
        NotInCriterion that = (NotInCriterion) other;
        return new EqualsBuilder()
                .append(this.getPropName(), that.getPropName())
                .append(value, that.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getPropName()).append(value).toHashCode();
    }

    @Override
    public String toString() {
        return getPropName() + " not in collection [" + collectionToString(value) + "]";
    }

    private String collectionToString(Collection<? extends Object> value) {
        return StringUtils.join(value, ",");
    }

}
