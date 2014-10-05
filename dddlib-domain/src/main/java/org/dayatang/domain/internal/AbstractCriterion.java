package org.dayatang.domain.internal;

import java.util.ArrayList;
import java.util.List;

import org.dayatang.domain.QueryCriterion;

/**
 * 查询条件的抽象基类，实现了AND、OR、NOT操作。
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 */
public abstract class AbstractCriterion implements QueryCriterion {
    
    protected String queryString;

    /**
     * 执行AND操作，返回代表两个QueryCriterion的“与”操作结果的一个新的QueryCriterion
     *
     * @param criterion 另一个QueryCriterion
     * @return 当前对象与criterion的“与”操作的结果
     */
    @Override
    public QueryCriterion and(QueryCriterion criterion) {
        return new AndCriterion(this, criterion);
    }

    /**
     * 执行OR操作，返回代表两个QueryCriterion的“或”操作结果的一个新的QueryCriterion
     *
     * @param criterion 另一个QueryCriterion
     * @return 当前对象与criterion的“或”操作的结果
     */
    @Override
    public QueryCriterion or(QueryCriterion criterion) {
        return new OrCriterion(this, criterion);
    }

    /**
     * 执行NOT操作，返回代表当前对象的“非”操作的一个新的QueryCriterion
     * @return 当前对象的“非”操作的结果
     */
    @Override
    public QueryCriterion not() {
        return new NotCriterion(this);
    }

    /**
     * 判断是否属于“空”条件对象，即EmptyCriterion的实例。主要用于生成查询串时进行判断
     * @return 除了EmptyCriterion子类返回true外，默认返回false。
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * 从数组中去除为Null的或EmptyCriterion的成员，返回剩余元素的列表
     * @param criterions 原始条件数组
     * @return 去除空条件对象后的剩余的成员的列表
     */
    protected List<QueryCriterion> removeNullOrEmptyCriterion(QueryCriterion[] criterions) {
        List<QueryCriterion> results = new ArrayList<QueryCriterion>();
        for (QueryCriterion each : criterions) {
            if (each == null || each.isEmpty()) {
                continue;
            }
            results.add(each);
        }
        return results;
    }

    /**
     * 获得查询条件对应的查询字符串
     * @return 查询字符串
     */
    @Override
    public String toQueryString() {
        return queryString;
    }
    
    
    /**
     * 判断等价性
     * @param other 要用来判等的另一个对象
     * @return 如果当前对象和other等价，则返回true，否则返回false
     */
    @Override
    public abstract boolean equals(final Object other);

    /**
     * 计算哈希值
     * @return 当前对象实例的哈希值
     */
    @Override
    public abstract int hashCode();
}
