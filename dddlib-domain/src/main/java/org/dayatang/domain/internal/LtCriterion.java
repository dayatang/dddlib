package org.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.MapParameters;
import org.dayatang.utils.Assert;

/**
 * 代表属性小于指定值的查询条件
 * @author yyang
 */
public class LtCriterion extends BasicCriterion {

    @SuppressWarnings("rawtypes")
	private final Comparable value;

    @SuppressWarnings("rawtypes")
	public LtCriterion(String propName, Comparable value) {
        super(propName);
        Assert.notNull(value, "Value is null!");
        this.value = value;
    }

    /**
     * 获取匹配值
     * @return 匹配值
     */
    @SuppressWarnings("rawtypes")
	public Comparable getValue() {
        return value;
    }

    @Override
    public String toQueryString() {
        return getPropNameWithAlias() + " < " + getParamNameWithColon();
    }

    @Override
    public MapParameters getParameters() {
        return MapParameters.create().add(getParamName(), value);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LtCriterion)) {
            return false;
        }
        LtCriterion that = (LtCriterion) other;
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
        return getPropName() + " < " + value;
    }

}
