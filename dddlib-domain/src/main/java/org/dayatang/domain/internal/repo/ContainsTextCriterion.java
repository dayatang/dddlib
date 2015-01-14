package org.dayatang.domain.internal.repo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.NamedParameters;
import org.dayatang.utils.Assert;

/**
 * 判断某个属性的值是否包含指定文本内容的查询条件
 * @author yyang
 */
public class ContainsTextCriterion extends BasicCriterion {

    private final String value;

    /**
     * 创建查询条件
     * @param propName 属性名
     * @param value 要包含在属性值中的子字符串
     */
    public ContainsTextCriterion(String propName, String value) {
        super(propName);
        Assert.notBlank(propName, "Property name is null or blank!");
        Assert.notBlank(value, "value is null or blank!");
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
    public NamedParameters getParameters() {
        return NamedParameters.create().add(getParamName(), "%" + value + "%");
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ContainsTextCriterion)) {
            return false;
        }
        ContainsTextCriterion that = (ContainsTextCriterion) other;
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
        return getPropName() + " like '*" + value + "*'";
    }

}
