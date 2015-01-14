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
import org.dayatang.domain.internal.repo.OrCriterion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author yyang
 */
public class OrCriterionTest {
    
    private QueryCriterion criterion1;
    
    private QueryCriterion criterion2;
    
    private QueryCriterion criterion3;
    
    private QueryCriterion criterion4;
    
    private OrCriterion instance;
    
    @Before
    public void setUp() {
        criterion1 = mock(QueryCriterion.class);
        when(criterion1.toQueryString()).thenReturn("a = 1");
        when(criterion1.getParameters()).thenReturn(
                NamedParameters.create().add("name", "abc"));
        
        criterion2 = mock(QueryCriterion.class);
        when(criterion2.toQueryString()).thenReturn("b = 2");
        when(criterion2.getParameters()).thenReturn(
                NamedParameters.create().add("age", 15));
        
        criterion3 = null;
        
        criterion4 = mock(QueryCriterion.class);
        when(criterion4.isEmpty()).thenReturn(Boolean.TRUE);
        
        instance = new OrCriterion(criterion1, criterion2, criterion3, criterion4);
    }

    @Test
    public void testToQueryString() {
        assertEquals("(a = 1 or b = 2)", instance.toQueryString());
    }

    @Test
    public void testGetParameters() {
        assertEquals(NamedParameters.create().add("name", "abc").add("age", 15),
                instance.getParameters());
    }

    @Test
    public void testEquals() {
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(criterion1));
        assertTrue(instance.equals(instance));
        
        OrCriterion other = new OrCriterion(criterion1, criterion2);
        assertTrue(instance.equals(other));
        assertTrue(other.equals(instance));
    }
}
