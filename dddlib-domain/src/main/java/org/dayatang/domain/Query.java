package org.dayatang.domain;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 查询接口。T是查询的根实体的类型。
 * Created with IntelliJ IDEA.
 * User: yyang
 * Date: 13-10-17
 * Time: 下午2:14
 * To change this template use File | Settings | File Templates.
 */
public interface Query<T extends Entity> {

    /**
     * 获得查询根实体的类
     * @return 查询的根实体的类
     */
    Class<T> getEntityClass();

    /**
     * 对于从大结果集中选取部分的查询，获得数据集的起始位置（0代表第一条记录）
     * @return 一个数字，代表从大结果集的第几条记录开始选取子集
     */
    int getFirstResult();

    /**
     * 对于从大结果集中选取部分的查询，设置数据集的起始获取位置（0代表第一条记录）
     * @param firstResult 一个数字，代表从大结果集的第几条记录开始选取子集
     */
    Query<T> setFirstResult(int firstResult);

    /**
     * 对于从大结果集中选取部分的查询，获得本次查询返回的记录的最大数量
     * @return  一个数字，代表从大数据集中最多选取多少条记录
     */
    int getMaxResults();

    /**
     * 对于从大结果集中选取部分的查询，获得本次查询返回的记录的最大数量
     * @param maxResults 一个数字，代表从大数据集中最多选取多少条记录
     */
    Query<T> setMaxResults(int maxResults);

    /**
     * 获得查询条件的集合
     * @return  本次查询指定的条件的集合
     */
    Set<QueryCriterion> getQueryCriterions();

    /**
     * 获得排序选项的列表（先按前面的排序选项排序，如果前面的选项相同，再按后面的选项排序）
     * @return 本次查询指定的排序选项的列表
     */
    List<OrderSetting> getOrderSettings();

    /**
     * 选择要返回的查询结果属性列表。如果props超过一个，查询返回属性数组的列表；
     * 如果props只有一个，查询返回该属性的列表；如果select任何东西，查询返回实体T的列表。
     * @param props 被查询的实体类型的属性的数组，例如Employee的name属性和sn属性
     * @return 该查询本身
     */
    Query<T> select(String... props);

    /**
     * 添加一个“属性名 = 属性值”的查询条件
     * @param propName 属性名
     * @param value 属性值
     * @return 该查询本身
     */
    Query<T> eq(String propName, Object value);

    /**
     * 添加一个“属性名 != 属性值”的查询条件
     * @param propName 属性名
     * @param value 属性值
     * @return 该查询本身
     */
    Query<T> notEq(String propName, Object value);

    /**
     * 添加一个“属性名 > 属性值”的查询条件
     * @param propName 属性名
     * @param value 属性值
     * @return 该查询本身
     */
    Query<T> gt(String propName, Comparable<?> value);

    /**
     * 添加一个“属性名 >= 属性值”的查询条件
     * @param propName 属性名
     * @param value 属性值
     * @return 该查询本身
     */
    Query<T> ge(String propName, Comparable<?> value);

    /**
     * 添加一个“属性名 < 属性值”的查询条件
     * @param propName 属性名
     * @param value 属性值
     * @return 该查询本身
     */
    Query<T> lt(String propName, Comparable<?> value);

    /**
     * 添加一个“属性名 <= 属性值”的查询条件
     * @param propName 属性名
     * @param value 属性值
     * @return 该查询本身
     */
    Query<T> le(String propName, Comparable<?> value);

    /**
     * 添加一个“属性1 = 属性2”的查询条件
     * @param propName 属性名
     * @param otherProp 另一个属性名
     * @return 该查询本身
     */
    Query<T> eqProp(String propName, String otherProp);

    /**
     * 添加一个“属性1 != 属性2”的查询条件
     * @param propName 属性名
     * @param otherProp 另一个属性名
     * @return 该查询本身
     */
    Query<T> notEqProp(String propName, String otherProp);

    /**
     * 添加一个“属性1 > 属性2”的查询条件
     * @param propName 属性名
     * @param otherProp 另一个属性名
     * @return 该查询本身
     */
    Query<T> gtProp(String propName, String otherProp);

