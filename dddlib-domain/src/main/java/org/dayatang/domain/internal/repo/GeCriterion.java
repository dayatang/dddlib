package org.dayatang.domain.internal.repo;

/**
 * 代表属性小于或等于指定值的查询条件
 * @author yyang
 */
public class GeCriterion extends ValueCompareCriterion {

    public GeCriterion(String propName, Comparable<?> value) {
        super(propName, value);
        setOperator(" >= ");
    }
}
