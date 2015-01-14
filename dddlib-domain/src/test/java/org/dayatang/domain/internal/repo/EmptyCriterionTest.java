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

package org.dayatang.domain.internal.repo;

import org.dayatang.domain.QueryCriterion;
import org.dayatang.domain.internal.repo.EmptyCriterion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author yyang
 */
public class EmptyCriterionTest {
    
    private EmptyCriterion instance;
    
    private QueryCriterion other;
    
    @Before
    public void setUp() {
        instance = new EmptyCriterion();
        other = mock(QueryCriterion.class);
    }

    @Test
    public void testAnd() {
        assertEquals(other, instance.and(other));
    }

    @Test
    public void testOr() {
        assertEquals(other, instance.or(other));
    }

    @Test
    public void testNot() {
        assertEquals(instance, instance.not());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testToQueryString() {
        assertTrue(instance.toQueryString().isEmpty());
    }

    @Test
    public void testGetParameters() {
        assertTrue(instance.getParameters().getParams().isEmpty());
    }
    
}