    /**
     * 添加一个“属性1 >= 属性2”的查询条件
     * @param propName 属性名
     * @param otherProp 另一个属性名
     * @return 该查询本身
     */
    Query<T> geProp(String propName, String otherProp);

    /**
     * 添加一个“属性1 < 属性2”的查询条件
     * @param propName 属性名
     * @param otherProp 另一个属性名
     * @return 该查询本身
     */
    Query<T> ltProp(String propName, String otherProp);

    /**
     * 添加一个“属性1 <= 属性2”的查询条件
     * @param propName 属性名
     * @param otherProp 另一个属性名
     * @return 该查询本身
     */
    Query<T> leProp(String propName, String otherProp);

    /**
     * 添加一个“集合属性的结果数量 = size”的查询条件。例如Order对象有个类型为List<OrderItem>的
     * 集合属性items，我们要查询订购了5种物品的订单，则propName为items，size为5.
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @param size 集合属性的结果数量
     * @return 该查询本身
     */
    Query<T> sizeEq(String propName, int size);

    /**
     * 添加一个“集合属性的结果数量 != size”的查询条件。例如Order对象有个类型为List<OrderItem>的
     * 集合属性items，我们要查询订购了不是5种物品的订单，则propName为items，size为5.
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @param size 集合属性的结果数量
     * @return 该查询本身
     */
    Query<T> sizeNotEq(String propName, int size);

    /**
     * 添加一个“集合属性的结果数量 > size”的查询条件。例如Order对象有个类型为List<OrderItem>的
     * 集合属性items，我们要查询订购了超过5种物品的订单，则propName为items，size为5.
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @param size 集合属性的结果数量
     * @return 该查询本身
     */
    Query<T> sizeGt(String propName, int size);

    /**
     * 添加一个“集合属性的结果数量 >= size”的查询条件。例如Order对象有个类型为List<OrderItem>的
     * 集合属性items，我们要查询订购了5种或5种以上物品的订单，则propName为items，size为5.
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @param size 集合属性的结果数量
     * @return 该查询本身
     */
    Query<T> sizeGe(String propName, int size);

    /**
     * 添加一个“集合属性的结果数量 < size”的查询条件。例如Order对象有个类型为List<OrderItem>的
     * 集合属性items，我们要查询订购了少于5种物品的订单，则propName为items，size为5.
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @param size 集合属性的结果数量
     * @return 该查询本身
     */
    Query<T> sizeLt(String propName, int size);

    /**
     * 添加一个“集合属性的结果数量 <= size”的查询条件。例如Order对象有个类型为List<OrderItem>的
     * 集合属性items，我们要查询订购了5种或5种以下物品的订单，则propName为items，size为5.
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @param size 集合属性的结果数量
     * @return 该查询本身
     */
    Query<T> sizeLe(String propName, int size);

    /**
     * 添加一个“属性包含指定文本”的查询条件
     * @param propName 属性名，该属性必须是字符串类型
     * @param value 文本内容
     * @return 该查询本身
     */
    Query<T> containsText(String propName, String value);

    /**
     * 添加一个“属性以指定文本开头”的查询条件
     * @param propName 属性名，该属性必须是字符串类型
     * @param value 文本内容
     * @return 该查询本身
     */
    Query<T> startsWithText(String propName, String value);

    /**
     * 添加一个“属性值包含在指定的集合内”的查询条件
     * @param propName 属性名
     * @param value 一个集合，符合查询条件的属性值必须包含在该集合内
     * @return 该查询本身
     */
    Query<T> in(String propName, Collection<? extends Object> value);

    /**
     * 添加一个“属性值包含在指定的数组内”的查询条件
     * @param propName 属性名
     * @param value 一个数组，符合查询条件的属性值必须包含在该数组内
     * @return 该查询本身
     */
    Query<T> in(String propName, Object[] value);

    /**
     * 添加一个“属性值不包含在指定的集合内”的查询条件
     * @param propName 属性名
     * @param value 一个集合，符合查询条件的属性值必须不包含在该集合内
     * @return 该查询本身
     */
    Query<T> notIn(String propName, Collection<? extends Object> value);

