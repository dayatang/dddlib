package org.dayatang.domain.internal.repo;

/**
 * 代表属性不等于指定值的查询条件
 * @author yyang
 */
public class NotEqCriterion extends ValueCompareCriterion {

    public NotEqCriterion(String propName, Object value) {
        super(propName, value);
        setOperator(" != ");
    }
}
