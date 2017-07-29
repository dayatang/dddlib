package org.dayatang.domain;

import java.util.Collection;

/**
 * 各种查询条件的静态工厂。
 * 将查询条件创建委托给CriterionBuilder实现。
 * 如果在Java类中频繁用到Criteria的多个方法，建议使用静态导入: import static org.dayatang.domain.Criteria.*;
 * Created by yyang on 15/8/10.
 */
public class Criteria {

    private static final CriterionBuilder BUILDER = InstanceFactory.getInstance(CriterionBuilder.class);

    /**
     * <pre>创建一个代表“属性名 = 值”的查询条件</pre>
     * @param propName 属性名
     * @param value 值
     * @return 一个查询条件
     */
    public static QueryCriterion eq(String propName, Object value) {
        return BUILDER.eq(propName, value);
    }

    /**
     * <pre>创建一个代表"属性名 != 值"的查询条件</pre>
     * @param propName 属性名
     * @param value 值
     * @return 一个查询条件
     */
    public static QueryCriterion notEq(String propName, Object value) {
        return BUILDER.notEq(propName, value);
    }

    /**
     * <pre>创建一个代表“属性名 >= 值”的查询条件</pre>
     * @param propName 属性名
     * @param value 值
     * @return 一个查询条件
     */
    public static QueryCriterion ge(String propName, Comparable<?> value) {
        return BUILDER.ge(propName, value);
    }

    /**
     * <pre>创建一个代表“属性名 > 值”的查询条件</pre>
     * @param propName 属性名
     * @param value 值
     * @return 一个查询条件
     */
    public static QueryCriterion gt(String propName, Comparable<?> value) {
        return BUILDER.gt(propName, value);
    }

    /**
     * <pre>创建一个代表“属性名 <= 值”的查询条件</pre>
     * @param propName 属性名
     * @param value 值
     * @return 一个查询条件
     */
    public static QueryCriterion le(String propName, Comparable<?> value) {
        return BUILDER.le(propName, value);
    }

    /**
     * <pre>创建一个代表“属性名 < 值”的查询条件</pre>
     * @param propName 属性名
     * @param value 值
     * @return 一个查询条件
     */
    public static QueryCriterion lt(String propName, Comparable<?> value) {
        return BUILDER.lt(propName, value);
    }

    /**
     * <pre>创建一个代表“属性1 = 属性2”的查询条件</pre>
     * @param propName 属性名
     * @param otherPropName 另一个属性名
     * @return 一个查询条件
     */
    public static QueryCriterion eqProp(String propName, String otherPropName) {
        return BUILDER.eqProp(propName, otherPropName);
    }

    /**
     * <pre>创建一个代表“属性1 != 属性2”的查询条件</pre>
     * @param propName 属性名
     * @param otherPropName 另一个属性名
     * @return 一个查询条件
     */
    public static QueryCriterion notEqProp(String propName, String otherPropName) {
        return BUILDER.notEqProp(propName, otherPropName);
    }

    /**
     * <pre>创建一个代表“属性1 > 属性2”的查询条件</pre>
     * @param propName 属性名
     * @param otherPropName 另一个属性名
     * @return 一个查询条件
     */
    public static QueryCriterion gtProp(String propName, String otherPropName) {
        return BUILDER.gtProp(propName, otherPropName);
    }

    /**
     * <pre>创建一个代表“属性1 >= 属性2”的查询条件</pre>
     * @param propName 属性名
     * @param otherPropName 另一个属性名
     * @return 一个查询条件
     */
    public static QueryCriterion geProp(String propName, String otherPropName) {
        return BUILDER.geProp(propName, otherPropName);
    }

    /**
     * <pre>创建一个代表“属性1 < 属性2”的查询条件</pre>
     * @param propName 属性名
     * @param otherPropName 另一个属性名
     * @return 一个查询条件
     */
    public static QueryCriterion ltProp(String propName, String otherPropName) {
        return BUILDER.ltProp(propName, otherPropName);
    }

