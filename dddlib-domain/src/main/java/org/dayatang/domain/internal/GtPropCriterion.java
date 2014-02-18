package org.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.utils.Assert;

public class GtPropCriterion extends AbstractCriterion {

    private final String propName1;
    private final String propName2;

    public GtPropCriterion(String propName1, String propName2) {
        Assert.notBlank(propName1, "Property 1 is null or blank!");
        Assert.notBlank(propName2, "Property 2 is null or blank!");
        this.propName1 = propName1;
        this.propName2 = propName2;
    }

    public String getPropName1() {
        return propName1;
    }

    public String getPropName2() {
        return propName2;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GtPropCriterion)) {
            return false;
        }
        GtPropCriterion that = (GtPropCriterion) other;
        return new EqualsBuilder()
                .append(this.getPropName1(), that.getPropName1())
                .append(propName2, that.propName2).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getPropName1()).append(propName2).toHashCode();
    }

    @Override
    public String toString() {
        return getPropName1() + " > " + propName2;
    }

}
