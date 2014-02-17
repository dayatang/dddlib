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

package org.dayatang.querychannel.service.impl;

import java.util.List;
import javax.inject.Inject;
import org.dayatang.domain.EntityRepository;
import org.dayatang.querychannel.service.Page;
import org.dayatang.querychannel.service.QueryChannelService;
import org.springframework.util.Assert;

/**
 *
 * @author yyang
 */
public class QueryChannelServiceImpl implements QueryChannelService {
    
    @Inject
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
    public <T> List<T> list(ChannelJpqlQuery jpqlQuery) {
        return jpqlQuery.list();
    }

    @Override
    public <T> Page<T> pagedList(ChannelJpqlQuery jpqlQuery) {
        return jpqlQuery.pagedList();
    }

    @Override
    public <T> T getSingleResult(ChannelJpqlQuery jpqlQuery) {
        return jpqlQuery.singleResult();
    }

    @Override
    public long getResultCount(ChannelJpqlQuery jpqlQuery) {
        return jpqlQuery.queryResultCount();
    }

    @Override
    public ChannelNamedQuery createNamedQuery(String queryName) {
        return new ChannelNamedQuery(repository, queryName);
    }

    @Override
    public <T> List<T> list(ChannelNamedQuery namedQuery) {
        return namedQuery.list();
    }

    @Override
    public <T> Page<T> pagedList(ChannelNamedQuery namedQuery) {
        return namedQuery.pagedList();
    }

    @Override
    public <T> T getSingleResult(ChannelNamedQuery namedQuery) {
        return namedQuery.singleResult();
    }

    @Override
    public long getResultCount(ChannelNamedQuery namedQuery) {
        return namedQuery.queryResultCount();
    }

    @Override
    public ChannelSqlQuery createSqlQuery(String sql) {
        return new ChannelSqlQuery(repository, sql);
    }

    @Override
    public <T> List<T> list(ChannelSqlQuery sqlQuery) {
        return sqlQuery.list();
    }

    @Override
    public <T> Page<T> pagedList(ChannelSqlQuery sqlQuery) {
        return sqlQuery.pagedList();
    }

    @Override
    public <T> T getSingleResult(ChannelSqlQuery sqlQuery) {
        return sqlQuery.singleResult();
    }

    @Override
    public long getResultCount(ChannelSqlQuery sqlQuery) {
        return sqlQuery.queryResultCount();
    }
    
}
