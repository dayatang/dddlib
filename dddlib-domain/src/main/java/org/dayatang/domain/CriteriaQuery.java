package org.dayatang.domain;

import org.dayatang.utils.Assert;

import java.util.*;

/**
 * 条件查询。DDDLib支持的三种查询形式之一。通过DSL针对特定实体指定查询条件、排序属性和针对结果取子集等。
 * Created with IntelliJ IDEA.
 * User: yyang
 * Date: 13-10-17
 * Time: 下午2:14
 * To change this template use File | Settings | File Templates.
 */
public class CriteriaQuery {

    private EntityRepository repository;
    private List<String> selectedProps;
    private Class<? extends Entity> entityClass;
    private int firstResult;
    private int maxResults;
    private Map<String, String> aliases = new LinkedHashMap<String, String>();
    private Set<QueryCriterion> queryCriterions = new HashSet<QueryCriterion>();
    private List<OrderSetting> orderSettings = new ArrayList<OrderSetting>();
    private Criterions criterions = Criterions.singleton();

    public CriteriaQuery(EntityRepository repository, Class<? extends Entity> entityClass) {
        Assert.notNull(repository);
        Assert.notNull(entityClass);
        this.repository = repository;
        this.entityClass = entityClass;
    }

    /**
     * 获得查询根实体的类
     * @return 查询的根实体的类
     */
    public Class getEntityClass() {
        return entityClass;
    }

    /**
     * 对于从大结果集中选取部分的查询，获得数据集的起始位置（0代表第一条记录）
     * @return 一个数字，代表从大结果集的第几条记录开始选取子集
     */
    public int getFirstResult() {
        return firstResult;
    }

    /**
     * 对于从大结果集中选取部分的查询，设置数据集的起始获取位置（0代表第一条记录）
     * @param firstResult 一个数字，代表从大结果集的第几条记录开始选取子集
     * @return 当前查询对象
     */
    public CriteriaQuery setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * 对于从大结果集中选取部分的查询，获得本次查询返回的记录的最大数量
     * @return  一个数字，代表从大数据集中最多选取多少条记录
     */
    public int getMaxResults() {
        return maxResults;
    }

    /**
     * 对于从大结果集中选取部分的查询，获得本次查询返回的记录的最大数量
     * @param maxResults 一个数字，代表从大数据集中最多选取多少条记录
     * @return 当前查询对象
     */
    public CriteriaQuery setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * 获得查询条件的集合
     * @return  本次查询指定的条件的集合
     */
    public Set<QueryCriterion> getQueryCriterions() {
        return queryCriterions;
    }

    /**
     * 获得排序选项的列表（先按前面的排序选项排序，如果前面的选项相同，再按后面的选项排序）
     * @return 本次查询指定的排序选项的列表
     */
    public List<OrderSetting> getOrderSettings() {
        return orderSettings;
    }

    /**
     * 选择要返回的查询结果属性列表。如果props超过一个，查询返回属性数组的列表；
     * 如果props只有一个，查询返回该属性的列表；如果select任何东西，查询返回实体T的列表。
     * @param props 被查询的实体类型的属性的数组，例如Employee的name属性和sn属性
     * @return 当前查询对象
     */
    public CriteriaQuery select(String... props) {
        selectedProps = Arrays.asList(props);
        return this;
    }

    /**
     * 添加一个“属性名 = 属性值”的查询条件
     * @param propName 属性名
     * @param value 属性值
     * @return 当前查询对象
     */
    public CriteriaQuery eq(String propName, Object value) {
        addCriterion(criterions.eq(propName, value));
        return this;
    }

    /**
     * 添加一个“属性名 != 属性值”的查询条件
     * @param propName 属性名
     * @param value 属性值
     * @return 当前查询对象
     */
    public CriteriaQuery notEq(String propName, Object value) {
        addCriterion(criterions.notEq(propName, value));
        return this;
    }

    /**
     * 添加一个“属性名 > 属性值”的查询条件
     * @param propName 属性名
     * @param value 属性值
     * @return 当前查询对象
     */
    public CriteriaQuery gt(String propName, Comparable<?> value) {
        addCriterion(criterions.gt(propName, value));
        return this;
    }

