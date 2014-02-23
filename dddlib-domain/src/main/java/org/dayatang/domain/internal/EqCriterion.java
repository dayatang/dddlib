package org.dayatang.domain.internal;

/**
 * 代表属性等于指定值的查询条件
 * @author yyang
 */
public class EqCriterion extends ValueCompareCriterion {

    public EqCriterion(String propName, Object value) {
        super(propName, value);
        setOperator(" = ");
    }
}
