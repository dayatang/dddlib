package org.dayatang.domain;

import java.util.Collection;

/**
 * 各种查询条件的静态工厂。
 * 将查询条件创建委托给CriterionBuilder实现。
 * Created by yyang on 15/8/10.
 */
public class Criteria {
    private static final CriterionBuilder BUILDER = InstanceFactory.getInstance(CriterionBuilder.class);

    public static QueryCriterion eq(String propName, Object value) {
        return BUILDER.eq(propName, value);
    }

    public static QueryCriterion notEq(String propName, Object value) {
        return BUILDER.notEq(propName, value);
    }

    public static QueryCriterion ge(String propName, Comparable<?> value) {
        return BUILDER.ge(propName, value);
    }

    public static QueryCriterion gt(String propName, Comparable<?> value) {
        return BUILDER.gt(propName, value);
    }

    public static QueryCriterion le(String propName, Comparable<?> value) {
        return BUILDER.le(propName, value);
    }

    public static QueryCriterion lt(String propName, Comparable<?> value) {
        return BUILDER.lt(propName, value);
    }

    public static QueryCriterion eqProp(String propName, String otherPropName) {
        return BUILDER.eqProp(propName, otherPropName);
    }

    public static QueryCriterion notEqProp(String propName, String otherPropName) {
        return BUILDER.notEqProp(propName, otherPropName);
    }

    public static QueryCriterion gtProp(String propName, String otherPropName) {
        return BUILDER.gtProp(propName, otherPropName);
    }

    public static QueryCriterion geProp(String propName, String otherPropName) {
        return BUILDER.geProp(propName, otherPropName);
    }

    public static QueryCriterion ltProp(String propName, String otherPropName) {
        return BUILDER.ltProp(propName, otherPropName);
    }

    public static QueryCriterion leProp(String propName, String otherPropName) {
        return BUILDER.leProp(propName, otherPropName);
    }

    public static QueryCriterion sizeEq(String propName, int size) {
        return BUILDER.sizeEq(propName, size);
    }

    public static QueryCriterion sizeNotEq(String propName, int size) {
        return BUILDER.sizeNotEq(propName, size);
    }

    public static QueryCriterion sizeGt(String propName, int size) {
        return BUILDER.sizeGt(propName, size);
    }

    public static QueryCriterion sizeGe(String propName, int size) {
        return BUILDER.sizeGe(propName, size);
    }

    public static QueryCriterion sizeLt(String propName, int size) {
        return BUILDER.sizeLt(propName, size);
    }

    public static QueryCriterion sizeLe(String propName, int size) {
        return BUILDER.sizeLe(propName, size);
    }

    public static QueryCriterion containsText(String propName, String value) {
        return BUILDER.containsText(propName, value);
    }

    public static QueryCriterion startsWithText(String propName, String value) {
        return BUILDER.startsWithText(propName, value);
    }

    public static QueryCriterion in(String propName, Collection<?> value) {
        return BUILDER.in(propName, value);
    }

    public static QueryCriterion in(String propName, Object[] value) {
        return BUILDER.in(propName, value);
    }

    public static QueryCriterion notIn(String propName, Collection<?> value) {
        return BUILDER.notIn(propName, value);
    }

    public static QueryCriterion notIn(String propName, Object[] value) {
        return BUILDER.notIn(propName, value);
    }

    public static QueryCriterion between(String propName, Comparable<?> from, Comparable<?> to) {
        return BUILDER.between(propName, from, to);
    }

    public static QueryCriterion isNull(String propName) {
        return BUILDER.isNull(propName);
    }

    public static QueryCriterion notNull(String propName) {
        return BUILDER.notNull(propName);
    }

    public static QueryCriterion isEmpty(String propName) {
        return BUILDER.isEmpty(propName);
    }

    public static QueryCriterion notEmpty(String propName) {
        return BUILDER.notEmpty(propName);
    }

    public static QueryCriterion isTrue(String propName) {
        return BUILDER.isTrue(propName);
    }

    public static QueryCriterion isFalse(String propName) {
        return BUILDER.isFalse(propName);
    }

    public static QueryCriterion isBlank(String propName) {
        return BUILDER.isBlank(propName);
    }

    public static QueryCriterion notBlank(String propName) {
        return BUILDER.notBlank(propName);
    }

    public static QueryCriterion not(QueryCriterion criterion) {
        return BUILDER.not(criterion);
    }

    public static QueryCriterion and(QueryCriterion... criteria) {
        return BUILDER.and(criteria);
    }

    public static QueryCriterion or(QueryCriterion... criteria) {
        return BUILDER.or(criteria);
    }

    public static QueryCriterion empty() {
        return BUILDER.empty();
    }


}