    /**
     * 添加一个“属性名 >= 属性值”的查询条件
     * @param propName 属性名
     * @param value 属性值
     * @return 当前查询对象
     */
    public CriteriaQuery ge(String propName, Comparable<?> value) {
        addCriterion(criterions.ge(propName, value));
        return this;
    }

    /**
     * 添加一个“属性名 < 属性值”的查询条件
     * @param propName 属性名
     * @param value 属性值
     * @return 当前查询对象
     */
    public CriteriaQuery lt(String propName, Comparable<?> value) {
        addCriterion(criterions.lt(propName, value));
        return this;
    }

    /**
     * 添加一个“属性名 <= 属性值”的查询条件
     * @param propName 属性名
     * @param value 属性值
     * @return 当前查询对象
     */
    public CriteriaQuery le(String propName, Comparable<?> value) {
        addCriterion(criterions.le(propName, value));
        return this;
    }

    /**
     * 添加一个“属性1 = 属性2”的查询条件
     * @param propName 属性名
     * @param otherProp 另一个属性名
     * @return 当前查询对象
     */
    public CriteriaQuery eqProp(String propName, String otherProp) {
        addCriterion(criterions.eqProp(propName, otherProp));
        return this;
    }

    /**
     * 添加一个“属性1 != 属性2”的查询条件
     * @param propName 属性名
     * @param otherProp 另一个属性名
     * @return 当前查询对象
     */
    public CriteriaQuery notEqProp(String propName, String otherProp) {
        addCriterion(criterions.notEqProp(propName, otherProp));
        return this;
    }

    /**
     * 添加一个“属性1 > 属性2”的查询条件
     * @param propName 属性名
     * @param otherProp 另一个属性名
     * @return 当前查询对象
     */
    public CriteriaQuery gtProp(String propName, String otherProp) {
        addCriterion(criterions.gtProp(propName, otherProp));
        return this;
    }

    /**
     * 添加一个“属性1 >= 属性2”的查询条件
     * @param propName 属性名
     * @param otherProp 另一个属性名
     * @return 当前查询对象
     */
    public CriteriaQuery geProp(String propName, String otherProp) {
        addCriterion(criterions.geProp(propName, otherProp));
        return this;
    }

    /**
     * 添加一个“属性1 < 属性2”的查询条件
     * @param propName 属性名
     * @param otherProp 另一个属性名
     * @return 当前查询对象
     */
    public CriteriaQuery ltProp(String propName, String otherProp) {
        addCriterion(criterions.ltProp(propName, otherProp));
        return this;
    }

    /**
     * 添加一个“属性1 <= 属性2”的查询条件
     * @param propName 属性名
     * @param otherProp 另一个属性名
     * @return 当前查询对象
     */
    public CriteriaQuery leProp(String propName, String otherProp) {
        addCriterion(criterions.leProp(propName, otherProp));
        return this;
    }

    /**
     * 添加一个“集合属性的结果数量 = size”的查询条件。例如Order对象有个类型为List<OrderItem>的
     * 集合属性items，我们要查询订购了5种物品的订单，则propName为items，size为5.
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @param size 集合属性的结果数量
     * @return 当前查询对象
     */
    public CriteriaQuery sizeEq(String propName, int size) {
        addCriterion(criterions.sizeEq(propName, size));
        return this;
    }

    /**
     * 添加一个“集合属性的结果数量 != size”的查询条件。例如Order对象有个类型为List<OrderItem>的
     * 集合属性items，我们要查询订购了不是5种物品的订单，则propName为items，size为5.
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @param size 集合属性的结果数量
     * @return 当前查询对象
     */
    public CriteriaQuery sizeNotEq(String propName, int size) {
        addCriterion(criterions.sizeNotEq(propName, size));
        return this;
    }

    /**
     * 添加一个“集合属性的结果数量 > size”的查询条件。例如Order对象有个类型为List<OrderItem>的
     * 集合属性items，我们要查询订购了超过5种物品的订单，则propName为items，size为5.
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @param size 集合属性的结果数量
     * @return 当前查询对象
     */
    public CriteriaQuery sizeGt(String propName, int size) {
        addCriterion(criterions.sizeGt(propName, size));
        return this;
    }

