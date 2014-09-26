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

import org.dayatang.domain.BaseQuery;
import org.dayatang.querychannel.ChannelQuery;
import java.util.List;
import org.dayatang.domain.Entity;
import org.dayatang.domain.EntityRepository;
import org.dayatang.domain.SqlQuery;
import org.dayatang.utils.Page;
import org.dayatang.utils.Assert;

/**
 * 通道查询的SQL实现
 * @author yyang
 */
public class ChannelSqlQuery extends ChannelQuery<ChannelSqlQuery> {
    
    private final SqlQuery query;

    public ChannelSqlQuery(EntityRepository repository, String sql) {
        super(repository);
        Assert.notBlank(sql, "SQL must be set!");
        query = new SqlQuery(repository, sql);
        setQuery(query);
    }

    /**
     * 设置查询的结果实体类型。注意setResultEntityClass()和addScalar()是互斥的，
     * 分别适用于查询结果是实体和标量两种情形
     * @param resultEntityClass 要设置的查询结果类型
     * @return 该对象本身
     */
    public ChannelSqlQuery setResultEntityClass(Class<? extends Entity> resultEntityClass) {
        query.setResultEntityClass(resultEntityClass);
        return this;
    }

    @Override
    public <T> List<T> list() {
        return query.list();
    }

    @Override
    public <T> Page<T> pagedList() {
        return new Page<T>(query.getFirstResult(), queryResultCount(), 
                query.getMaxResults(), (List<T>) query.list());
    }

    @Override
    public <T> T singleResult() {
        return (T) query.singleResult();
    }

    @Override
    protected String getQueryString() {
        return query.getSql();
    }

    @Override
    protected BaseQuery createBaseQuery(String queryString) {
        return repository.createSqlQuery(queryString);
    }


}
