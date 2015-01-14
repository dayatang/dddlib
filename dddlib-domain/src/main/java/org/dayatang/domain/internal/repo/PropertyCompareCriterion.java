package org.dayatang.domain.internal.repo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.NamedParameters;
import org.dayatang.utils.Assert;
import org.dayatang.utils.BeanUtils;

import java.util.Map;

/**
 * 代表属性值与另一个属性值比较的一类查询条件
 * Created by yyang on 14-2-23.
 */
public abstract class PropertyCompareCriterion extends BasicCriterion {

    private final String otherPropName;

    private String operator;

    public PropertyCompareCriterion(String propName, String otherPropName) {
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

    protected final void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toQueryString() {
        return getPropNameWithAlias() + operator + ROOT_ALIAS + "." + otherPropName;
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
        if (other == null) {
            return false;
        }
        if (!(getClass().isAssignableFrom(other.getClass()))) {
            return false;
        }
        Map<String, Object> thisPropValues = new BeanUtils(this).getPropValues();
        Map<String, Object> otherPropValues = new BeanUtils(other).getPropValues();
        return new EqualsBuilder()
                .append(thisPropValues.get("propName"), otherPropValues.get("propName"))
                .append(thisPropValues.get("otherPropName"), otherPropValues.get("otherPropName"))
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getPropName())
                .append(getOtherPropName())
                .toHashCode();
    }

    @Override
    public String toString() {
        return getPropName() + operator + getOtherPropName();
    }
}
