package com.dayatang.domain;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: yyang
 * Date: 13-10-17
 * Time: 下午2:14
 * To change this template use File | Settings | File Templates.
 */
public interface Query<T extends Entity> {

    Class<T> getEntityClass();

    int getFirstResult();

    int getMaxResults();

    Set<QueryCriterion> getQueryCriterions();

    List<OrderSetting> getOrderSettings();

    Query<T> select(String... props);

    Query<T> eq(String propName, Object value);

    Query<T> notEq(String propName, Object value);

    Query<T> ge(String propName, Comparable<?> value);

    Query<T> gt(String propName, Comparable<?> value);

    Query<T> le(String propName, Comparable<?> value);

    Query<T> lt(String propName, Comparable<?> value);

    Query<T> eqProp(String propName, String otherProp);

    Query<T> notEqProp(String propName, String otherProp);

    Query<T> gtProp(String propName, String otherProp);

    Query<T> geProp(String propName, String otherProp);

    Query<T> ltProp(String propName, String otherProp);

    Query<T> leProp(String propName, String otherProp);

    Query<T> sizeEq(String propName, int size);

    Query<T> sizeNotEq(String propName, int size);

    Query<T> sizeGt(String propName, int size);

    Query<T> sizeGe(String propName, int size);

    Query<T> sizeLt(String propName, int size);

    Query<T> sizeLe(String propName, int size);

    Query<T> containsText(String propName, String value);

    Query<T> startsWithText(String propName, String value);

    Query<T> in(String propName, Collection<? extends Object> value);

    Query<T> in(String propName, Object[] value);

    Query<T> notIn(String propName, Collection<? extends Object> value);

    Query<T> notIn(String propName, Object[] value);

    <E> Query<T> between(String propName, Comparable<E> from, Comparable<E> to);

    Query<T> isNull(String propName);

    Query<T> notNull(String propName);

    Query<T> isEmpty(String propName);

    Query<T> notEmpty(String propName);

    Query<T> not(QueryCriterion criterion);

    Query<T> and(QueryCriterion... queryCriterions);

    Query<T> or(QueryCriterion... queryCriterions);

    Query<T> setFirstResult(int firstResult);

    Query<T> setMaxResults(int maxResults);

    Query<T> asc(String propName);

    Query<T> desc(String propName);

    List<T> list();

    T singleResult();

    <E> List<E> list(Class<E> resultClass);

    <E> E singleResult(Class<E> resultClass);
}
