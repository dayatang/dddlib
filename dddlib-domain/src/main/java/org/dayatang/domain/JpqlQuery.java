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
package org.dayatang.domain;

import org.dayatang.utils.Assert;

import java.util.List;
import java.util.Map;

/**
 * 基于对象查询语言字符串的查询。可以指定定位查询参数或命名查询参数，也可以针对查询结果取子集。
 * @author yyang
 */
public class JpqlQuery {

    private EntityRepository repository;
    private final String jpql;
    private QueryParameters parameters;
    private MapParameters mapParameters = MapParameters.create();
    private int firstResult;
    private int maxResults;

    public JpqlQuery(EntityRepository repository, String jpql) {
        Assert.notNull(repository);
        Assert.notBlank(jpql);
        this.repository = repository;
        this.jpql = jpql;
    }

    public String getJpql() {
        return jpql;
    }

    public QueryParameters getParameters() {
        return parameters;
    }

    public JpqlQuery setParameters(QueryParameters parameters) {
        this.parameters = parameters;
        return this;
    }

    public JpqlQuery setParameters(Object... parameters) {
        this.parameters = ArrayParameters.create(parameters);
        return this;
    }

    public JpqlQuery setParameters(List<Object> parameters) {
        this.parameters = ArrayParameters.create(parameters);
        return this;
    }

    public JpqlQuery setParameters(Map<String, Object> parameters) {
        this.parameters = MapParameters.create(parameters);
        return this;
    }

    public JpqlQuery addParameter(String key, Object value) {
        mapParameters.add(key, value);
        this.parameters = mapParameters;
        return this;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public JpqlQuery setFirstResult(int firstResult) {
        Assert.isTrue(firstResult >= 0);
        this.firstResult = firstResult;
        return this;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public JpqlQuery setMaxResults(int maxResults) {
        Assert.isTrue(maxResults > 0);
        this.maxResults = maxResults;
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

    /**
     * 执行更新仓储的操作。
     */
    public int executeUpdate() {
        return repository.executeUpdate(this);
    }
}
