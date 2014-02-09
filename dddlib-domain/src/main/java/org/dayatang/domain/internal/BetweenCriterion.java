package org.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.QueryCriterion;
import org.dayatang.utils.Assert;

public class BetweenCriterion implements QueryCriterion {

    private final String propName;

    private final Comparable<?> from;

    private Comparable<?> to;

    public BetweenCriterion(String propName, Comparable<?> from, Comparable<?> to) {
        Assert.notBlank(propName, "Property name is null or blank!");
        Assert.notNull(from, "From value is null!");
        Assert.notNull(to, "To value is null!");
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
        BetweenCriterion that = (BetweenCriterion) other;
        return new EqualsBuilder()
                .append(this.getPropName(), that.getPropName())
                .append(from, that.from)
                .append(this.to, that.to)
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
