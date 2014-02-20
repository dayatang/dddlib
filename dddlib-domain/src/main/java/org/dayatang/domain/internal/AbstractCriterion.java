package org.dayatang.domain.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dayatang.domain.MapParameters;
import org.dayatang.domain.QueryCriterion;

/**
 * 查询条件的抽象基类，实现了AND、OR、NOT操作。
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 */
public abstract class AbstractCriterion implements QueryCriterion {
    
    protected final Map<String, Object> params = new HashMap<String, Object>();
    
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
     *
     * @return
     */
    @Override
    public QueryCriterion not() {
        return new NotCriterion(this);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    /**
     * 从数组中去除为Null的或EmptyCriterion的成员，返回剩余元素的列表
     * @param criterions
     * @return 
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
    
    protected void addParameter(String name, Object value) {
        params.put(name, value);
    }

    protected void addParameters(MapParameters parameters) {
        params.putAll(parameters.getParams());
    }

    @Override
    public MapParameters getParameters() {
        return MapParameters.create(params);
    }

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
