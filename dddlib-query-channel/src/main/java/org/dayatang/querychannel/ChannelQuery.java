/*
 * Copyright 2014 Dayatang Open Source..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dayatang.querychannel;

import java.util.List;
import java.util.Map;

import org.dayatang.domain.BaseQuery;
import org.dayatang.domain.EntityRepository;
import org.dayatang.domain.QueryParameters;
import org.dayatang.utils.Assert;
import org.dayatang.utils.Page;

/**
 * 查询通道查询
 *
 * @author yyang
 * @param <E> 查询类型
 */
public abstract class ChannelQuery<E extends ChannelQuery> {

    protected EntityRepository repository;
    private BaseQuery query;
    private int pageIndex;
    //private int pageSize = Page.DEFAULT_PAGE_SIZE;


    public ChannelQuery(EntityRepository repository) {
        this.repository = repository;
    }

    public void setQuery(BaseQuery query) {
        this.query = query;
    }

    /**
     * 获取查询参数
     *
     * @return 查询参数
     */
    public QueryParameters getParameters() {
        return query.getParameters();
    }

    /**
     * 设置定位命名参数（数组方式）
     *
     * @param parameters 要设置的参数
     * @return 该对象本身
     */
    public E setParameters(Object... parameters) {
        query.setParameters(parameters);
        return (E) this;
    }

    /**
     * 设置定位参数（列表方式）
     *
     * @param parameters 要设置的参数
     * @return 该对象本身
     */
    public E setParameters(List<Object> parameters) {
        query.setParameters(parameters);
        return (E) this;
    }

    /**
     * 设置命名参数（Map形式，Key是参数名称，Value是参数值）
     *
     * @param parameters 要设置的参数
     * @return 该对象本身
     */
    public E setParameters(Map<String, Object> parameters) {
        query.setParameters(parameters);
        return (E) this;
    }

    /**
     * 添加一个命名参数，Key是参数名称，Value是参数值。
     *
     * @param key 命名参数名称
     * @param value 参数值
     * @return 该对象本身
     */
    public E addParameter(String key, Object value) {
        query.addParameter(key, value);
        return (E) this;
    }

    /**
     * 针对分页查询，获取firstResult。 firstResult代表从满足查询条件的记录的第firstResult + 1条开始获取数据子集。
     *
     * @return firstResult的设置值，
     */
    public int getFirstResult() {
        return query.getFirstResult();
    }

    /**
     * 针对分页查询，设置firstResult。 firstResult代表从满足查询条件的记录的第firstResult + 1条开始获取数据子集。
     *
     * @param firstResult 要设置的firstResult值。
     * @return 该对象本身
     */
    public E setFirstResult(int firstResult) {
        Assert.isTrue(firstResult >= 0, "First result must be greater than 0!");
        query.setFirstResult(firstResult);
        return (E) this;
    }

    /**
     * 获取当前页码（0为第一页）
     *
     * @return 当前页码
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * 获取每页记录数
     *
     * @return 每页记录数
     */
    public int getPageSize() {
        return query.getMaxResults();
    }

    /**
     * 设置每页记录数
     *
     * @param pageSize 每页记录数
     * @return 该对象本身
     */
    public E setPageSize(int pageSize) {
        Assert.isTrue(pageSize > 0, "Page size must be greater than 0!");
        query.setMaxResults(pageSize);
        return (E) this;
    }

    /**
     * 设置分页信息
     *
     * @param pageIndex 要设置的页码
     * @param pageSize 要设置的页大小
     * @return 该对象本身
     */
    public E setPage(int pageIndex, int pageSize) {
        Assert.isTrue(pageIndex >= 0, "Page index must be greater than or equals to 0!");
        Assert.isTrue(pageSize > 0, "Page index must be greater than 0!");
        this.pageIndex = pageIndex;
        query.setMaxResults(pageSize);
        query.setFirstResult(Page.getStartOfPage(pageIndex, pageSize));
        return (E) this;
    }

    /**
     * 返回查询结果数据页。
     *
     * @param <T> 查询结果的列表元素类型
     * @return 查询结果。
     */
    public <T> List<T> list() {
        return query.list();
    }

    /**
     * 返回查询结果数据页。
     *
     * @param <T> 查询结果的列表元素类型
     * @return 查询结果。
     */
    public <T> Page<T> pagedList() {
        return new Page<T>(query.getFirstResult(), queryResultCount(), 
                query.getMaxResults(), query.list());
    }
     
    /**
     * 返回单条查询结果。
     *
     * @param <T> 查询结果的类型
     * @return 查询结果。
     */
    public <T> T singleResult() {
        return (T) query.singleResult();
    }

    /**
     * 获取符合查询条件的记录总数
     *
     * @return 符合查询条件的记录总数
     */
    public long queryResultCount() {
        CountQueryStringBuilder builder = new CountQueryStringBuilder(getQueryString());
        if (builder.containsGroupByClause()) {
            List rows = createBaseQuery(builder.removeOrderByClause())
                    .setParameters(query.getParameters()).list();
            return rows == null ? 0 : rows.size();
        } else {
            Number result = (Number) createBaseQuery(builder.buildQueryStringOfCount())
                    .setParameters(query.getParameters()).singleResult();
            return result.longValue();
        }
    }

    /**
     * 获得当前查询对应的查询字符串
     * @return 当前查询对应的查询字符串
     */
    protected abstract String getQueryString();

    protected abstract BaseQuery createBaseQuery(String queryString);
}
