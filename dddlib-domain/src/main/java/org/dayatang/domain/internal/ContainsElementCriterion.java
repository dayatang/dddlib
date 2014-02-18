package org.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.utils.Assert;

public class ContainsElementCriterion extends AbstractCriterion {

    private final String propName;

    private final Object value;

    public ContainsElementCriterion(String propName, Object value) {
        Assert.notBlank(propName, "Property name is null or blank!");
        Assert.notNull(value, "Value is null!");
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
	public String toQueryString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ContainsElementCriterion)) {
            return false;
        }
        ContainsElementCriterion that = (ContainsElementCriterion) other;
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
        return getPropName() + " contains " + value;
    }

}
