package org.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.MapParameters;
import org.dayatang.utils.Assert;

/**
 * 代表属性等于指定值的查询条件
 * @author yyang
 */
public class EqCriterion extends BasicCriterion {

    private final Object value;

    public EqCriterion(String propName, Object value) {
        super(propName);
        Assert.notNull(value, "Value is null!");
        this.value = value;
    }

    @Override
    public String toQueryString() {
        return getPropNameWithAlias() + " = " + getParamNameWithColon();
    }

    @Override
    public MapParameters getParameters() {
        return MapParameters.create().add(getParamName(), value);
    }

    /**
     * 获取匹配值
     * @return 匹配值
     */
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EqCriterion)) {
            return false;
        }
        EqCriterion that = (EqCriterion) other;
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
        return getPropName() + " = " + value;
    }

}
