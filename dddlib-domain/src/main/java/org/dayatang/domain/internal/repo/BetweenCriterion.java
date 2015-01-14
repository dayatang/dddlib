package org.dayatang.domain.internal.repo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.NamedParameters;
import org.dayatang.utils.Assert;

/**
 * 判断某个属性的值是否位于指定的值空间范围的查询条件
 * @author yyang
 */
public class BetweenCriterion extends BasicCriterion {

    private final Comparable<?> from;

    private final Comparable<?> to;

    /**
     * 创建查询条件实例
     * @param propName 属性名称
     * @param from 值的下限
     * @param to 值的上限
     */
    public BetweenCriterion(String propName, Comparable<?> from, Comparable<?> to) {
        super(propName);
        Assert.notNull(from, "From value is null!");
        Assert.notNull(to, "To value is null!");
        this.from = from;
        this.to = to;
    }

    /**
     * 获取属性值上限
     * @return 属性值上限
     */
    public Comparable<?> getFrom() {
        return from;
    }

    /**
     * 获取属性值下限
     * @return 属性值下限
     */
    public Comparable<?> getTo() {
        return to;
    }

    @Override
    public String toQueryString() {
        return String.format("%s between %s and %s", 
                getPropNameWithAlias(),
                getParamNameWithColon() + "_from",
                getParamNameWithColon() + "_to");
    }

    @Override
    public NamedParameters getParameters() {
        return NamedParameters.create()
                .add(getParamName() + "_from", from)
                .add(getParamName() + "_to", to);
    }

    /**
     * 判断等价性
     * @param other 要用来判等的另一个对象
     * @return 如果当前对象和other等价，则返回true，否则返回false
     */
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
                .append(this.getFrom(), that.getFrom())
                .append(this.getTo(), that.getTo())
                .isEquals();
    }

    /**
     * 计算哈希值
     * @return 当前对象实例的哈希值
     */
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
