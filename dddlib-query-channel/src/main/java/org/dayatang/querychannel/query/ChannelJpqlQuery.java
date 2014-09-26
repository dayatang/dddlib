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
import org.dayatang.domain.EntityRepository;
import org.dayatang.domain.JpqlQuery;
import org.dayatang.utils.Page;
import org.dayatang.utils.Assert;

/**
 * 通道查询的JPQL实现
 * @author yyang
 */
public class ChannelJpqlQuery extends ChannelQuery<ChannelJpqlQuery> {
    
    private final JpqlQuery query;

    public ChannelJpqlQuery(EntityRepository repository, String jpql) {
        super(repository);
        Assert.notBlank(jpql, "JPQL must be set!");
        query = new JpqlQuery(repository, jpql);
        setQuery(query);
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
        return query.getJpql();
    }

    @Override
    protected BaseQuery createBaseQuery(String queryString) {
        return repository.createJpqlQuery(queryString);
    }

}
