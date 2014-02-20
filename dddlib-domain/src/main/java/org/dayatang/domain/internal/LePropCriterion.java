package org.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.MapParameters;
import static org.dayatang.domain.internal.AbstractCriterion.ROOT_ALIAS;
import org.dayatang.utils.Assert;

/**
 * 代表一个属性小于或等于另一个属性的查询条件
 * @author yyang
 */
public class LePropCriterion extends BasicCriterion {

    private final String otherPropName;

    /**
     * 创建查询条件
     * @param propName 属性名
     * @param otherPropName 另一个属性名
     */
    public LePropCriterion(String propName, String otherPropName) {
        super(propName);
        Assert.notBlank(otherPropName, "Other property name is null or blank!");
        this.otherPropName = otherPropName;
    }

    /**
     * 获得另一个属性名
     * @return 另一个属性名
     */
    public String getOtherPropName() {
        return otherPropName;
    }

    @Override
    public String toQueryString() {
        return getPropNameWithAlias() + " <= " + ROOT_ALIAS + "." + otherPropName;
    }

    @Override
    public MapParameters getParameters() {
        return MapParameters.create();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LePropCriterion)) {
            return false;
        }
        LePropCriterion that = (LePropCriterion) other;
        return new EqualsBuilder()
                .append(this.getPropName(), that.getPropName())
                .append(otherPropName, that.otherPropName).isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getPropName())
                .append(otherPropName).toHashCode();
    }

    @Override
    public String toString() {
        return getPropName() + " <= " + otherPropName;
    }

}