    /**
     * 添加一个“集合属性的结果数量 >= size”的查询条件。例如Order对象有个类型为List<OrderItem>的
     * 集合属性items，我们要查询订购了5种或5种以上物品的订单，则propName为items，size为5.
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @param size 集合属性的结果数量
     * @return 当前查询对象
     */
    public CriteriaQuery sizeGe(String propName, int size) {
        addCriterion(criterions.sizeGe(propName, size));
        return this;
    }

    /**
     * 添加一个“集合属性的结果数量 < size”的查询条件。例如Order对象有个类型为List<OrderItem>的
     * 集合属性items，我们要查询订购了少于5种物品的订单，则propName为items，size为5.
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @param size 集合属性的结果数量
     * @return 当前查询对象
     */
    public CriteriaQuery sizeLt(String propName, int size) {
        addCriterion(criterions.sizeLt(propName, size));
        return this;
    }

    /**
     * 添加一个“集合属性的结果数量 <= size”的查询条件。例如Order对象有个类型为List<OrderItem>的
     * 集合属性items，我们要查询订购了5种或5种以下物品的订单，则propName为items，size为5.
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @param size 集合属性的结果数量
     * @return 当前查询对象
     */
    public CriteriaQuery sizeLe(String propName, int size) {
        addCriterion(criterions.sizeLe(propName, size));
        return this;
    }

    /**
     * 添加一个“属性包含指定文本”的查询条件
     * @param propName 属性名，该属性必须是字符串类型
     * @param value 文本内容
     * @return 当前查询对象
     */
    public CriteriaQuery containsText(String propName, String value) {
        addCriterion(criterions.containsText(propName, value));
        return this;
    }

    /**
     * 添加一个“属性以指定文本开头”的查询条件
     * @param propName 属性名，该属性必须是字符串类型
     * @param value 文本内容
     * @return 当前查询对象
     */
    public CriteriaQuery startsWithText(String propName, String value) {
        addCriterion(criterions.startsWithText(propName, value));
        return this;
    }

    /**
     * 添加一个“属性值包含在指定的集合内”的查询条件
     * @param propName 属性名
     * @param value 一个集合，符合查询条件的属性值必须包含在该集合内
     * @return 当前查询对象
     */
    public CriteriaQuery in(String propName, Collection<? extends Object> value) {
        addCriterion(criterions.in(propName, value));
        return this;
    }

    /**
     * 添加一个“属性值包含在指定的数组内”的查询条件
     * @param propName 属性名
     * @param value 一个数组，符合查询条件的属性值必须包含在该数组内
     * @return 当前查询对象
     */
    public CriteriaQuery in(String propName, Object[] value) {
        addCriterion(criterions.in(propName, value));
        return this;
    }

    /**
     * 添加一个“属性值不包含在指定的集合内”的查询条件
     * @param propName 属性名
     * @param value 一个集合，符合查询条件的属性值必须不包含在该集合内
     * @return 当前查询对象
     */
    public CriteriaQuery notIn(String propName, Collection<? extends Object> value) {
        addCriterion(criterions.notIn(propName, value));
        return this;
    }

    /**
     * 添加一个“属性值不包含在指定的数组内”的查询条件
     * @param propName 属性名
     * @param value 一个数组，符合查询条件的属性值必须不包含在该数组内
     * @return 当前查询对象
     */
    public CriteriaQuery notIn(String propName, Object[] value) {
        addCriterion(criterions.notIn(propName, value));
        return this;
    }

    /**
     * 添加一个“属性值介于两个值之间（包含左右边界）”的查询条件
     * @param propName 属性名
     * @param from 第一个边界值
     * @param to 第二个边界值
     * @param <E> 被比较的值的类型，也就是属性的兼容类型
     * @return
     */
    public <E> CriteriaQuery between(String propName, Comparable<E> from, Comparable<E> to) {
        addCriterion(criterions.between(propName, from, to));
        return this;
    }

