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
package org.dayatang.querychannel.service;

import java.util.List;
import java.util.Map;
import org.dayatang.domain.BaseQuery;
import org.dayatang.domain.QueryParameters;
import org.dayatang.querychannel.support.Page;
import org.dayatang.utils.Assert;

/**
 * 查询通道查询
 *
 * @author yyang
 * @param <E> 查询类型
 */
public abstract class ChannelQuery<E extends ChannelQuery> {

    protected BaseQuery query;
    private int pageIndex;
    private int pageSize;

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
        Assert.isTrue(firstResult >= 0);
        query.setFirstResult(firstResult);
        return (E) this;
    }

    /**
     * 针对分页查询，获取maxResults设置值。 maxResults代表从满足查询条件的结果中最多获取的数据记录的数量。
     *
     * @return maxResults的设置值。
     */
    public int getMaxResults() {
        return query.getMaxResults();
    }

    /**
     * 针对分页查询，设置maxResults的值。 maxResults代表从满足查询条件的结果中最多获取的数据记录的数量。
     *
     * @param maxResults 要设置的maxResults值
     * @return 该对象本身
     */
    public E setMaxResults(int maxResults) {
        query.setMaxResults(maxResults);
        return (E) this;
    }

    /**
     * 获取当前页码（0为第一页）
     * @return 当前页码
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * 设置当前页码
     * @param pageIndex 要设置的页码
     * @return 该对象本身
     */
    public E setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        return (E) this;
    }

    /**
     * 获取每页记录数
     * @return 每页记录数
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页记录数
     * @param pageSize 每页记录数
     * @return 该对象本身
     */
    public E setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return (E) this;
    }

    /**
     * 返回查询结果数据页。
     * @param <T> 查询结果的列表元素类型
     * @return 查询结果。
     */
    public abstract <T> Page<T> list();

    /**
     * 返回查询结果数据页。
     * @return 查询结果。
     */
    public abstract Page<Map<String, Object>> listAsMap();

    /**
     * 返回单条查询结果。
     * @param <T> 查询结果的类型
     * @return 查询结果。
     */
    public abstract <T> T singleResult();
    
    /**
     * 获取符合查询条件的记录总数
     * @return 符合查询条件的记录总数
     */
    public abstract long queryResultSize();

}
