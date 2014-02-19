package org.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.utils.Assert;

public class NotEmptyCriterion extends AbstractCriterion {

    private final String propName;

    public NotEmptyCriterion(String propName) {
        Assert.notBlank(propName, "Property name is null or blank!");
        this.propName = propName;
    }

    public String getPropName() {
        return propName;
    }

    @Override
	public String toQueryString() {
		return ROOT_ALIAS + "." + getPropName() + " is not empty";
	}

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NotEmptyCriterion)) {
            return false;
        }
        NotEmptyCriterion that = (NotEmptyCriterion) other;
        return new EqualsBuilder()
                .append(this.getPropName(), that.getPropName())
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