    /**
     * 添加一个“属性值不包含在指定的数组内”的查询条件
     * @param propName 属性名
     * @param value 一个数组，符合查询条件的属性值必须不包含在该数组内
     * @return 该查询本身
     */
    Query<T> notIn(String propName, Object[] value);

    /**
     * 添加一个“属性值介于两个值之间（包含左右边界）”的查询条件
     * @param propName 属性名
     * @param from 第一个边界值
     * @param to 第二个边界值
     * @param <E> 被比较的值的类型，也就是属性的兼容类型
     * @return
     */
    <E> Query<T> between(String propName, Comparable<E> from, Comparable<E> to);

    /**
     * 添加一个“属性值是Null”的查询条件
     * @param propName 属性名
     * @return 该查询本身
     */
    Query<T> isNull(String propName);

    /**
     * 添加一个“属性值不是Null”的查询条件
     * @param propName 属性名
     * @return 该查询本身
     */
    Query<T> notNull(String propName);

    /**
     * 添加一个“集合类型属性值为空集合”的查询条件
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @return 该查询本身
     */
    Query<T> isEmpty(String propName);

    /**
     * 添加一个“集合类型属性值不是空集合”的查询条件
     * @param propName 属性名，必须是集合属性（x-to-many或ElementCollection）
     * @return 该查询本身
     */
    Query<T> notEmpty(String propName);

    /**
     * 添加一个“属性值为true”的查询条件
     * @param propName 属性名
     * @return 该查询本身
     */
    public Query<T> isTrue(String propName);

    /**
     * 添加一个“属性值为false”的查询条件
     * @param propName 属性名
     * @return 该查询本身
     */
    public Query<T> isFalse(String propName);

    /**
     * 添加一个“属性值为空白，即Null或空字符串”的查询条件
     * @param propName 属性名，必须是字符串型属性
     * @return 该查询本身
     */
    public Query<T> isBlank(String propName);

    /**
     * 添加一个“属性值非空”的查询条件
     * @param propName 属性名，必须是字符串型属性
     * @return 该查询本身
     */
    public Query<T> notBlank(String propName);




    /**
     * 添加一个“取反”的查询条件
     * @param criterion 原本的查询条件
     * @return 该查询本身
     */
    Query<T> not(QueryCriterion criterion);

    /**
     * 添加一个“与”的查询条件，即同时符合指定的几个查询条件
     * @param criterions 多个基本查询条件
     * @return 该查询本身
     */
    Query<T> and(QueryCriterion... criterions);

    /**
     * 添加一个“或”的查询条件，即符合指定的几个查询条件之一
     * @param criterions 多个基本查询条件
     * @return 该查询本身
     */
    Query<T> or(QueryCriterion... criterions);

    /**
     * 按指定的属性的升序对结果集排序
     * @param propName 要排序的属性名
     * @return 该查询本身
     */
    Query<T> asc(String propName);

    /**
     * 按指定的属性的降序对结果集排序
     * @param propName 要排序的属性名
     * @return 该查询本身
     */
    Query<T> desc(String propName);

    /**
     * 以列表形式返回符合条件和排序规则的查询结果。一般而言，没有调用select()方法的查询应该调用此方法返回列表结果。
     * @return 符合查询结果的类型为字段entityClass的实体集合。
     */
    List<T> list();

    /**
     * 返回单条查询结果。一般而言，没有调用select()方法的查询应该调用此方法返回单个结果。
     * @return 一个符合查询结果的类型为字段entityClass的实体。
     */
    T singleResult();

    /**
     * 以列表形式返回符合条件和排序规则的查询结果。如果返回结果集合元素的类型不是被查询的根实体的类型时该调用此方法。
     * @param resultClass 返回的结果集合元素的类
     * @param <E> 返回的结果集合元素的类型
     * @return 符合查询结果的类型为字段entityClass的实体集合。
     */
    <E> List<E> list(Class<E> resultClass);

    /**
     * 返回单条查询结果.如果返回结果集合元素的类型不是被查询的根实体的类型时该调用此方法。
     * @param resultClass 返回的结果集合元素的类
     * @param <E> 返回的结果集合元素的类型
     * @return 一个符合查询条件的单一结果
     */
    <E> E singleResult(Class<E> resultClass);
}
