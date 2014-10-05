package org.dayatang.domain;


/**
 * 查询条件接口
 *
 * @author yyang
 */
public interface QueryCriterion {

    static final String ROOT_ALIAS = "rootEntity";

    /**
     * 执行AND操作，返回代表两个QueryCriterion的“与”操作结果的一个新的QueryCriterion
     *
     * @param criterion 另一个QueryCriterion
     * @return 当前对象与criterion的“与”操作的结果
     */
    QueryCriterion and(QueryCriterion criterion);

    /**
     * 执行OR操作，返回代表两个QueryCriterion的“或”操作结果的一个新的QueryCriterion
     *
     * @param criterion 另一个QueryCriterion
     * @return 当前对象与criterion的“或”操作的结果
     */
    QueryCriterion or(QueryCriterion criterion);

    /**
     * 执行NOT操作，返回代表当前对象的“非”操作的一个新的QueryCriterion
     * @return 当前对象的“非”操作的结果
     */
    QueryCriterion not();

    /**
     * 是否空条件，即EmptyCriterion的实例
     * @return 如果是空条件就返回true，否则返回false
     */
    boolean isEmpty();

    /**
     * 转换成JPQL字符串
     * @return 查询字符串
     */
    String toQueryString();

    /**
     * 获得查询参数
     * @return 查询的参数集
     */
    NamedParameters getParameters();


}
