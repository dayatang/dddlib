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

import java.util.HashMap;
import org.dayatang.utils.Assert;

import java.util.List;
import java.util.Map;

/**
 * 基于原生SQL的查询。DDDLib支持的四种查询形式之一。
 * 可以指定定位查询参数或命名查询参数，也可以针对查询结果取子集。
 * @author yyang
 */
public class SqlQuery {

    private final EntityRepository repository;
    private final String sql;
    private QueryParameters parameters;
    private final MapParameters mapParameters = MapParameters.create();
    private int firstResult;
    private int maxResults;
    private Class<? extends Entity> resultEntityClass;

    /**
     * 使用仓储和SQL语句创建SQL查询。
     * @param repository 仓储
     * @param sql SQL查询语句
     */
    public SqlQuery(EntityRepository repository, String sql) {
        Assert.notNull(repository);
        Assert.notBlank(sql);
        this.repository = repository;
        this.sql = sql;
    }

    /**
     * 获取SQL查询语句
     * @return SQL查询语句
     */
    public String getSql() {
        return sql;
    }

    /**
     * 获取查询参数
     * @return 查询参数
     */
    public QueryParameters getParameters() {
        return parameters;
    }

    /**
     * 设置定位命名参数（数组方式）
     * @param parameters 要设置的参数
     * @return 该对象本身
     */
    public SqlQuery setParameters(Object... parameters) {
        this.parameters = ArrayParameters.create(parameters);
        return this;
    }

    /**
     * 设置定位参数（列表方式）
     * @param parameters 要设置的参数
     * @return 该对象本身
     */
    public SqlQuery setParameters(List<Object> parameters) {
        this.parameters = ArrayParameters.create(parameters);
        return this;
    }

    /**
     * 设置命名参数（Map形式，Key是参数名称，Value是参数值）
     * @param parameters 要设置的参数
     * @return 该对象本身
     */
    public SqlQuery setParameters(Map<String, Object> parameters) {
        this.parameters = MapParameters.create(parameters);
        return this;
    }

    /**
     * 添加一个命名参数，Key是参数名称，Value是参数值。
     * @param key 命名参数名称
     * @param value 参数值
     * @return 该对象本身
     */
    public SqlQuery addParameter(String key, Object value) {
        mapParameters.add(key, value);
        this.parameters = mapParameters;
        return this;
    }

    /**
     * 针对分页查询，获取firstResult。
     * firstResult代表从满足查询条件的记录的第firstResult + 1条开始获取数据子集。
     * @return firstResult的设置值，
     */
    public int getFirstResult() {
        return firstResult;
    }

    /**
     * 针对分页查询，设置firstResult。
     * firstResult代表从满足查询条件的记录的第firstResult + 1条开始获取数据子集。
     * @param firstResult 要设置的firstResult值。
     * @return 该对象本身
     */
    public SqlQuery setFirstResult(int firstResult) {
        Assert.isTrue(firstResult >= 0);
        this.firstResult = firstResult;
        return this;
    }

    /**
     * 针对分页查询，获取maxResults设置值。
     * maxResults代表从满足查询条件的结果中最多获取的数据记录的数量。
     * @return maxResults的设置值。
     */
    public int getMaxResults() {
        return maxResults;
    }

    /**
     * 针对分页查询，设置maxResults的值。
     * maxResults代表从满足查询条件的结果中最多获取的数据记录的数量。
     * @param maxResults 要设置的maxResults值
     * @return 该对象本身
     */
    public SqlQuery setMaxResults(int maxResults) {
        Assert.isTrue(maxResults > 0);
        this.maxResults = maxResults;
        return this;
    }

    /**
     * 返回查询结果实体类型。适用于返回结果是实体或实体列表的情形。
     * @return 查询结果的实体类型（如果结果是个集合，就是集合元素的类型）
     */
    public Class<? extends Entity> getResultEntityClass() {
        return resultEntityClass;
    }

    /**
     * 设置查询的结果实体类型。注意setResultEntityClass()和addScalar()是互斥的，
     * 分别适用于查询结果是实体和标量两种情形
     * @param resultEntityClass 要设置的查询结果类型
     * @return 该对象本身
     */
    public SqlQuery setResultEntityClass(Class<? extends Entity> resultEntityClass) {
        this.resultEntityClass = resultEntityClass;
        return this;
    }
    
            

    /**
     * 以列表形式返回符合条件和排序规则的查询结果。一般而言，没有调用select()方法的查询应该调用此方法返回列表结果。
     * @return 符合查询结果的类型为字段resultEntityClass的实体集合。
     */
    public <T> List<T> list() {
        return repository.find(this);
    }

    /**
     * 返回单条查询结果。一般而言，没有调用select()方法的查询应该调用此方法返回单个结果。
     * @return 一个符合查询结果的类型为字段resultEntityClass的实体。
     */
    public <T> T singleResult() {
        return repository.getSingleResult(this);
    }

    /**
     * 执行更新仓储的操作。
     * @return 被更新或删除的实体的数量
     */
    public int executeUpdate() {
        return repository.executeUpdate(this);
    }
}
