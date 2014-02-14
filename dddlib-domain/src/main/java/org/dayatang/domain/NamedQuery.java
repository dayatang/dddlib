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

/**
 * 基于命名查询的查询对象。DDDLib支持的四种查询形式之一。
 * 可以指定定位查询参数或命名查询参数，也可以针对查询结果取子集。
 * @author yyang
 */
public class NamedQuery extends BaseQuery<NamedQuery> {

    private final String queryName;

    /**
     * 使用仓储和命名查询名字创建命名查询
     * @param repository 仓储
     * @param queryName 命名查询的名称
     */
    public NamedQuery(EntityRepository repository, String queryName) {
        super(repository);
        Assert.notBlank(queryName);
        this.queryName = queryName;
    }

    /**
     * 获取命名查询的名称
     * @return 命名查询名称
     */
    public String getQueryName() {
        return queryName;
    }

    /**
     * 返回查询结果列表。
     * @param <T> 查询结果的列表元素类型
     * @return 查询结果。
     */
    @Override
    public <T> List<T> list() {
        return getRepository().find(this);
    }

    /**
     * 返回单条查询结果。
     * @param <T> 查询结果的类型
     * @return 查询结果。
     */
    @Override
    public <T> T singleResult() {
        return getRepository().getSingleResult(this);
    }

    /**
     * 执行更新仓储的操作。
     * @return 被更新或删除的实体的数量
     */
    @Override
    public int executeUpdate() {
        return getRepository().executeUpdate(this);
    }

}
