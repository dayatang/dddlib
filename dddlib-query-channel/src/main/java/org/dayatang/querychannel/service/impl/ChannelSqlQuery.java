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
import java.util.Map;
import org.dayatang.domain.EntityRepository;
import org.dayatang.domain.SqlQuery;
import org.dayatang.querychannel.service.ChannelQuery;
import org.dayatang.querychannel.support.Page;

/**
 * 通道查询的SQL实现
 * @author yyang
 */
public class ChannelSqlQuery extends ChannelQuery<ChannelSqlQuery> {

    public ChannelSqlQuery(EntityRepository repository, String sql) {
        super(repository);
        query = new SqlQuery(repository, sql);
    }

    public SqlQuery getQuery() {
        return (SqlQuery) query;
    }

    @Override
    public <T> Page<T> listAsPage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T singleResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long queryResultCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> List<T> list() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getQueryString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
