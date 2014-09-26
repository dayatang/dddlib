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

import java.util.Collections;
import java.util.List;
import org.dayatang.querychannel.domain.MyEntity;
import org.dayatang.utils.Page;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yyang
 */
public class PageTest {
    
    private Page<MyEntity> instance;
    private List<MyEntity> data;
    
    @Before
    public void setUp() {
        data = Collections.EMPTY_LIST;
        instance = new Page<MyEntity>(22, 51, 10, data);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetTotalCount() {
        assertEquals(51, instance.getResultCount());
    }

    @Test
    public void testGetPageCount() {
        assertEquals(6, instance.getPageCount());
    }

    @Test
    public void testGetPageSize() {
        assertEquals(10, instance.getPageSize());
    }

    @Test
    public void testGetResults() {
        assertEquals(data, instance.getData());
    }

    @Test
    public void testGetPageIndex() {
        assertEquals(2, instance.getPageIndex());
    }

    @Test
    public void testHasNextPage() {
        assertTrue(instance.hasNextPage());
        instance = new Page<MyEntity>(50, 51, data);
        assertFalse(instance.hasNextPage());
    }

    @Test
    public void testHasPreviousPage() {
        assertTrue(instance.hasPreviousPage());
        instance = new Page<MyEntity>(5, 51, data);
        assertFalse(instance.hasPreviousPage());
    }

    @Test
    public void testGetStartOfPage() {
        assertEquals(20, Page.getStartOfPage(2, 10));
    }
}