    /**
     * <pre>创建一个代表“属性1 <= 属性2”的查询条件</pre>
     * @param propName 属性名
     * @param otherPropName 另一个属性名
     * @return 一个查询条件
     */
    public static QueryCriterion leProp(String propName, String otherPropName) {
        return BUILDER.leProp(propName, otherPropName);
    }

    /**
     * <pre>创建一个代表“集合属性元素数量 = 值”的查询条件（例如：查找子女数量=2的人）</pre>
     * @param propName 集合属性名
     * @param size 集合元素的数量
     * @return 一个查询条件
     */
    public static QueryCriterion sizeEq(String propName, int size) {
        return BUILDER.sizeEq(propName, size);
    }

    /**
     * <pre>创建一个代表“集合属性元素数量 != 值”的查询条件（例如：查找子女数量!=2的人）</pre>
     * @param propName 集合属性名
     * @param size 集合元素的数量
     * @return 一个查询条件
     */
    public static QueryCriterion sizeNotEq(String propName, int size) {
        return BUILDER.sizeNotEq(propName, size);
    }

    /**
     * <pre>创建一个代表“集合属性元素数量 > 值”的查询条件（例如：查找子女数量>2的人）</pre>
     * @param propName 集合属性名
     * @param size 集合元素的数量
     * @return 一个查询条件
     */
    public static QueryCriterion sizeGt(String propName, int size) {
        return BUILDER.sizeGt(propName, size);
    }

    /**
     * <pre>创建一个代表“集合属性元素数量 >= 值”的查询条件（例如：查找子女数量>=2的人）</pre>
     * @param propName 集合属性名
     * @param size 集合元素的数量
     * @return 一个查询条件
     */
    public static QueryCriterion sizeGe(String propName, int size) {
        return BUILDER.sizeGe(propName, size);
    }

    /**
     * <pre>创建一个代表“集合属性元素数量 < 值”的查询条件（例如：查找子女数量<2的人）</pre>
     * @param propName 集合属性名
     * @param size 集合元素的数量
     * @return 一个查询条件
     */
    public static QueryCriterion sizeLt(String propName, int size) {
        return BUILDER.sizeLt(propName, size);
    }

    /**
     * <pre>创建一个代表“集合属性元素数量 <= 值”的查询条件（例如：查找子女数量<=2的人）</pre>
     * @param propName 集合属性名
     * @param size 集合元素的数量
     * @return 一个查询条件
     */
    public static QueryCriterion sizeLe(String propName, int size) {
        return BUILDER.sizeLe(propName, size);
    }

    /**
     * <pre>创建一个代表“文本属性包含某段文字”的查询条件</pre>
     * @param propName 文本属性名
     * @param value 被匹配的文本
     * @return 一个查询条件
     */
    public static QueryCriterion containsText(String propName, String value) {
        return BUILDER.containsText(propName, value);
    }

    /**
     * <pre>创建一个代表“文本属性以某段文字开头”的查询条件</pre>
     * @param propName 文本属性名
     * @param value 被匹配的文本
     * @return 一个查询条件
     */
    public static QueryCriterion startsWithText(String propName, String value) {
        return BUILDER.startsWithText(propName, value);
    }

    /**
     * <pre>创建一个代表“属性值包含在某个集合中”的查询条件</pre>
     * @param propName 属性名
     * @param value 值的集合
     * @return 一个查询条件
     */
    public static QueryCriterion in(String propName, Collection<?> value) {
        return BUILDER.in(propName, value);
    }

    /**
     * <pre>创建一个代表“属性值包含在某个数组中”的查询条件</pre>
     * @param propName 属性名
     * @param value 值的数组
     * @return 一个查询条件
     */
    public static QueryCriterion in(String propName, Object[] value) {
        return BUILDER.in(propName, value);
    }

    /**
     * <pre>创建一个代表“属性值不包含在某个集合中”的查询条件</pre>
     * @param propName 属性名
     * @param value 值的集合
     * @return 一个查询条件
     */
    public static QueryCriterion notIn(String propName, Collection<?> value) {
        return BUILDER.notIn(propName, value);
    }

