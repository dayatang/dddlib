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

import org.dayatang.domain.NamedParameters;
import org.dayatang.domain.QueryCriterion;
import org.dayatang.domain.internal.repo.NotCriterion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author yyang
 */
public class NotCriterionTest {
    
    private QueryCriterion other;
    
    private NotCriterion instance;
    
    @Before
    public void setUp() {
        other = mock(QueryCriterion.class);
        when(other.toQueryString()).thenReturn("a = 1");
        when(other.getParameters()).thenReturn(
                NamedParameters.create().add("name", "abc"));
        instance = new NotCriterion(other);
    }

    @Test
    public void testToQueryString() {
        assertEquals("not (a = 1)", instance.toQueryString());
    }

    @Test
    public void testGetParameters() {
        assertEquals(other.getParameters(), instance.getParameters());
    }

    @Test
    public void testEquals() {
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(other));
        assertTrue(instance.equals(instance));
        
        NotCriterion another = new NotCriterion(other);
        assertTrue(instance.equals(another));
        assertTrue(another.equals(instance));
    }
    
}
