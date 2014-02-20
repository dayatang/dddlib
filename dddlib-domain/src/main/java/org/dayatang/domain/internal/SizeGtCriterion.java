package org.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.MapParameters;
import org.dayatang.utils.Assert;

/**
 * 判断某个集合属性的记录数大于指定值的查询条件
 * @author yyang
 */
public class SizeGtCriterion extends BasicCriterion {

    private final int value;

    /**
     * 创建查询条件
     * @param propName 属性名
     * @param value 属性值
     */
    public SizeGtCriterion(String propName, int value) {
        super(propName);
        this.value = value;
    }

    /**
     * 返回属性值
     * @return 属性值
     */
    public int getValue() {
        return value;
    }

    @Override
    public String toQueryString() {
        return "size(" + getPropNameWithAlias() + ") > " + getParamNameWithColon();
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
        if (!(other instanceof SizeGtCriterion)) {
            return false;
        }
        SizeGtCriterion that = (SizeGtCriterion) other;
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
        return "size of " + getPropName() + " > " + value;
    }

}
