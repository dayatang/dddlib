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
import org.dayatang.domain.EntityRepository;
import org.dayatang.domain.NamedQuery;
import org.dayatang.querychannel.service.ChannelQuery;
import org.dayatang.querychannel.service.ChannelQuery;
import org.dayatang.querychannel.service.Page;
import org.dayatang.querychannel.service.Page;
import org.dayatang.utils.Assert;

/**
 * 通道查询的SQL实现
 * @author yyang
 */
public class ChannelNamedQuery extends ChannelQuery<ChannelNamedQuery> {
    
    private String queryName;

    public ChannelNamedQuery(EntityRepository repository, String queryName) {
        super(repository);
        Assert.notBlank(queryName, "Query name must be set!");
        query = new NamedQuery(repository, queryName);
    }

    public NamedQuery getQuery() {
        return (NamedQuery) query;
    }

    @Override
    public <T> List<T> list() {
        return query.list();
    }

    @Override
    public <T> Page<T> pagedList() {
        return new Page<T>(query.getFirstResult(), queryResultCount(), 
                query.getMaxResults(), query.list());
    }

    @Override
    public <T> T singleResult() {
        return (T) query.singleResult();
    }

    @Override
    public long queryResultCount() {
        String queryString = getQueryString();
        if (containGroupByClause(queryString)) {
            List rows = repository.createJpqlQuery(removeOrderByClause(queryString)).setParameters(query.getParameters()).list();
            return rows == null ? 0 : rows.size();
        } else {
            Long result = repository.createJpqlQuery(buildCountQueryString(queryString)).setParameters(query.getParameters()).singleResult();
            return result;
        }
    }

    @Override
    protected String getQueryString() {
        return repository.getQueryStringOfNamedQuery(queryName);
    }

}
