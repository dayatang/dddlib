package com.dayatang.domain;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * 一个工具类，作为各种查询条件的工厂
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public abstract class Criterions {
    
    private static Criterions instance = getInstance();

    public static Criterions singleton() {
    	return instance;
    }
    
	protected Criterions() {
	}

	public QueryCriterion eq(String propName, Object value) {
		return instance.eq(propName, value);
	}
	
	public QueryCriterion notEq(String propName, Object value) {
		return instance.notEq(propName, value);
	}
	
	public QueryCriterion ge(String propName, Comparable<?> value) {
		return instance.ge(propName, value);
	}
	
	public QueryCriterion gt(String propName, Comparable<?> value) {
		return instance.gt(propName, value);
	}
	
	public QueryCriterion le(String propName, Comparable<?> value) {
		return instance.le(propName, value);
	}
	
	public QueryCriterion lt(String propName, Comparable<?> value) {
		return instance.lt(propName, value);
	}
	
	public QueryCriterion eqProp(String propName1, String propName2) {
		return instance.eqProp(propName1, propName2);
	}
	
	public QueryCriterion notEqProp(String propName1, String propName2) {
		return instance.notEqProp(propName1, propName2);
	}
	
	public QueryCriterion gtProp(String propName1, String propName2) {
		return instance.gtProp(propName1, propName2);
	}
	
	public QueryCriterion geProp(String propName1, String propName2) {
		return instance.geProp(propName1, propName2);
	}
	
	public QueryCriterion ltProp(String propName1, String propName2) {
		return instance.ltProp(propName1, propName2);
	}
	
	public QueryCriterion leProp(String propName1, String propName2) {
		return instance.leProp(propName1, propName2);
	}
	
	public QueryCriterion sizeEq(String propName, int size) {
		return instance.sizeEq(propName, size);
	}
	
	public QueryCriterion sizeNotEq(String propName, int size) {
		return instance.sizeNotEq(propName, size);
	}
	
	public QueryCriterion sizeGt(String propName, int size) {
		return instance.sizeGt(propName, size);
	}
	
	public QueryCriterion sizeGe(String propName, int size) {
		return instance.sizeGe(propName, size);
	}
	
	public QueryCriterion sizeLt(String propName, int size) {
		return instance.sizeLt(propName, size);
	}
	
	public QueryCriterion sizeLe(String propName, int size) {
		return instance.sizeLe(propName, size);
	}

	public QueryCriterion containsText(String propName, String value) {
		return instance.containsText(propName, value);
	}

	public QueryCriterion startsWithText(String propName, String value) {
		return instance.startsWithText(propName, value);
	}

	public QueryCriterion in(String propName, Collection<?> value) {
		return instance.in(propName, value);
	}

	public QueryCriterion in(String propName, Object[] value) {
		return instance.in(propName, value);
	}

	public QueryCriterion notIn(String propName, Collection<?> value) {
		return instance.notIn(propName, value);
	}

	public QueryCriterion notIn(String propName, Object[] value) {
		return instance.notIn(propName, value);
	}

	public QueryCriterion between(String propName, Comparable<?> from, Comparable<?> to) {
		return instance.between(propName, from, to);
	}
	
	public QueryCriterion isNull(String propName) {
		return instance.isNull(propName);
	}
	
	public QueryCriterion notNull(String propName) {
		return instance.notNull(propName);
	}
	
	public QueryCriterion isEmpty(String propName) {
		return instance.isEmpty(propName);
	}
	
	public QueryCriterion notEmpty(String propName) {
		return instance.notEmpty(propName);
	}
	
	public QueryCriterion not(QueryCriterion criterion) {
		return not(criterion);
	}
	
	public QueryCriterion and(QueryCriterion... criterions) {
		return and(criterions);
	}
	
	public QueryCriterion or(QueryCriterion... criterions) {
		return instance.or(criterions);
	}

    private static Criterions getInstance() {
    	return ServiceLoader.load(Criterions.class).iterator().next();
    }
	
}