    /**
     * <pre>创建一个代表“属性值包含在某个数组中”的查询条件</pre>
     * @param propName 属性名
     * @param value 值的数组
     * @return 一个查询条件
     */
    public static QueryCriterion notIn(String propName, Object[] value) {
        return BUILDER.notIn(propName, value);
    }

    /**
     * <pre>创建一个代表“属性值位于某个区间范围”的查询条件。结果包含下限，不包含上限。</pre>
     * @param propName 属性名
     * @param from 区间下限
     * @param to 区间上限
     * @return 一个查询条件
     */
    public static QueryCriterion between(String propName, Comparable<?> from, Comparable<?> to) {
        return BUILDER.between(propName, from, to);
    }

    /**
     * <pre>创建一个代表“属性值为null”的查询条件</pre>
     * @param propName 属性名
     * @return 一个查询条件
     */
    public static QueryCriterion isNull(String propName) {
        return BUILDER.isNull(propName);
    }

    /**
     * <pre>创建一个代表“属性值不为null”的查询条件</pre>
     * @param propName 属性名
     * @return 一个查询条件
     */
    public static QueryCriterion notNull(String propName) {
        return BUILDER.notNull(propName);
    }

    /**
     * <pre>创建一个代表“集合属性值为空集”的查询条件</pre>
     * @param propName 属性名
     * @return 一个查询条件
     */
    public static QueryCriterion isEmpty(String propName) {
        return BUILDER.isEmpty(propName);
    }

    /**
     * <pre>创建一个代表“集合属性值不为空集”的查询条件</pre>
     * @param propName 属性名
     * @return 一个查询条件
     */
    public static QueryCriterion notEmpty(String propName) {
        return BUILDER.notEmpty(propName);
    }

    /**
     * <pre>创建一个代表“属性值为true”的查询条件</pre>
     * @param propName 属性名
     * @return 一个查询条件
     */
    public static QueryCriterion isTrue(String propName) {
        return BUILDER.isTrue(propName);
    }

    /**
     * <pre>创建一个代表“属性值为false”的查询条件</pre>
     * @param propName 属性名
     * @return 一个查询条件
     */
    public static QueryCriterion isFalse(String propName) {
        return BUILDER.isFalse(propName);
    }

    /**
     * <pre>创建一个代表“文本属性值为null或空字符串”的查询条件</pre>
     * @param propName 属性名
     * @return 一个查询条件
     */
    public static QueryCriterion isBlank(String propName) {
        return BUILDER.isBlank(propName);
    }

    /**
     * 创建一个代表“文本属性值不为null和空字符串”的查询条件
     * @param propName 属性名
     * @return 一个查询条件
     */
    public static QueryCriterion notBlank(String propName) {
        return BUILDER.notBlank(propName);
    }

    /**
     * 创建一个代表“对一个查询条件取反”的查询条件
     * @param criterion 查询条件
     * @return 一个查询条件
     */
    public static QueryCriterion not(QueryCriterion criterion) {
        return BUILDER.not(criterion);
    }

    /**
     * 创建一个代表“同时符合多个查询条件”的查询条件
     * @param criteria 一批查询条件
     * @return 一个查询条件
     */
    public static QueryCriterion and(QueryCriterion... criteria) {
        return BUILDER.and(criteria);
    }

    /**
     * 创建一个代表“符合多个查询条件之一”的查询条件
     * @param criteria 一批查询条件
     * @return 一个查询条件
     */
    public static QueryCriterion or(QueryCriterion... criteria) {
        return BUILDER.or(criteria);
    }

    /**
     * 创建一个代表“空查询条件”的查询条件。“空查询条件”对查询不添加任何限制，它的存在只是为了简化代码逻辑。
     * @return 一个空查询条件
     */
    public static QueryCriterion empty() {
        return BUILDER.empty();
    }


}
