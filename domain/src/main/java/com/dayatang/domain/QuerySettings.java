package com.dayatang.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * 数据仓储查询设定对象,用来收集各种查询设定,如过滤条件,分页信息,排序信息等等.
 * @author cnyangyu
 *
 */
public class QuerySettings<T> {
	
	private Class<T> entityClass;
	private String rootAlias;
	private int firstResult;
	private int maxResults;
	private Map<String, String> aliases = new LinkedHashMap<String, String>();
	private Set<QueryCriterion> queryCriterions = new HashSet<QueryCriterion>();
	private List<OrderSetting> orderSettings = new ArrayList<OrderSetting>();
	private Criterions criterions = Criterions.singleton();
	
	public static <T extends Entity> QuerySettings<T> create(Class<T> entityClass) {
		return new QuerySettings<T>(entityClass);
	}
	
	public static <T extends Entity> QuerySettings<T> create(Class<T> entityClass, String alias) {
		return new QuerySettings<T>(entityClass, alias);
	}
	
	private QuerySettings(Class<T> entityClass) {
		super();
		this.entityClass = entityClass;
	}

	public QuerySettings(Class<T> entityClass, String alias) {
		this.entityClass = entityClass;
		this.rootAlias = alias;
	}

	/**
	 * @return the entityClass
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}

	public String getRootAlias() {
		return rootAlias;
	}

	public Map<String, String> getAliases() {
		return aliases;
	}

	public Set<QueryCriterion> getQueryCriterions() {
		return queryCriterions;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public List<OrderSetting> getOrderSettings() {
		return orderSettings;
	}

	public QuerySettings<T> alias(String propName, String aliasName) {
		aliases.put(propName, aliasName);
		return this;
	}

	public QuerySettings<T> eq(String propName, Object value) {
		addCriterion(criterions.eq(propName, value));
		return this;
	}
	
	public QuerySettings<T> notEq(String propName, Object value) {
		addCriterion(criterions.notEq(propName, value));
		return this;
	}
	
	public QuerySettings<T> ge(String propName, Comparable<?> value) {
		addCriterion(criterions.ge(propName, value));
		return this;
	}
	
	public QuerySettings<T> gt(String propName, Comparable<?> value) {
		addCriterion(criterions.gt(propName, value));
		return this;
	}
	
	public QuerySettings<T> le(String propName, Comparable<?> value) {
		addCriterion(criterions.le(propName, value));
		return this;
	}
	
	public QuerySettings<T> lt(String propName, Comparable<?> value) {
		addCriterion(criterions.lt(propName, value));
		return this;
	}
	
	public QuerySettings<T> eqProp(String propName, String otherProp) {
		addCriterion(criterions.eqProp(propName, otherProp));
		return this;
	}
	
	public QuerySettings<T> notEqProp(String propName, String otherProp) {
		addCriterion(criterions.notEqProp(propName, otherProp));
		return this;
	}
	
	public QuerySettings<T> gtProp(String propName, String otherProp) {
		addCriterion(criterions.gtProp(propName, otherProp));
		return this;
	}
	
	public QuerySettings<T> geProp(String propName, String otherProp) {
		addCriterion(criterions.geProp(propName, otherProp));
		return this;
	}
	
	public QuerySettings<T> ltProp(String propName, String otherProp) {
		addCriterion(criterions.ltProp(propName, otherProp));
		return this;
	}
	
	public QuerySettings<T> leProp(String propName, String otherProp) {
		addCriterion(criterions.leProp(propName, otherProp));
		return this;
	}
	
	public QuerySettings<T> sizeEq(String propName, int size) {
		addCriterion(criterions.sizeEq(propName, size));
		return this;
	}
	
	public QuerySettings<T> sizeNotEq(String propName, int size) {
		addCriterion(criterions.sizeNotEq(propName, size));
		return this;
	}
	
	public QuerySettings<T> sizeGt(String propName, int size) {
		addCriterion(criterions.sizeGt(propName, size));
		return this;
	}
	
	public QuerySettings<T> sizeGe(String propName, int size) {
		addCriterion(criterions.sizeGe(propName, size));
		return this;
	}
	
	public QuerySettings<T> sizeLt(String propName, int size) {
		addCriterion(criterions.sizeLt(propName, size));
		return this;
	}
	
	public QuerySettings<T> sizeLe(String propName, int size) {
		addCriterion(criterions.sizeLe(propName, size));
		return this;
	}

	public QuerySettings<T> containsText(String propName, String value) {
		addCriterion(criterions.containsText(propName, value));
		return this;
	}

	public QuerySettings<T> startsWithText(String propName, String value) {
		addCriterion(criterions.startsWithText(propName, value));
		return this;
	}

	public QuerySettings<T> in(String propName, Collection<? extends Object> value) {
		addCriterion(criterions.in(propName, value));
		return this;
	}

	public QuerySettings<T> in(String propName, Object[] value) {
		addCriterion(criterions.in(propName, value));
		return this;
	}

	public QuerySettings<T> notIn(String propName, Collection<? extends Object> value) {
		addCriterion(criterions.notIn(propName, value));
		return this;
	}

	public QuerySettings<T> notIn(String propName, Object[] value) {
		addCriterion(criterions.notIn(propName, value));
		return this;
	}

	public <E extends Object> QuerySettings<T> between(String propName, Comparable<E> from, Comparable<E> to) {
		addCriterion(criterions.between(propName, from, to));
		return this;
	}
	
	public QuerySettings<T> isNull(String propName) {
		addCriterion(criterions.isNull(propName));
		return this;
	}
	
	public QuerySettings<T> notNull(String propName) {
		addCriterion(criterions.notNull(propName));
		return this;
	}
	
	public QuerySettings<T> isEmpty(String propName) {
		addCriterion(criterions.isEmpty(propName));
		return this;
	}
	
	public QuerySettings<T> notEmpty(String propName) {
		addCriterion(criterions.notEmpty(propName));
		return this;
	}
	
	public QuerySettings<T> not(QueryCriterion criterion) {
		addCriterion(criterions.not(criterion));
		return this;
	}
	
	public QuerySettings<T> and(QueryCriterion... queryCriterions) {
		addCriterion(criterions.and(queryCriterions));
		return this;
	}
	
	public QuerySettings<T> or(QueryCriterion... queryCriterions) {
		addCriterion(criterions.or(queryCriterions));
		return this;
	}

	private void addCriterion(QueryCriterion criterion) {
		queryCriterions.add(criterion);
	}

	public QuerySettings<T> setFirstResult(int firstResult) {
		this.firstResult = firstResult;
		return this;
	}

	public QuerySettings<T> setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public QuerySettings<T> asc(String propName) {
		orderSettings.add(OrderSetting.asc(propName));
		return this;
	}

	public QuerySettings<T> desc(String propName) {
		orderSettings.add(OrderSetting.desc(propName));
		return this;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof QuerySettings)) {
			return false;
		}
		QuerySettings castOther = (QuerySettings) other;
		return new EqualsBuilder()
				.append(entityClass, castOther.entityClass)
				.append(queryCriterions, castOther.queryCriterions)
				.append(firstResult, castOther.firstResult)
				.append(maxResults, castOther.maxResults)
				.append(orderSettings, castOther.orderSettings)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(entityClass)
				.append(queryCriterions)
				.append(firstResult)
				.append(maxResults)
				.append(orderSettings)
				.toHashCode();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Class:").append(entityClass.getSimpleName()).append(SystemUtils.LINE_SEPARATOR);
		result.append("queryCriterions: [");
		for (QueryCriterion criteron : queryCriterions) {
			result.append(criteron);
		}
		result.append("]").append(SystemUtils.LINE_SEPARATOR);
		result.append("firstResult:" + firstResult).append(SystemUtils.LINE_SEPARATOR);
		result.append("maxResults" + maxResults).append(SystemUtils.LINE_SEPARATOR);
		result.append("orderSettings: [");
		for (OrderSetting orderSetting : orderSettings) {
			result.append(orderSetting);
		}
		result.append("]");
		return result.toString();
	}
	
}
