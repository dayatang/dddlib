package org.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.MapParameters;
import org.dayatang.utils.Assert;

public class StartsWithTextCriterion extends BasicCriterion {

    private final String value;

    public StartsWithTextCriterion(String propName, String value) {
        super(propName);
        Assert.notBlank(value, "Value is null or blank!");
        this.value = value;
    }

    /**
     * 获取匹配值
     * @return 匹配值
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toQueryString() {
        return getPropNameWithAlias() + " like " + getParamNameWithColon();
    }

    @Override
    public MapParameters getParameters() {
        return MapParameters.create().add(getParamName(), value + "%");
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StartsWithTextCriterion)) {
            return false;
        }
        StartsWithTextCriterion that = (StartsWithTextCriterion) other;
        return new EqualsBuilder()
                .append(this.getPropName(), that.getPropName())
                .append(value, that.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getPropName())
                .append(value)
                .toHashCode();
    }

    @Override
    public String toString() {
        return getPropName() + " like '" + value + "*'";
    }
}
