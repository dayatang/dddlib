package org.dayatang.domain.internal.repo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.NamedParameters;
import org.dayatang.domain.QueryCriterion;
import org.dayatang.utils.Assert;


/**
 * 代表两个或两个以上的查询条件OR操作结果的查询条件
 * @author yyang
 */
public class OrCriterion extends AbstractCriterion {

    private final List<QueryCriterion> criterions;

    /**
     * 根据多个查询条件创建OR查询条件。创建过程中会去除为null或EmptyCriterion的查询条件。如果
     * 剩余查询条件不足两个，则抛出异常。
     * @param criterions 要用来执行OR操作的查询条件
     */
    public OrCriterion(QueryCriterion... criterions) {
        Assert.notNull(criterions, "Criterions to \"OR\" is null!");
        this.criterions = removeNullOrEmptyCriterion(criterions);
        Assert.isTrue(criterions.length > 1, "At least two query criterions required!");
    }

    /**
     * 获得要用来执行OR操作的查询条件
     * @return 要用来执行AND操作的查询条件，去除了Null和EmptyCriterion类型的元素。
     */
    public List<QueryCriterion> getCriterons() {
        return criterions;
    }

    @Override
	public String toQueryString() {
        List<String> subCriterionsStr = new ArrayList<String>();
        for (QueryCriterion each : getCriterons()) {
            subCriterionsStr.add(each.toQueryString());
        }
		return "(" + StringUtils.join(subCriterionsStr, " or ") + ")";
	}

	@Override
	public NamedParameters getParameters() {
		NamedParameters result = NamedParameters.create();
        for (QueryCriterion each : getCriterons()) {
        	result.add(each.getParameters());
        }
		return result;
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
        if (!(other instanceof OrCriterion)) {
            return false;
        }
        OrCriterion that = (OrCriterion) other;
        return new EqualsBuilder()
                .append(this.getCriterons(), that.getCriterons())
                .isEquals();
    }

    /**
     * 计算哈希值
     * @return 当前对象实例的哈希值
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getCriterons()).toHashCode();
    }
}
