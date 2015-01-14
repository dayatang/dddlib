package org.dayatang.domain.internal.repo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.NamedParameters;
import org.dayatang.domain.QueryCriterion;
import org.dayatang.utils.Assert;

/**
 * 代表某个查询条件的取反的查询条件
 * @author yyang
 */
public class NotCriterion extends AbstractCriterion {

    private final QueryCriterion criterion;

    /**
     * 根据一个查询条件创建它的取反查询条件
     * @param criterion 原本的查询条件
     */
    public NotCriterion(QueryCriterion criterion) {
        Assert.notNull(criterion, "Query criterion is null!");
        this.criterion = criterion;
    }

    /**
     * 返回原本的查询条件
     * @return 原本的查询条件
     */
    public QueryCriterion getCriteron() {
        return criterion;
    }

    @Override
    public String toQueryString() {
        return "not (" + criterion.toQueryString() + ")";
    }

    @Override
    public NamedParameters getParameters() {
        return criterion.getParameters();
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
        if (!(other instanceof NotCriterion)) {
            return false;
        }
        NotCriterion that = (NotCriterion) other;
        return new EqualsBuilder()
                .append(this.getCriteron(), that.getCriteron())
                .isEquals();
    }

    /**
     * 计算哈希值
     * @return 当前对象实例的哈希值
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getCriteron()).toHashCode();
    }

}
