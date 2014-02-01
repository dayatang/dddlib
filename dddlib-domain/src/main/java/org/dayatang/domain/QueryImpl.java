package org.dayatang.domain;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: yyang
 * Date: 13-10-17
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class QueryImpl<T extends Entity> implements Query<T> {

    private EntityRepository repository;
    private List<String> selectedProps;
    private Class<T> entityClass;
    private int firstResult;
    private int maxResults;
    private Map<String, String> aliases = new LinkedHashMap<String, String>();
    private Set<QueryCriterion> queryCriterions = new HashSet<QueryCriterion>();
    private List<OrderSetting> orderSettings = new ArrayList<OrderSetting>();
    private Criterions criterions = Criterions.singleton();


    public QueryImpl(EntityRepository repository, Class<T> entityClass) {
        this.repository = repository;
        this.entityClass = entityClass;
    }

    public Query<T> select(String... props) {
        selectedProps = Arrays.asList(props);
        return this;
    }

    public Query<T> eq(String propName, Object value) {
        addCriterion(criterions.eq(propName, value));
        return this;
    }

    public Query<T> notEq(String propName, Object value) {
        addCriterion(criterions.notEq(propName, value));
        return this;
    }

    public Query<T> gt(String propName, Comparable<?> value) {
        addCriterion(criterions.gt(propName, value));
        return this;
    }

    public Query<T> ge(String propName, Comparable<?> value) {
        addCriterion(criterions.ge(propName, value));
        return this;
    }

    public Query<T> lt(String propName, Comparable<?> value) {
        addCriterion(criterions.lt(propName, value));
        return this;
    }

    public Query<T> le(String propName, Comparable<?> value) {
        addCriterion(criterions.le(propName, value));
        return this;
    }

    public Query<T> eqProp(String propName, String otherProp) {
        addCriterion(criterions.eqProp(propName, otherProp));
        return this;
    }

    public Query<T> notEqProp(String propName, String otherProp) {
        addCriterion(criterions.notEqProp(propName, otherProp));
        return this;
    }

    public Query<T> gtProp(String propName, String otherProp) {
        addCriterion(criterions.gtProp(propName, otherProp));
        return this;
    }

    public Query<T> geProp(String propName, String otherProp) {
        addCriterion(criterions.geProp(propName, otherProp));
        return this;
    }

    public Query<T> ltProp(String propName, String otherProp) {
        addCriterion(criterions.ltProp(propName, otherProp));
        return this;
    }

    public Query<T> leProp(String propName, String otherProp) {
        addCriterion(criterions.leProp(propName, otherProp));
        return this;
    }

    public Query<T> sizeEq(String propName, int size) {
        addCriterion(criterions.sizeEq(propName, size));
        return this;
    }

    public Query<T> sizeNotEq(String propName, int size) {
        addCriterion(criterions.sizeNotEq(propName, size));
        return this;
    }

    public Query<T> sizeGt(String propName, int size) {
        addCriterion(criterions.sizeGt(propName, size));
        return this;
    }

    public Query<T> sizeGe(String propName, int size) {
        addCriterion(criterions.sizeGe(propName, size));
        return this;
    }

    public Query<T> sizeLt(String propName, int size) {
        addCriterion(criterions.sizeLt(propName, size));
        return this;
    }

    public Query<T> sizeLe(String propName, int size) {
        addCriterion(criterions.sizeLe(propName, size));
        return this;
    }

    public Query<T> containsText(String propName, String value) {
        addCriterion(criterions.containsText(propName, value));
        return this;
    }

    public Query<T> startsWithText(String propName, String value) {
        addCriterion(criterions.startsWithText(propName, value));
        return this;
    }

    public Query<T> in(String propName, Collection<? extends Object> value) {
        addCriterion(criterions.in(propName, value));
        return this;
    }

    public Query<T> in(String propName, Object[] value) {
        addCriterion(criterions.in(propName, value));
        return this;
    }

    public Query<T> notIn(String propName, Collection<? extends Object> value) {
        addCriterion(criterions.notIn(propName, value));
        return this;
    }

    public Query<T> notIn(String propName, Object[] value) {
        addCriterion(criterions.notIn(propName, value));
        return this;
    }

    public <E> Query between(String propName, Comparable<E> from, Comparable<E> to) {
        addCriterion(criterions.between(propName, from, to));
        return this;
    }

    public Query<T> isNull(String propName) {
        addCriterion(criterions.isNull(propName));
        return this;
    }

    public Query<T> notNull(String propName) {
        addCriterion(criterions.notNull(propName));
        return this;
    }

    public Query<T> isEmpty(String propName) {
        addCriterion(criterions.isEmpty(propName));
        return this;
    }

    public Query<T> notEmpty(String propName) {
        addCriterion(criterions.notEmpty(propName));
        return this;
    }


    public Query<T> isTrue(String propName) {
        addCriterion(criterions.isTrue(propName));
        return this;
    }

    public Query<T> isFalse(String propName) {
        addCriterion(criterions.isFalse(propName));
        return this;
    }

    public Query<T> isBlank(String propName) {
        addCriterion(criterions.isBlank(propName));
        return this;
    }

    public Query<T> notBlank(String propName) {
        addCriterion(criterions.notBlank(propName));
        return this;
    }

    public Query<T> not(QueryCriterion criterion) {
        addCriterion(criterions.not(criterion));
        return this;
    }

    public Query<T> and(QueryCriterion... queryCriterions) {
        addCriterion(criterions.and(queryCriterions));
        return this;
    }

    public Query<T> or(QueryCriterion... queryCriterions) {
        addCriterion(criterions.or(queryCriterions));
        return this;
    }

    private void addCriterion(QueryCriterion criterion) {
        queryCriterions.add(criterion);
    }

    public Query<T> setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    public Query<T> setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public Query<T> asc(String propName) {
        orderSettings.add(OrderSetting.asc(propName));
        return this;
    }

    public Query<T> desc(String propName) {
        orderSettings.add(OrderSetting.desc(propName));
        return this;
    }

    public List<String> getSelectedProps() {
        return Collections.unmodifiableList(selectedProps);
    }

    /**
     * @return the entityClass
     */
    public Class<T> getEntityClass() {
        return entityClass;
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

    @Override
    public List<T> list() {
        return repository.find(this);
    }

    @Override
    public T singleResult() {
        return repository.getSingleResult(this);
    }

    public <E> List<E> list(Class<E> resultClass) {
         return repository.find(this, resultClass);
    }

    public <E> E singleResult(Class<E> resultClass) {
         return repository.getSingleResult(this, resultClass);
    }
}