    /**
     * 添加一个“属性值是Null”的查询条件
     * @param propName 属性名
     * @return 当前查询对象
     */
    public CriteriaQuery isNull(String propName) {
        addCriterion(criterions.isNull(propName));
        return this;
    }

    /**
     * 添加一个“属性值不是Null”的查询条件
     * @param propName 属性名
     * @return 当前查询对象
     */
    public CriteriaQuery notNull(String propName) {
        addCriterion(criterions.notNull(propName));
        return this;
    }

    /**
     * 添加一个“集合类型属性值为空集合”的查询条件
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @return 当前查询对象
     */
    public CriteriaQuery isEmpty(String propName) {
        addCriterion(criterions.isEmpty(propName));
        return this;
    }

    /**
     * 添加一个“集合类型属性值不是空集合”的查询条件
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @return 当前查询对象
     */
    public CriteriaQuery notEmpty(String propName) {
        addCriterion(criterions.notEmpty(propName));
        return this;
    }

    /**
     * 添加一个“属性值为true”的查询条件
     * @param propName 属性名
     * @return 当前查询对象
     */
    public CriteriaQuery isTrue(String propName) {
        addCriterion(criterions.isTrue(propName));
        return this;
    }

    /**
     * 添加一个“属性值为false”的查询条件
     * @param propName 属性名
     * @return 当前查询对象
     */
    public CriteriaQuery isFalse(String propName) {
        addCriterion(criterions.isFalse(propName));
        return this;
    }

    /**
     * 添加一个“属性值为空白，即Null或空字符串”的查询条件
     * @param propName 属性名，必须是字符串型属性
     * @return 当前查询对象
     */
    public CriteriaQuery isBlank(String propName) {
        addCriterion(criterions.isBlank(propName));
        return this;
    }

    /**
     * 添加一个“属性值非空”的查询条件
     * @param propName 属性名，必须是字符串型属性
     * @return 当前查询对象
     */
    public CriteriaQuery notBlank(String propName) {
        addCriterion(criterions.notBlank(propName));
        return this;
    }


    /**
     * 添加一个“取反”的查询条件
     * @param criterion 原本的查询条件
     * @return 当前查询对象
     */
    public CriteriaQuery not(QueryCriterion criterion) {
        addCriterion(criterions.not(criterion));
        return this;
    }

    /**
     * 添加一个“与”的查询条件，即同时符合指定的几个查询条件
     * @param queryCriterions 多个基本查询条件
     * @return 当前查询对象
     */
    public CriteriaQuery and(QueryCriterion... queryCriterions) {
        addCriterion(criterions.and(queryCriterions));
        return this;
    }

    /**
     * 添加一个“或”的查询条件，即符合指定的几个查询条件之一
     * @param queryCriterions 多个基本查询条件
     * @return 当前查询对象
     */
    public CriteriaQuery or(QueryCriterion... queryCriterions) {
        addCriterion(criterions.or(queryCriterions));
        return this;
    }

    /**
     * 按指定的属性的升序对结果集排序
     * @param propName 要排序的属性名
     * @return 当前查询对象
     */
    public CriteriaQuery asc(String propName) {
        orderSettings.add(OrderSetting.asc(propName));
        return this;
    }

    /**
     * 按指定的属性的降序对结果集排序
     * @param propName 要排序的属性名
     * @return 当前查询对象
     */
    public CriteriaQuery desc(String propName) {
        orderSettings.add(OrderSetting.desc(propName));
        return this;
    }

    /**
     * 以列表形式返回符合条件和排序规则的查询结果。一般而言，没有调用select()方法的查询应该调用此方法返回列表结果。
     * @return 符合查询结果的类型为字段entityClass的实体集合。
     */
    public <T> List<T> list() {
        return repository.find(this);
    }

    /**
     * 返回单条查询结果。一般而言，没有调用select()方法的查询应该调用此方法返回单个结果。
     * @return 一个符合查询结果的类型为字段entityClass的实体。
     */
    public <T> T singleResult() {
        return repository.getSingleResult(this);
    }

    private void addCriterion(QueryCriterion criterion) {
        queryCriterions.add(criterion);
    }
}
