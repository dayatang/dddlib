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

/**
 *
 * @author yyang
 */
public class JpqlQuery {

    private final String jpql;
    private QueryParameters params;
    private int firstResult;
    private int maxResults;

    public JpqlQuery(String jpql) {
        Assert.notBlank(jpql);
        this.jpql = jpql;
    }

    public String getJpql() {
        return jpql;
    }

    public QueryParameters getParams() {
        return params;
    }

    public JpqlQuery setParams(QueryParameters params) {
        this.params = params;
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

}
