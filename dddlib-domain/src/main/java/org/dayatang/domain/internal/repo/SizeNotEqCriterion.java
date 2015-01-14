package org.dayatang.domain.internal.repo;

/**
 * 判断某个集合属性的记录数不等于指定值的查询条件
 * @author yyang
 */
public class SizeNotEqCriterion extends SizeCompareCriterion {

    /**
     * 创建查询条件
     * @param propName 属性名
     * @param value 属性值
     */
    public SizeNotEqCriterion(String propName, int value) {
        super(propName, value);
        setOperator(" != ");
    }
}
