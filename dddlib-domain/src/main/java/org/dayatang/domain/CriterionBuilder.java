package org.dayatang.domain;

import java.util.Collection;

/**
 * 一个工具类，作为各种查询条件的工厂
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public interface CriterionBuilder {

    QueryCriterion eq(String propName, Object value);

    QueryCriterion notEq(String propName, Object value);

    QueryCriterion ge(String propName, Comparable<?> value);

    QueryCriterion gt(String propName, Comparable<?> value);

    QueryCriterion le(String propName, Comparable<?> value);

    QueryCriterion lt(String propName, Comparable<?> value);

    QueryCriterion eqProp(String propName1, String propName2);

    QueryCriterion notEqProp(String propName1, String propName2);

    QueryCriterion gtProp(String propName1, String propName2);

    QueryCriterion geProp(String propName1, String propName2);

    QueryCriterion ltProp(String propName1, String propName2);

    QueryCriterion leProp(String propName1, String propName2);

    QueryCriterion sizeEq(String propName, int size);

    QueryCriterion sizeNotEq(String propName, int size);

    QueryCriterion sizeGt(String propName, int size);

    QueryCriterion sizeGe(String propName, int size);

    QueryCriterion sizeLt(String propName, int size);

    QueryCriterion sizeLe(String propName, int size);

    QueryCriterion containsText(String propName, String value);

    QueryCriterion startsWithText(String propName, String value);

    QueryCriterion in(String propName, Collection<?> value);

    QueryCriterion in(String propName, Object[] value);

    QueryCriterion notIn(String propName, Collection<?> value);

    QueryCriterion notIn(String propName, Object[] value);

    QueryCriterion between(String propName, Comparable<?> from, Comparable<?> to);

    QueryCriterion isNull(String propName);

    QueryCriterion notNull(String propName);

    QueryCriterion isEmpty(String propName);

    QueryCriterion notEmpty(String propName);

    QueryCriterion not(QueryCriterion criterion);

    QueryCriterion and(QueryCriterion... criterions);

    QueryCriterion or(QueryCriterion... criterions);

    QueryCriterion isTrue(String propName);

    QueryCriterion isFalse(String propName);

    QueryCriterion isBlank(String propName);

    QueryCriterion notBlank(String propName);

}
