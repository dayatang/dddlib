package org.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.utils.Assert;

public class GePropCriterion extends AbstractCriterion {

    private final String propName;
    private final String otherPropName;

    public GePropCriterion(String propName1, String propName2) {
        Assert.notBlank(propName1, "Property 1 is null or blank!");
        Assert.notBlank(propName2, "Property 2 is null or blank!");
        this.propName = propName1;
        this.otherPropName = propName2;
    }

    public String getPropName() {
        return propName;
    }

    public String getOtherPropName() {
        return otherPropName;
    }

	@Override
	public String toQueryString() {
		return ROOT_ALIAS + getPropName() + " >= " + ROOT_ALIAS + getOtherPropName();
	}

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GePropCriterion)) {
            return false;
        }
        GePropCriterion that = (GePropCriterion) other;
        return new EqualsBuilder()
                .append(this.getPropName(), that.getPropName())
                .append(otherPropName, that.otherPropName).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getPropName()).append(otherPropName).toHashCode();
    }

    @Override
    public String toString() {
        return getPropName() + " >= " + otherPropName;
    }

}
