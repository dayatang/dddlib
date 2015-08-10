package org.dayatang.domain;

import java.util.Collection;

/**
 * 查询条件生成器，生成各种查询条件。
 * 该类主要用于DDDLib的内部实现机制。DDDLib用户请使用Criteria代替CriterionBuilder
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

    QueryCriterion eqProp(String propName, String otherPropName);

    QueryCriterion notEqProp(String propName, String otherPropName);

    QueryCriterion gtProp(String propName, String otherPropName);

    QueryCriterion geProp(String propName, String otherPropName);

    QueryCriterion ltProp(String propName, String otherPropName);

    QueryCriterion leProp(String propName, String otherPropName);

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

	QueryCriterion empty();

}
