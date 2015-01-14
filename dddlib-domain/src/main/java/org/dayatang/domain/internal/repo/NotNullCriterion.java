package org.dayatang.domain.internal.repo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.NamedParameters;

/**
 * 判断某个属性值是否为不null的查询条件
 * @author yyang
 */
public class NotNullCriterion extends BasicCriterion {

    public NotNullCriterion(String propName) {
        super(propName);
    }

    @Override
    public String toQueryString() {
        return getPropNameWithAlias() + " is not null";
    }

    @Override
    public NamedParameters getParameters() {
        return NamedParameters.create();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NotNullCriterion)) {
            return false;
        }
        NotNullCriterion that = (NotNullCriterion) other;
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
        return getPropName() + " is not null";
    }

}
