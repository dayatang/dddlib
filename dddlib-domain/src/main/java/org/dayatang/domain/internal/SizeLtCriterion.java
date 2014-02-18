package org.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.utils.Assert;

public class SizeLtCriterion extends AbstractCriterion {

    private final String propName;
    private final int value;

    public SizeLtCriterion(String propName, int value) {
        Assert.notBlank(propName, "Property name is null or blank!");
        this.propName = propName;
        this.value = value;
    }

    public String getPropName() {
        return propName;
    }

    public int getValue() {
        return value;
    }

	@Override
	public String toQueryString() {
		return "size(" + ROOT_ALIAS + getPropName() + ") < ?";
	}

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SizeLtCriterion)) {
            return false;
        }
        SizeLtCriterion that = (SizeLtCriterion) other;
        return new EqualsBuilder().append(this.getPropName(), that.getPropName()).append(value, that.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getPropName()).append(value).toHashCode();
    }

    @Override
    public String toString() {
        return "size of " + getPropName() + " < " + value;
    }

}
