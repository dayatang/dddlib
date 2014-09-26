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

package org.dayatang.querychannel.impl;

import org.dayatang.querychannel.query.ChannelNamedQuery;
import org.dayatang.querychannel.query.ChannelSqlQuery;
import org.dayatang.querychannel.query.ChannelJpqlQuery;
import java.util.List;

import org.dayatang.domain.EntityRepository;
import org.dayatang.querychannel.ChannelQuery;
import org.dayatang.utils.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.springframework.util.Assert;

/**
 *
 * @author yyang
 */
public class QueryChannelServiceImpl implements QueryChannelService {
    
    private EntityRepository repository;

    public QueryChannelServiceImpl(EntityRepository repository) {
        Assert.notNull(repository, "Repository must set!");
        this.repository = repository;
    }

    @Override
    public ChannelJpqlQuery createJpqlQuery(String jpql) {
        return new ChannelJpqlQuery(repository, jpql);
    }

    @Override
    public ChannelNamedQuery createNamedQuery(String queryName) {
        return new ChannelNamedQuery(repository, queryName);
    }

    @Override
    public ChannelSqlQuery createSqlQuery(String sql) {
        return new ChannelSqlQuery(repository, sql);
    }

    @Override
    public <T> List<T> list(ChannelQuery query) {
        return query.list();
    }

    @Override
    public <T> Page<T> pagedList(ChannelQuery query) {
        return query.pagedList();
    }

    @Override
    public <T> T getSingleResult(ChannelQuery query) {
        return (T) query.singleResult();
    }

    @Override
    public long getResultCount(ChannelQuery query) {
        return query.queryResultCount();
    }
    
}
