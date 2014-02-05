package org.dayatang.domain;

import org.dayatang.utils.Assert;

import java.util.*;

/**
 * 条件查询
 * User: yyang
 * Date: 13-10-17
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class CriteriaQueryImpl implements CriteriaQuery {

    private EntityRepository repository;
    private List<String> selectedProps;
    private Class<? extends Entity> entityClass;
    private int firstResult;
    private int maxResults;
    private Map<String, String> aliases = new LinkedHashMap<String, String>();
    private Set<QueryCriterion> queryCriterions = new HashSet<QueryCriterion>();
    private List<OrderSetting> orderSettings = new ArrayList<OrderSetting>();
    private Criterions criterions = Criterions.singleton();


    public CriteriaQueryImpl(EntityRepository repository, Class<? extends Entity> entityClass) {
        Assert.notNull(repository);
        Assert.notNull(entityClass);
        this.repository = repository;
        this.entityClass = entityClass;
    }

    public CriteriaQuery select(String... props) {
        selectedProps = Arrays.asList(props);
        return this;
    }

    public CriteriaQuery eq(String propName, Object value) {
        addCriterion(criterions.eq(propName, value));
        return this;
    }

    public CriteriaQuery notEq(String propName, Object value) {
        addCriterion(criterions.notEq(propName, value));
        return this;
    }

    public CriteriaQuery gt(String propName, Comparable<?> value) {
        addCriterion(criterions.gt(propName, value));
        return this;
    }

    public CriteriaQuery ge(String propName, Comparable<?> value) {
        addCriterion(criterions.ge(propName, value));
        return this;
    }

    public CriteriaQuery lt(String propName, Comparable<?> value) {
        addCriterion(criterions.lt(propName, value));
        return this;
    }

    public CriteriaQuery le(String propName, Comparable<?> value) {
        addCriterion(criterions.le(propName, value));
        return this;
    }

    public CriteriaQuery eqProp(String propName, String otherProp) {
        addCriterion(criterions.eqProp(propName, otherProp));
        return this;
    }

    public CriteriaQuery notEqProp(String propName, String otherProp) {
        addCriterion(criterions.notEqProp(propName, otherProp));
        return this;
    }

    public CriteriaQuery gtProp(String propName, String otherProp) {
        addCriterion(criterions.gtProp(propName, otherProp));
        return this;
    }

    public CriteriaQuery geProp(String propName, String otherProp) {
        addCriterion(criterions.geProp(propName, otherProp));
        return this;
    }

    public CriteriaQuery ltProp(String propName, String otherProp) {
        addCriterion(criterions.ltProp(propName, otherProp));
        return this;
    }

    public CriteriaQuery leProp(String propName, String otherProp) {
        addCriterion(criterions.leProp(propName, otherProp));
        return this;
    }

    public CriteriaQuery sizeEq(String propName, int size) {
        addCriterion(criterions.sizeEq(propName, size));
        return this;
    }

    public CriteriaQuery sizeNotEq(String propName, int size) {
        addCriterion(criterions.sizeNotEq(propName, size));
        return this;
    }

    public CriteriaQuery sizeGt(String propName, int size) {
        addCriterion(criterions.sizeGt(propName, size));
        return this;
    }

    public CriteriaQuery sizeGe(String propName, int size) {
        addCriterion(criterions.sizeGe(propName, size));
        return this;
    }

    public CriteriaQuery sizeLt(String propName, int size) {
        addCriterion(criterions.sizeLt(propName, size));
        return this;
    }

    public CriteriaQuery sizeLe(String propName, int size) {
        addCriterion(criterions.sizeLe(propName, size));
        return this;
    }

    public CriteriaQuery containsText(String propName, String value) {
        addCriterion(criterions.containsText(propName, value));
        return this;
    }

    public CriteriaQuery startsWithText(String propName, String value) {
        addCriterion(criterions.startsWithText(propName, value));
        return this;
    }

    public CriteriaQuery in(String propName, Collection<? extends Object> value) {
        addCriterion(criterions.in(propName, value));
        return this;
    }

    public CriteriaQuery in(String propName, Object[] value) {
        addCriterion(criterions.in(propName, value));
        return this;
    }

    public CriteriaQuery notIn(String propName, Collection<? extends Object> value) {
        addCriterion(criterions.notIn(propName, value));
        return this;
    }

    public CriteriaQuery notIn(String propName, Object[] value) {
        addCriterion(criterions.notIn(propName, value));
        return this;
    }

    public <E> CriteriaQuery between(String propName, Comparable<E> from, Comparable<E> to) {
        addCriterion(criterions.between(propName, from, to));
        return this;
    }

    public CriteriaQuery isNull(String propName) {
        addCriterion(criterions.isNull(propName));
        return this;
    }

    public CriteriaQuery notNull(String propName) {
        addCriterion(criterions.notNull(propName));
        return this;
    }

    public CriteriaQuery isEmpty(String propName) {
        addCriterion(criterions.isEmpty(propName));
        return this;
    }

    public CriteriaQuery notEmpty(String propName) {
        addCriterion(criterions.notEmpty(propName));
        return this;
    }


    public CriteriaQuery isTrue(String propName) {
        addCriterion(criterions.isTrue(propName));
        return this;
    }

    public CriteriaQuery isFalse(String propName) {
        addCriterion(criterions.isFalse(propName));
        return this;
    }

    public CriteriaQuery isBlank(String propName) {
        addCriterion(criterions.isBlank(propName));
        return this;
    }

    public CriteriaQuery notBlank(String propName) {
        addCriterion(criterions.notBlank(propName));
        return this;
    }

    public CriteriaQuery not(QueryCriterion criterion) {
        addCriterion(criterions.not(criterion));
        return this;
    }

    public CriteriaQuery and(QueryCriterion... queryCriterions) {
        addCriterion(criterions.and(queryCriterions));
        return this;
    }

    public CriteriaQuery or(QueryCriterion... queryCriterions) {
        addCriterion(criterions.or(queryCriterions));
        return this;
    }

    private void addCriterion(QueryCriterion criterion) {
        queryCriterions.add(criterion);
    }

    public CriteriaQuery setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    public CriteriaQuery setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public CriteriaQuery asc(String propName) {
        orderSettings.add(OrderSetting.asc(propName));
        return this;
    }

    public CriteriaQuery desc(String propName) {
        orderSettings.add(OrderSetting.desc(propName));
        return this;
    }

    public List<String> getSelectedProps() {
        return Collections.unmodifiableList(selectedProps);
    }

    /**
     * @return the entityClass
     */
    public Class getEntityClass() {
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
    public <T> List<T> list() {
        return repository.find(this);
    }

    @Override
    public <T> T singleResult() {
        return repository.getSingleResult(this);
    }
}
