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

    /**
     * 创建一个代表“属性名 = 值”的查询条件
     * @param propName 属性名
     * @param value 值
     * @return 一个查询条件
     */
    QueryCriterion eq(String propName, Object value);

    /**
     * 创建一个代表“属性名 != 值”的查询条件
     * @param propName 属性名
     * @param value 值
     * @return 一个查询条件
     */
    QueryCriterion notEq(String propName, Object value);

    /**
     * 创建一个代表“属性名 >= 值”的查询条件
     * @param propName 属性名
     * @param value 值
     * @return 一个查询条件
     */
    QueryCriterion ge(String propName, Comparable<?> value);

    /**
     * 创建一个代表“属性名 > 值”的查询条件
     * @param propName 属性名
     * @param value 值
     * @return 一个查询条件
     */
    QueryCriterion gt(String propName, Comparable<?> value);

    /**
     * 创建一个代表“属性名 <= 值”的查询条件
     * @param propName 属性名
     * @param value 值
     * @return 一个查询条件
     */
    QueryCriterion le(String propName, Comparable<?> value);

    /**
     * 创建一个代表“属性名 < 值”的查询条件
     * @param propName 属性名
     * @param value 值
     * @return 一个查询条件
     */
    QueryCriterion lt(String propName, Comparable<?> value);

    /**
     * 创建一个代表“属性1 = 属性2”的查询条件
     * @param propName 属性名
     * @param otherPropName 另一个属性名
     * @return 一个查询条件
     */
    QueryCriterion eqProp(String propName, String otherPropName);

    /**
     * 创建一个代表“属性1 != 属性2”的查询条件
     * @param propName 属性名
     * @param otherPropName 另一个属性名
     * @return 一个查询条件
     */
    QueryCriterion notEqProp(String propName, String otherPropName);

    /**
     * 创建一个代表“属性1 > 属性2”的查询条件
     * @param propName 属性名
     * @param otherPropName 另一个属性名
     * @return 一个查询条件
     */
    QueryCriterion gtProp(String propName, String otherPropName);

    /**
     * 创建一个代表“属性1 >= 属性2”的查询条件
     * @param propName 属性名
     * @param otherPropName 另一个属性名
     * @return 一个查询条件
     */
    QueryCriterion geProp(String propName, String otherPropName);

    /**
     * 创建一个代表“属性1 < 属性2”的查询条件
     * @param propName 属性名
     * @param otherPropName 另一个属性名
     * @return 一个查询条件
     */
    QueryCriterion ltProp(String propName, String otherPropName);

    /**
     * 创建一个代表“属性1 <= 属性2”的查询条件
     * @param propName 属性名
     * @param otherPropName 另一个属性名
     * @return 一个查询条件
     */
    QueryCriterion leProp(String propName, String otherPropName);

    /**
     * 创建一个代表“集合属性元素数量 = 值”的查询条件（例如：查找子女数量=2的人）
     * @param propName 集合属性名
     * @param size 集合元素的数量
     * @return 一个查询条件
     */
    QueryCriterion sizeEq(String propName, int size);

    /**
     * 创建一个代表“集合属性元素数量 != 值”的查询条件（例如：查找子女数量!=2的人）
     * @param propName 集合属性名
     * @param size 集合元素的数量
     * @return 一个查询条件
     */
    QueryCriterion sizeNotEq(String propName, int size);

    /**
     * 创建一个代表“集合属性元素数量 > 值”的查询条件（例如：查找子女数量>2的人）
     * @param propName 集合属性名
     * @param size 集合元素的数量
     * @return 一个查询条件
     */
    QueryCriterion sizeGt(String propName, int size);

    /**
     * 创建一个代表“集合属性元素数量 >= 值”的查询条件（例如：查找子女数量>=2的人）
     * @param propName 集合属性名
     * @param size 集合元素的数量
     * @return 一个查询条件
     */
    QueryCriterion sizeGe(String propName, int size);

    /**
     * 创建一个代表“集合属性元素数量 < 值”的查询条件（例如：查找子女数量<2的人）
     * @param propName 集合属性名
     * @param size 集合元素的数量
     * @return 一个查询条件
     */
    QueryCriterion sizeLt(String propName, int size);

    /**
     * 创建一个代表“集合属性元素数量 <= 值”的查询条件（例如：查找子女数量<=2的人）
     * @param propName 集合属性名
     * @param size 集合元素的数量
     * @return 一个查询条件
     */
    QueryCriterion sizeLe(String propName, int size);

    /**
     * 创建一个代表“文本属性包含某段文字”的查询条件
     * @param propName 文本属性名
     * @param value 被匹配的文本
     * @return 一个查询条件
     */
    QueryCriterion containsText(String propName, String value);

    /**
     * 创建一个代表“文本属性以某段文字开头”的查询条件
     * @param propName 文本属性名
     * @param value 被匹配的文本
     * @return 一个查询条件
     */
    QueryCriterion startsWithText(String propName, String value);

    /**
     * 创建一个代表“属性值包含在某个集合中”的查询条件
     * @param propName 属性名
     * @param value 值的集合
     * @return 一个查询条件
     */
    QueryCriterion in(String propName, Collection<?> value);

    /**
     * 创建一个代表“属性值包含在某个数组中”的查询条件
     * @param propName 属性名
     * @param value 值的数组
     * @return 一个查询条件
     */
    QueryCriterion in(String propName, Object[] value);

    /**
     * 创建一个代表“属性值不包含在某个集合中”的查询条件
     * @param propName 属性名
     * @param value 值的集合
     * @return 一个查询条件
     */
    QueryCriterion notIn(String propName, Collection<?> value);

    /**
     * 创建一个代表“属性值包含在某个数组中”的查询条件
     * @param propName 属性名
     * @param value 值的数组
     * @return 一个查询条件
     */
    QueryCriterion notIn(String propName, Object[] value);

    /**
     * 创建一个代表“属性值位于某个区间范围”的查询条件。结果包含下限，不包含上限。
     * @param propName 属性名
     * @param from 区间下限
     * @param to 区间上限
     * @return 一个查询条件
     */
    QueryCriterion between(String propName, Comparable<?> from, Comparable<?> to);

    /**
     * 创建一个代表“属性值为null”的查询条件
     * @param propName 属性名
     * @return 一个查询条件
     */
    QueryCriterion isNull(String propName);

    /**
     * 创建一个代表“属性值不为null”的查询条件
     * @param propName 属性名
     * @return 一个查询条件
     */
    QueryCriterion notNull(String propName);

    /**
     * 创建一个代表“集合属性值为空集”的查询条件
     * @param propName 属性名
     * @return 一个查询条件
     */
    QueryCriterion isEmpty(String propName);

    /**
     * 创建一个代表“集合属性值不为空集”的查询条件
     * @param propName 属性名
     * @return 一个查询条件
     */
    QueryCriterion notEmpty(String propName);

    /**
     * 创建一个代表“属性值为true”的查询条件
     * @param propName 属性名
     * @return 一个查询条件
     */
    QueryCriterion isTrue(String propName);

    /**
     * 创建一个代表“属性值为false”的查询条件
     * @param propName 属性名
     * @return 一个查询条件
     */
    QueryCriterion isFalse(String propName);

    /**
     * 创建一个代表“文本属性值为null或空字符串”的查询条件
     * @param propName 属性名
     * @return 一个查询条件
     */
    QueryCriterion isBlank(String propName);

    /**
     * 创建一个代表“文本属性值不为null和空字符串”的查询条件
     * @param propName 属性名
     * @return 一个查询条件
     */
    QueryCriterion notBlank(String propName);

    /**
     * 创建一个代表“对一个查询条件取反”的查询条件
     * @param criterion 查询条件
     * @return 一个查询条件
     */
    QueryCriterion not(QueryCriterion criterion);

    /**
     * 创建一个代表“同时符合多个查询条件”的查询条件
     * @param criteria 一批查询条件
     * @return 一个查询条件
     */
    QueryCriterion and(QueryCriterion... criteria);

    /**
     * 创建一个代表“符合多个查询条件之一”的查询条件
     * @param criteria 一批查询条件
     * @return 一个查询条件
     */
    QueryCriterion or(QueryCriterion... criteria);

    /**
     * 创建一个代表“空查询条件”的查询条件。“空查询条件”对查询不添加任何限制，它的存在只是为了简化代码逻辑。
     * @return 一个空查询条件
     */
	QueryCriterion empty();

}
