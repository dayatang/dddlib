package org.dayatang.domain.internal;

/**
 * 代表属性小于指定值的查询条件
 * @author yyang
 */
public class LtCriterion extends ValueCompareCriterion {

	public LtCriterion(String propName, Comparable<?> value) {
        super(propName, value);
        setOperator(" < ");
    }
}
