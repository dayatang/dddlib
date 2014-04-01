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

package org.dayatang.querychannel;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yyang
 */
public class CountQueryStringBuilderTest {
    
    public CountQueryStringBuilderTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testBuildQueryStringOfCount() {
        String queryString = "select o from Employee o where o.name = :name order by o.id";
        CountQueryStringBuilder instance = new CountQueryStringBuilder(queryString);
        assertEquals("select count(*) from Employee o where o.name = :name", instance.buildQueryStringOfCount());
    }

    @Test
    public void testBuildQueryStringOfCount_2() {
        String queryString = "select distinct(o) from Employee o where o.name = :name order by o.id";
        CountQueryStringBuilder instance = new CountQueryStringBuilder(queryString);
        assertEquals("select count(distinct o) from Employee o where o.name = :name", instance.buildQueryStringOfCount());
    }

    @Test
    public void testBuildQueryStringOfCount_3() {
        String queryString = "select distinct o from Employee o where o.name = :name order by o.id";
        CountQueryStringBuilder instance = new CountQueryStringBuilder(queryString);
        assertEquals("select count(distinct o) from Employee o where o.name = :name", instance.buildQueryStringOfCount());
    }

    @Test
    public void testContainsGroupByClause() {
        String queryString = "select o.name, sum(o.sex) from Employee o where o.name = :name order by o.id group by o.sex";
        CountQueryStringBuilder instance = new CountQueryStringBuilder(queryString);
        assertTrue(instance.containsGroupByClause());
    }

    @Test
    public void testNotContainsGroupByClause() {
        String queryString = "select o.name, sum(o.sex) from Employee o where o.name = :name order by o.id";
        CountQueryStringBuilder instance = new CountQueryStringBuilder(queryString);
        assertFalse(instance.containsGroupByClause());
    }
    
}
