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
package org.dayatang.querychannel.query;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.dayatang.domain.BaseQuery;
import org.dayatang.domain.EntityRepository;
import org.dayatang.domain.QueryParameters;
import org.dayatang.querychannel.service.Page;
import org.dayatang.utils.Assert;

/**
 * 查询通道查询
 *
 * @author yyang
 * @param <E> 查询类型
 */
public abstract class ChannelQuery<E extends ChannelQuery> {

    protected EntityRepository repository;
    protected BaseQuery query;
    private int pageIndex;
    //private int pageSize = Page.DEFAULT_PAGE_SIZE;

    public ChannelQuery(EntityRepository repository) {
        this.repository = repository;
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
     * 设置当前页码。0为第一页
     *
     * @param pageIndex 要设置的页码
     * @return 该对象本身
     */
    public E setPageIndex(int pageIndex) {
        Assert.isTrue(pageIndex >= 0, "Page index must be greater than 0!");
        this.pageIndex = pageIndex;
        int pageSize = query.getMaxResults() == 0 ? Page.DEFAULT_PAGE_SIZE : query.getMaxResults();
        query.setFirstResult(Page.getStartOfPage(pageIndex, pageSize));
        return (E) this;
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
        query.setFirstResult(Page.getStartOfPage(pageIndex, pageSize));
        return (E) this;
    }

    /**
     * 返回查询结果数据页。
     *
     * @param <T> 查询结果的列表元素类型
     * @return 查询结果。
     */
    public abstract <T> List<T> list();

    /**
     * 返回查询结果数据页。
     *
     * @param <T> 查询结果的列表元素类型
     * @return 查询结果。
     */
    public abstract <T> Page<T> pagedList();

    /*
     * 返回查询结果数据页。
     *
     * @return 查询结果。
     *
     public abstract Page<Map<String, Object>> listAsMap();
     */
     
    /**
     * 返回单条查询结果。
     *
     * @param <T> 查询结果的类型
     * @return 查询结果。
     */
    public abstract <T> T singleResult();

    /**
     * 获取符合查询条件的记录总数
     *
     * @return 符合查询条件的记录总数
     */
    public abstract long queryResultCount();

    /**
     * 构造一个查询数据条数的语句,不能用于union
     *
     * @param queryString 源语句
     * @return 查询数据条数的语句
     */
    protected String buildCountQueryString(String queryString) {
        String result = removeOrderByClause(queryString);

        int index = StringUtils.indexOfIgnoreCase(result, " from ");

        StringBuilder builder = new StringBuilder("select count(" + stringInCount(result, index) + ") ");

        if (index != -1) {
            builder.append(result.substring(index));
        } else {
            builder.append(result);
        }
        return builder.toString();
    }

    /**
     * 去除查询语句的orderby 子句
     *
     * @param queryString
     * @return
     */
    protected String removeOrderByClause(String queryString) {
        Matcher m = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE).matcher(queryString);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static String stringInCount(String queryString, int fromIndex) {
        int distinctIndex = getPositionOfDistinct(queryString);
        if (distinctIndex == -1) {
            return "*";
        }
        String distinctToFrom = queryString.substring(distinctIndex, fromIndex);

        // 除去“,”之后的语句
        int commaIndex = distinctToFrom.indexOf(",");
        String strMayBeWithAs = commaIndex == -1 ? distinctToFrom : distinctToFrom.substring(0, commaIndex);

        // 除去as语句
        int asIndex = StringUtils.indexOfIgnoreCase(strMayBeWithAs, " as ");
        String strInCount = asIndex == -1 ? strMayBeWithAs : strMayBeWithAs.substring(0, asIndex);

        // 除去()，因为HQL不支持 select count(distinct (...))，但支持select count(distinct ...)
        return strInCount.replace("(", " ").replace(")", " ");
    }

    private static int getPositionOfDistinct(String queryString) {
        int result = StringUtils.indexOfIgnoreCase(queryString, "distinct(");
        return result == -1 ? StringUtils.indexOfIgnoreCase(queryString, "distinct (") : result;
    }

    protected boolean containGroupByClause(String queryString) {
        return StringUtils.containsIgnoreCase(queryString, " group by ");
    }

    protected abstract String getQueryString();
    
}
